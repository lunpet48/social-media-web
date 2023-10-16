package com.webapp.socialmedia.service;

import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.PostMedia;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;

public interface PostMediaService {
    List<PostMedia> uploadFiles(List<Media> mediaList, Post post);

    Pair<List<String>, List<PostMedia>> updateFiles(List<PostMedia> files, List<Media> mediaList, Post post);
}
