package com.webapp.socialmedia.controller.user;

import com.webapp.socialmedia.dto.requests.RelationshipRequest;
import com.webapp.socialmedia.dto.responses.RelationshipResponse;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.service.IRelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/relationship")
@RequiredArgsConstructor
public class RelationshipController {
    private final IRelationshipService relationshipService;

    @PostMapping("friend-request")
    public ResponseEntity<?> sendFriendRequest(@RequestBody RelationshipRequest relationshipRequest){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RelationshipResponse relationshipResponse = relationshipService.sendFriendRequest(relationshipRequest, user.getId());
        return ResponseEntity.ok(new ResponseDTO().success(relationshipResponse));
    }
    @DeleteMapping("friend-request")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelFriendRequest(@RequestBody RelationshipRequest relationshipRequest){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        relationshipService.cancelFriendRequest(relationshipRequest, user.getId());
    }
    @PostMapping("received-friend-requests")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody RelationshipRequest relationshipRequest){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RelationshipResponse relationshipResponse = relationshipService.acceptFriendRequest(relationshipRequest, user.getId());
        return ResponseEntity.ok(new ResponseDTO().success(relationshipResponse));
    }
    @DeleteMapping("received-friend-requests")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void denyFriendRequest(@RequestBody RelationshipRequest relationshipRequest){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        relationshipService.denyFriendRequest(relationshipRequest,user.getId());
    }

    @DeleteMapping("friends")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@RequestBody RelationshipRequest relationshipRequest){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        relationshipService.deleteFriend(relationshipRequest, user.getId());
    }

    @GetMapping("outgoing-requests")
    public ResponseEntity<?> getAllOutgoingRequest(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserProfileResponse> responses = relationshipService.findByUserIdAndStatus(user.getId(), RelationshipStatus.PENDING);
        return ResponseEntity.ok(new ResponseDTO().success(responses));
    }

    @GetMapping("incoming-requests")
    public ResponseEntity<?> getAllIncomingRequest(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserProfileResponse> responses = relationshipService.findByRelatedUserIdAndStatus(user.getId(), RelationshipStatus.PENDING);
        return ResponseEntity.ok(new ResponseDTO().success(responses));
    }

    @GetMapping("friends")
    public ResponseEntity<?> getAllFriend(@RequestParam String userId){
        List<UserProfileResponse> responses = relationshipService.getFriends(userId);
        return ResponseEntity.ok(new ResponseDTO().success(responses));
    }

    @GetMapping("blocklist")
    public ResponseEntity<?> getBlocklist(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserProfileResponse> responses = relationshipService
                .findByUserIdAndStatus(user.getId(), RelationshipStatus.BLOCK);
        return ResponseEntity.ok(new ResponseDTO().success(responses));
    }

    @PostMapping("blocklist")
    public ResponseEntity<?> blockUser(@RequestBody RelationshipRequest relationshipRequest){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RelationshipResponse relationshipResponse = relationshipService.blockUser(relationshipRequest, user.getId());
        return ResponseEntity.ok(new ResponseDTO().success(relationshipResponse));
    }

    @DeleteMapping("blocklist")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unblockUser(@RequestBody RelationshipRequest relationshipRequest){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        relationshipService.unblockUser(relationshipRequest, user.getId());
    }
}
