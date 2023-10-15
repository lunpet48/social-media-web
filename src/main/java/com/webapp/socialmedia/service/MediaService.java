package com.webapp.socialmedia.service;

import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    List<Media> uploadFiles(MultipartFile[] multipartFiles, Post post);

    void deleteFiles(List<String> mediaIds);
}
