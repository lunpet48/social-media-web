package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.PostMedia;
import com.webapp.socialmedia.enums.PostType;
import com.webapp.socialmedia.repository.MediaRepository;
import com.webapp.socialmedia.repository.PostMediaRepository;
import com.webapp.socialmedia.service.CloudService;
import com.webapp.socialmedia.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void deleteFiles(List<String> mediaIds) {
        List<Media> mediaList = mediaRepository.findAllById(mediaIds);
        mediaList.forEach(media -> {
            media.setIsDeleted(true);
        });
        mediaRepository.saveAll(mediaList);
    }
}
