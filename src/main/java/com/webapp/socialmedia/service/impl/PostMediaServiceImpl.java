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

@Service
@RequiredArgsConstructor
public class PostMediaServiceImpl implements PostMediaService {
    private final PostMediaRepository postMediaRepository;

    @Override
    public List<PostMedia> uploadFiles(List<Media> mediaList, Post post) {
        List<PostMedia> postMedia = new ArrayList<>();
        mediaList.forEach(media -> {
            postMedia.add(postMediaRepository.save(PostMedia.builder().mediaId(media.getId()).post(post).build()));
        });
        return postMedia;
    }

    @Override
    public Pair<List<String>, List<PostMedia>> updateFiles(List<PostMedia> files, List<Media> mediaList, Post post) {
        List<PostMedia> oldPostMedia = postMediaRepository.findByPostId(post.getId());
        List<String> deleteFile = new ArrayList<>();
//        oldPostMedia.forEach(old -> {
//            files.forEach(file -> {
//                if(!file.getMediaId().equals(old.getMediaId())){
//                    deleteFile.add(old.getMediaId());
//                    postMediaRepository.deleteById(old.getMediaId());
//                }
//            });
//            /*if (!file.contains(old)) {
//                deleteFile.add(old.getMediaId());
//                postMediaRepository.deleteById(old.getMediaId());
//            }*/
//        });
        boolean flag = false;
        for(int i = 0; i < oldPostMedia.size(); i++) {
            flag = false;
            for(int j = 0; j < files.size(); j++) {
                if(oldPostMedia.get(i).getMediaId().equals(files.get(j).getMediaId())) {
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                deleteFile.add(oldPostMedia.get(i).getMediaId());
                postMediaRepository.deleteById(oldPostMedia.get(i).getMediaId());
            }
        }
        return new Pair<>(deleteFile, this.uploadFiles(mediaList, post));
    }
}
