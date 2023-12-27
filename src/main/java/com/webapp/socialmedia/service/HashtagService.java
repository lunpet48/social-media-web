package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.responses.PostResponseV2;

import java.util.List;

public interface HashtagService {
    List<PostResponseV2> getPostsByHashtag(String name);
}
