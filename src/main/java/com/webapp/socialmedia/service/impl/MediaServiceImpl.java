package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.repository.MediaRepository;
import com.webapp.socialmedia.service.CloudService;
import com.webapp.socialmedia.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final CloudService cloudService;
    private final MediaRepository mediaRepository;

    @Override
    public List<Media> uploadFiles(MultipartFile[] multipartFiles, Post post) {
        if (multipartFiles != null) {
            return mediaRepository.saveAll(
                    cloudService.uploadFiles(multipartFiles, post.getUser().getId(), post.getType()));
        }
        return new ArrayList<>();
    }

    @Override
    public Media uploadFile(MultipartFile multipartFile, String path) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (multipartFile != null && !multipartFile.isEmpty()) {
            return mediaRepository.save(cloudService.uploadFile(multipartFile, user.getId(), path));
        }
        return new Media();
    }

    @Override
    public void deleteFiles(List<String> mediaIds) {
        List<Media> mediaList = mediaRepository.findAllById(mediaIds);
        mediaList.forEach(media -> {
            media.setIsDeleted(true);
        });
        mediaRepository.saveAll(mediaList);
    }
}
