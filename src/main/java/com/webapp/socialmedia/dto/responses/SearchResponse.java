package com.webapp.socialmedia.dto.responses;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    List<UserProfileResponse> users;
    List<PostResponseV2> posts;
}
