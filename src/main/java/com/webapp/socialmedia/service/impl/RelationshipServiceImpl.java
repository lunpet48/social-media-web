package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.RelationshipRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponseV2;
import com.webapp.socialmedia.dto.responses.RelationshipResponse;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.entity.Notification;
import com.webapp.socialmedia.entity.Relationship;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.NotificationType;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.exceptions.RelationshipNotFoundException;
import com.webapp.socialmedia.mapper.NotificationMapper;
import com.webapp.socialmedia.mapper.RelationshipMapper;
import com.webapp.socialmedia.mapper.UserMapper;
import com.webapp.socialmedia.repository.NotificationRepository;
import com.webapp.socialmedia.repository.RelationshipRepository;
import com.webapp.socialmedia.service.IRelationshipService;
import com.webapp.socialmedia.utils.NotificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RelationshipServiceImpl implements IRelationshipService {
    private final RelationshipRepository relationshipRepository;
    private final RelationshipMapper relationshipMapper;
    private final UserMapper userMapper;
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpUserRegistry simpUserRegistry;
    @Override
    public RelationshipResponse sendFriendRequest(RelationshipRequest relationshipRequest, String userId) {
        Optional<Relationship> relationshipOptional = relationshipRepository
                .findByUserIdAndRelatedUserId(
                        relationshipRequest.getTargetId(),
                        userId
                );
        if(relationshipOptional.isPresent()){
            Relationship relationshipCheck = relationshipOptional.get();
            if(relationshipCheck.getStatus().equals(RelationshipStatus.PENDING)){
                throw new BadRequestException("Đối phương đã gửi lời mời đến bạn");
            }
            if(relationshipCheck.getStatus().equals(RelationshipStatus.FRIEND)){
                throw new BadRequestException("Các bạn đã là bạn bè");
            }
        }

        Relationship newRelationship = relationshipMapper.ToRelationship(userId, relationshipRequest.getTargetId(), RelationshipStatus.PENDING);

        //Thông báo
        Relationship temp = relationshipRepository.saveAndFlush(newRelationship);
        Notification newNoti = Notification.builder()
                .actor(temp.getUser())
                .receiver(temp.getRelatedUser())
                .notificationType(NotificationType.FRIEND_REQUEST)
                .build();
        notificationRepository.saveAndFlush(newNoti);
        simpMessagingTemplate.convertAndSendToUser(newNoti.getReceiver().getUsername(), NotificationUtils.NOTIFICATION_LINK, notificationMapper.toResponse(newNoti));

        return relationshipMapper.RelationshipToRelationshipResponse(newRelationship);
    }

    @Override
    public void cancelFriendRequest(RelationshipRequest relationshipRequest, String userId) {
        Optional<Relationship> relationshipOptional = relationshipRepository
                .findByUserIdAndRelatedUserIdAndStatus(
                        userId,
                        relationshipRequest.getTargetId(),
                        RelationshipStatus.PENDING
                );

        if(relationshipOptional.isEmpty())
            throw new RelationshipNotFoundException("Không tìm thấy yêu cầu");

        Relationship relationship = relationshipOptional.get();
        relationshipRepository.delete(relationship);
    }

    @Override
    public RelationshipResponse acceptFriendRequest(RelationshipRequest relationshipRequest, String userId) {
        Optional<Relationship> relationshipOptional = relationshipRepository
                .findByUserIdAndRelatedUserIdAndStatus(
                        relationshipRequest.getTargetId(),
                        userId,
                        RelationshipStatus.PENDING
                );

        if(relationshipOptional.isEmpty())
            throw new RelationshipNotFoundException("Không tìm thấy yêu cầu");

        Relationship relationship = relationshipMapper.ToRelationship(userId, relationshipRequest.getTargetId(), RelationshipStatus.FRIEND);

        Relationship reverseRelationship = relationshipOptional.get();
        reverseRelationship.setStatus(RelationshipStatus.FRIEND);

        relationshipRepository.save(reverseRelationship);
        relationshipRepository.save(relationship);

        //Thông báo
        Notification newNoti = Notification.builder()
                .actor(relationship.getUser())
                .receiver(relationship.getRelatedUser())
                .notificationType(NotificationType.FRIEND_ACCEPT)
                .build();
        notificationRepository.saveAndFlush(newNoti);
        simpMessagingTemplate.convertAndSendToUser(newNoti.getReceiver().getUsername(), NotificationUtils.NOTIFICATION_LINK, notificationMapper.toResponse(newNoti));

        return relationshipMapper.RelationshipToRelationshipResponse(relationship);
    }

    @Override
    public void denyFriendRequest(RelationshipRequest relationshipRequest, String userId) {

        Optional<Relationship> relationshipOptional = relationshipRepository
                .findByUserIdAndRelatedUserIdAndStatus(
                        relationshipRequest.getTargetId(),
                        userId,
                        RelationshipStatus.PENDING
                );

        if(relationshipOptional.isEmpty())
            throw new RelationshipNotFoundException("Không tìm thấy yêu cầu");

        Relationship relationship = relationshipOptional.get();

        relationshipRepository.delete(relationship);
    }

    @Override
    public void deleteFriend(RelationshipRequest relationshipRequest, String userId) {

        Optional<Relationship> relationshipOptional = relationshipRepository
                .findByUserIdAndRelatedUserIdAndStatus(
                        userId,
                        relationshipRequest.getTargetId(),
                        RelationshipStatus.FRIEND
                );
        if(relationshipOptional.isEmpty())
            throw new RelationshipNotFoundException("Không tìm thấy yêu cầu");
        Relationship relationship = relationshipOptional.get();

        Optional<Relationship> reverseRelationshipOptional = relationshipRepository
                .findByUserIdAndRelatedUserIdAndStatus(
                        relationshipRequest.getTargetId(),
                        userId,
                        RelationshipStatus.FRIEND
                );
        if(reverseRelationshipOptional.isEmpty())
            throw new RelationshipNotFoundException("Không tìm thấy yêu cầu");
        Relationship reverseRelationship = reverseRelationshipOptional.get();

        relationshipRepository.delete(relationship);
        relationshipRepository.delete(reverseRelationship);
    }

    @Override
    public List<UserProfileResponse> findByUserIdAndStatus(String userId,RelationshipStatus status){
        List<Relationship> relationships = relationshipRepository.findByUserIdAndStatus(userId,status);
        List<UserProfileResponse> responses = new ArrayList<>();
        for (Relationship relationship: relationships) {
            UserProfileResponse userProfileResponse = userMapper.userToUserProfileResponse(relationship.getRelatedUser(), userId);
            responses.add(userProfileResponse);
        }
        return responses;
    }

    @Override
    public List<UserProfileResponse> getFriends(String userId){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Relationship> relationships = relationshipRepository.findByUserIdAndStatus(userId,RelationshipStatus.FRIEND);
        List<UserProfileResponse> responses = new ArrayList<>();
        for (Relationship relationship: relationships) {
            UserProfileResponse userProfileResponse = userMapper.userToUserProfileResponse(relationship.getRelatedUser(), currentUser.getId());
            responses.add(userProfileResponse);
        }
        return responses;
    }

    @Override
    public List<UserProfileResponse> findByRelatedUserIdAndStatus(String userId, RelationshipStatus status) {
        List<Relationship> relationships = relationshipRepository.findByRelatedUserIdAndStatus(userId,status);
        List<UserProfileResponse> responses = new ArrayList<>();
        for (Relationship relationship: relationships) {
            UserProfileResponse userProfileResponse = userMapper.userToUserProfileResponse(relationship.getUser(), userId);
            responses.add(userProfileResponse);
        }
        return responses;
    }

    @Override
    public RelationshipResponse blockUser(RelationshipRequest relationshipRequest, String userId) {
        try {
            deleteFriend(relationshipRequest, userId);
        }
        catch (Exception ignored){}

        Relationship newRelationship = relationshipMapper.ToRelationship(userId, relationshipRequest.getTargetId(), RelationshipStatus.BLOCK);

        relationshipRepository.save(newRelationship);
        return relationshipMapper.RelationshipToRelationshipResponse(newRelationship);
    }

    @Override
    public void unblockUser(RelationshipRequest relationshipRequest, String userId) {
        Relationship relationship = relationshipRepository.findByUserIdAndRelatedUserIdAndStatus(
                userId,
                relationshipRequest.getTargetId(),
                RelationshipStatus.BLOCK
        ).orElseThrow(()-> new RelationshipNotFoundException("Không tìm thấy yêu cầu"));
        relationshipRepository.delete(relationship);
    }

    @Override
    public List<ProfileResponseV2> getOnlineUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ProfileResponseV2> responseV2s = new ArrayList<>();

        List<Relationship> relationships = relationshipRepository.findByUserIdAndStatus(user.getId(), RelationshipStatus.FRIEND);
        List<String> simpUsers = simpUserRegistry.getUsers().stream().map(SimpUser::getName).toList();


        for (Relationship relationship : relationships) {
            if(simpUsers.contains(relationship.getRelatedUser().getUsername())) {
                responseV2s.add(ProfileResponseV2.builder()
                                .userId(relationship.getRelatedUser().getId())
                                .avatar(relationship.getRelatedUser().getProfile().getAvatar())
                                .username(relationship.getRelatedUser().getUsername())
                        .build());
            }
        }

        return responseV2s;
    }
}
