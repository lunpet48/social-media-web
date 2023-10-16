package com.webapp.socialmedia.controller.user;

import com.webapp.socialmedia.dto.requests.RelationshipRequest;
import com.webapp.socialmedia.dto.responses.RelationshipResponse;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.service.IRelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/relationship")
@RequiredArgsConstructor
public class RelationshipController {
    private final IRelationshipService relationshipService;

    @PostMapping("friend-request")
    public ResponseEntity<?> sendFriendRequest(@RequestBody RelationshipRequest relationshipRequest){
        RelationshipResponse relationshipResponse = relationshipService.sendFriendRequest(relationshipRequest);
        return ResponseEntity.ok(relationshipResponse);
    }
    @DeleteMapping("friend-request")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelFriendRequest(@RequestBody RelationshipRequest relationshipRequest){
        relationshipService.cancelFriendRequest(relationshipRequest);
    }
    @PostMapping("received-friend-requests")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody RelationshipRequest relationshipRequest){
        RelationshipResponse relationshipResponse = relationshipService.acceptFriendRequest(relationshipRequest);
        return ResponseEntity.ok(relationshipResponse);
    }
    @DeleteMapping("received-friend-requests")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void denyFriendRequest(@RequestBody RelationshipRequest relationshipRequest){
        relationshipService.denyFriendRequest(relationshipRequest);
    }

    @DeleteMapping("friends")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@RequestBody RelationshipRequest relationshipRequest){
        relationshipService.deleteFriend(relationshipRequest);
    }

    @GetMapping("{userId}/outgoing-requests")
    public ResponseEntity<?> getAllOutgoingRequest(@PathVariable String userId){
        List<RelationshipResponse> relationshipResponses = relationshipService.findByUserIdAndStatus(userId, RelationshipStatus.PENDING);
        return ResponseEntity.ok(relationshipResponses);
    }

    @GetMapping("{userId}/incoming-requests")
    public ResponseEntity<?> getAllIncomingRequest(@PathVariable String userId){
        List<RelationshipResponse> relationshipResponses = relationshipService.findByRelatedUserIdAndStatus(userId, RelationshipStatus.PENDING);
        return ResponseEntity.ok(relationshipResponses);
    }

    @GetMapping("{userId}/friends")
    public ResponseEntity<?> getAllFriend(@PathVariable String userId){
        List<RelationshipResponse> relationshipResponses = relationshipService.findByUserIdAndStatus(userId, RelationshipStatus.FRIEND);
        return ResponseEntity.ok(relationshipResponses);
    }

    @GetMapping("{userId}/blocklist")
    public ResponseEntity<?> getBlocklist(@PathVariable String userId){
        List<RelationshipResponse> relationshipResponses = relationshipService.findByUserIdAndStatus(userId, RelationshipStatus.BLOCK);
        return ResponseEntity.ok(relationshipResponses);
    }

//    @PostMapping("blocklist")
//    public ResponseEntity<?> blockUser(){
//        return ResponseEntity.ok("");
//    }
}
