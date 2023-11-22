package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.responses.SearchResponse;

public interface SearchService {
    SearchResponse search(String keyword);
}
