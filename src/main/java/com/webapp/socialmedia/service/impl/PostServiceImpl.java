package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.PostRequest;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.entity.*;
import com.webapp.socialmedia.enums.NotificationType;
import com.webapp.socialmedia.enums.PostMode;
import com.webapp.socialmedia.enums.PostType;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.exceptions.*;
import com.webapp.socialmedia.mapper.NotificationMapper;
import com.webapp.socialmedia.mapper.UserMapper;
import com.webapp.socialmedia.repository.*;
import com.webapp.socialmedia.service.PostService;
import com.webapp.socialmedia.utils.NotificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final RelationshipRepository relationshipRepository;
    private final PostTagRepository postTagRepository;
    private final UserMapper userMapper;
    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    Pattern patternUser = Pattern.compile("@[A-z0-9_]+");

    @Override
    public Post createPost(PostRequest postRequest) {
        Matcher matcher = patternUser.matcher(postRequest.getCaption());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Tag> tagResult = new ArrayList<>();
        if (postRequest.getTagList() != null)
            postRequest.getTagList().forEach(tag -> {
                Optional<Tag> temp = tagRepository.findById(tag.toLowerCase());
                if (temp.isEmpty())
                    tagResult.add(tagRepository.save(Tag.builder().id(tag.toLowerCase()).build()));
                else
                    tagResult.add(temp.get());
            });

        List<PostTag> postTag = new ArrayList<>();

        Post post = postRepository.saveAndFlush(Post.builder().user(user)
                .mode(PostMode.valueOf(postRequest.getPostMode()))
                .type(PostType.valueOf(postRequest.getPostType()))
                .caption(postRequest.getCaption().isEmpty() ? "" : postRequest.getCaption())
                .build());
        tagResult.forEach(tag -> {
            postTag.add(postTagRepository.saveAndFlush(PostTag.builder().id(new PostTagId(post.getId(), tag.getId())).tag(tag).post(post).build()));
        });

        post.setPostTags(postTag);
        postRepository.save(post);
        //Thông báo

        while (matcher.find()) {
            String username = matcher.group().substring(1);
            User receiver = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

            Notification response = notificationRepository.saveAndFlush(Notification.builder()
                    .receiver(receiver)
                    .actor(post.getUser())
                    .idType(post.getId())
                    .notificationType(NotificationType.MENTION)
                    .build());

            simpMessagingTemplate.convertAndSendToUser(username, NotificationUtils.NOTIFICATION_LINK, notificationMapper.toResponse(response));
        }

        return post;
    }

    @Override
    @Transactional
    public Post updatePost(Post post, List<PostMedia> postMediaList, MultipartFile[] files, String userId) throws PostNotFoundException, PostCannotUploadException {
        Post oldPost = postRepository.findById(post.getId()).orElseThrow(() -> new PostNotFoundException("Bài đăng không tồn tại hoặc đã bị ẩn"));
        if (!oldPost.getUser().getId().equals(userId))
            throw new RuntimeException("Người dùng không có quyền");
        if (postMediaList.isEmpty() && files == null)
            throw new PostCannotUploadException("Không thể đăng/chỉnh sửa bài viết nếu thiếu hình ảnh hoặc video!!!");
        oldPost.setType(post.getType());
        oldPost.setMode(post.getMode());
        oldPost.setCaption(post.getCaption());
        oldPost.setPostTags(new ArrayList<>());
        //Post postUseForSaveTag = postRepository.saveAndFlush(oldPost);

        List<Tag> tags = new ArrayList<>();

        post.getPostTags().forEach(postTag -> {
            Optional<Tag> temp = tagRepository.findById(postTag.getId().getTagId());
            if (temp.isEmpty())
                tags.add(tagRepository.saveAndFlush(Tag.builder().id(postTag.getId().getTagId()).build()));
            else
                tags.add(temp.get());
        });

        postTagRepository.deletePostTagsById_PostId(oldPost.getId());


        List<PostTag> newPostTag = new ArrayList<>();

        tags.forEach(tag -> {
            PostTag temp = PostTag.builder().id(PostTagId.builder().tagId(tag.getId()).postId(oldPost.getId()).build()).tag(tag).post(oldPost).build();
            Optional<PostTag> postTag = postTagRepository.findById(temp.getId());
            newPostTag.add(postTagRepository.save(temp));
        });

//        tags.forEach(tag -> {
//            newPostTag.add(PostTag.builder().id(PostTagId.builder().tagId(tag.getId()).postId(oldPost.getId()).build()).tag(tag).post(oldPost).build());
//        });

        oldPost.setPostTags(newPostTag);
        oldPost.setUpdatedAt(new Date());


//        Set<Tag> tags = new HashSet<>();
//        post.getTags().forEach(tag -> {
//            if (tagRepository.findById(tag.getId().toLowerCase()).isEmpty())
//                tags.add(tagRepository.saveAndFlush(Tag.builder().id(tag.getId().toLowerCase()).build()));
//            tags.add(tag);
//        });
//        oldPost.setTags(tags);

        //Thông báo
        Matcher matcher = patternUser.matcher(post.getCaption());
        while (matcher.find()) {
            String username = matcher.group().substring(1);
            User receiver = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

            Notification response = notificationRepository.saveAndFlush(Notification.builder()
                    .receiver(receiver)
                    .actor(oldPost.getUser())
                    .idType(oldPost.getId())
                    .notificationType(NotificationType.MENTION)
                    .build());

            simpMessagingTemplate.convertAndSendToUser(username, NotificationUtils.NOTIFICATION_LINK, notificationMapper.toResponse(response));
        }

        return postRepository.save(oldPost);
    }

    @Override
    public void deletePost(String postId, String userId) throws PostNotFoundException, UserNotAuthoritativeException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Bài đăng không tồn tại hoặc đã bị ẩn"));
        if (!post.getUser().getId().equals(userId))
            throw new UserNotAuthoritativeException("Người dùng không có quyền hạn này");

        post.setIsDeleted(true);
        postRepository.save(post);
    }

    @Override
    public Post getPost(String postId, String userId) throws PostNotFoundException {
        Post post = postRepository.findByIdAndIsDeleted(postId, false).orElseThrow(() -> new PostNotFoundException("Bài đăng không tồn tại hoặc đã bị ẩn"));

        PostMode mode = post.getMode();
        if (post.getUser().getId().equals(userId)) return post;
        if (mode.equals(PostMode.PUBLIC)) {
            return post;
        }
        if (mode.equals(PostMode.FRIEND)) {
            Optional<Relationship> relationship = relationshipRepository.findByUserIdAndRelatedUserIdAndStatus(post.getUser().getId(), userId, RelationshipStatus.FRIEND);
            if (relationship.isPresent())
                return post;
        }
        throw new PostNotFoundException("Bài đăng không tồn tại hoặc đã bị ẩn");
    }

    @Override
    public List<Post> getListPostByUserIdAndIsDeleted(String userId) {
        User userRelated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userRelated.getId().equals(userId))
            return postRepository.findByUser_IdAndIsDeletedOrderByCreatedAtDesc(userId, Boolean.FALSE);
        Optional<Relationship> relationship = relationshipRepository.findByUserIdAndRelatedUserId(userId, userRelated.getId());

        if (relationship.isPresent()) {
            if (relationship.get().getStatus().equals(RelationshipStatus.FRIEND))
                return postRepository.findPostsWithFriends(userId);
            else if (relationship.get().getStatus().equals(RelationshipStatus.BLOCK))
                throw new UserNotFoundException();
        }

        return postRepository.findPostWithPublic(userId);
    }

    @Override
    public List<Post> getHomepage(int pageSize, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Post> result = new ArrayList<>();
        relationshipRepository.findByUserIdAndStatus(user.getId(), RelationshipStatus.FRIEND).forEach(
                relationship -> {
                    //result.addAll(postRepository.findPostsWithFriendsAndDay(relationship.getRelatedUser().getId(), 1000, pageSize, pageNo * pageSize));
                    result.addAll(postRepository.findByUser_IdAndModeIsNotAndIsDeletedIsFalse(relationship.getRelatedUser().getId(), PostMode.PRIVATE, pageable));
                }
        );

        result.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        return result;
    }
    @Override
    public List<UserProfileResponse> getLikesOfPost(String postId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findByIdAndIsDeleted(postId, false).orElseThrow(PostNotFoundException::new);
        List<UserProfileResponse> responses = new ArrayList<>();
        for (Reaction reaction:post.getReactionList()) {
            UserProfileResponse userProfileResponse = userMapper.userToUserProfileResponse(reaction.getUser(), currentUser.getId());
            responses.add(userProfileResponse);
        }
        return responses;
    }

    @Override
    public Post sharePost(PostRequest sharedPostRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findByIdAndIsDeleted(sharedPostRequest.getSharedPost(), Boolean.FALSE).orElseThrow(PostNotFoundException::new);

        Relationship relationship = relationshipRepository.findByUserIdAndRelatedUserId(user.getId(), post.getUser().getId()).orElse(null);

        List<Tag> tagResult = new ArrayList<>();

        if(post.getMode().equals(PostMode.PUBLIC) ||
            relationship != null && post.getMode().equals(PostMode.FRIEND) && relationship.getStatus().equals(RelationshipStatus.FRIEND) ||
            post.getUser().equals(user)) {

            if (sharedPostRequest.getTagList() != null)
                sharedPostRequest.getTagList().forEach(tag -> {
                    Optional<Tag> temp = tagRepository.findById(tag.toLowerCase());
                    if (temp.isEmpty())
                        tagResult.add(tagRepository.save(Tag.builder().id(tag.toLowerCase()).build()));
                    else
                        tagResult.add(temp.get());
                });

            List<PostTag> postTag = new ArrayList<>();

            Post sharedPost = postRepository.saveAndFlush(Post.builder().user(user)
                    .mode(PostMode.valueOf(sharedPostRequest.getPostMode()))
                    .type(PostType.valueOf(sharedPostRequest.getPostType()))
                    .caption(sharedPostRequest.getCaption().isEmpty() ? "" : sharedPostRequest.getCaption())
                    .build());
            tagResult.forEach(tag -> {
                postTag.add(postTagRepository.saveAndFlush(PostTag.builder().id(new PostTagId(sharedPost.getId(), tag.getId())).tag(tag).post(sharedPost).build()));
            });

            sharedPost.setPostTags(postTag);
            sharedPost.setSharedPost(post);

            return postRepository.saveAndFlush(sharedPost);
        }

        //Trả về thông báo lỗi
        throw new BadRequestException("Không tìm thấy người dùng/bài viết");
    }

    @Override
    public List<Post> getAllSharedPost(String userId) {
        User userRelated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userRelated.getId().equals(userId))
            return postRepository.findByUser_IdAndSharedPostIsNotNullAndIsDeletedOrderByCreatedAtDesc(userId, Boolean.FALSE);
        Optional<Relationship> relationship = relationshipRepository.findByUserIdAndRelatedUserId(userId, userRelated.getId());

        if (relationship.isPresent()) {
            if (relationship.get().getStatus().equals(RelationshipStatus.FRIEND))
                return postRepository.findSharedPostsWithFriends(userId);
            else if (relationship.get().getStatus().equals(RelationshipStatus.BLOCK))
                throw new UserNotFoundException();
        }

        return postRepository.findSharedPostWithPublic(userId);
    }
}
