package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.PostMedia;
import com.webapp.socialmedia.repository.PostMediaRepository;
import com.webapp.socialmedia.service.PostMediaService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class PostMediaServiceImpl implements PostMediaService {
    private final PostMediaRepository postMediaRepository;

    @Override
    public List<PostMedia> uploadFiles(List<Media> mediaList, Post post) {
        AtomicInteger num = new AtomicInteger();
        List<PostMedia> postMedia = new ArrayList<>();
        mediaList.forEach(media -> {
            postMedia.add(postMediaRepository.saveAndFlush(PostMedia.builder().mediaId(media.getId()).media(media).post(post).serial(num.getAndIncrement()).build()));
        });
        return postMedia;
    }

    @Override
    public Pair<List<String>, List<PostMedia>> updateFiles(List<PostMedia> files, List<Media> mediaList, Post post) {
        List<PostMedia> oldPostMedia = postMediaRepository.findByPostIdOrderBySerial(post.getId());
        List<String> deleteFile = new ArrayList<>();
        boolean flag;
        for (PostMedia postMedia : oldPostMedia) {
            flag = false;
            for (PostMedia file : files) {
                if (postMedia.getMedia().getLink().equals(file.getMedia().getLink())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                deleteFile.add(postMedia.getMediaId());
                postMediaRepository.deleteOnlyPostMedia(postMedia.getMediaId());
            }
        }
        return new Pair<>(deleteFile, this.uploadFiles(mediaList, post));
    }

    @Override
    public List<PostMedia> getFilesByPostId(String postId) {
        return postMediaRepository.findByPostIdOrderBySerial(postId);
    }
}
