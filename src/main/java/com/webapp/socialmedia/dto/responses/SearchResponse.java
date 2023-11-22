package com.webapp.socialmedia.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    List<UserProfileResponse> users;
    List<PostResponseV2> posts;
}
