package com.webapp.socialmedia.service;

import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.enums.PostType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CloudService {
    Media uploadFile(MultipartFile multipartFile, String userId, PostType postType) throws IOException;

    Object getFile(String assetId) throws Exception;

    List<Media> uploadFiles(MultipartFile[] multipartFiles, String userId, PostType postType);
}
