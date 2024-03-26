package com.webapp.socialmedia.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.enums.MediaType;
import com.webapp.socialmedia.enums.PostType;
import com.webapp.socialmedia.service.CloudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CloudServiceImpl implements CloudService {
    final Cloudinary cloudinary;
    @Override
    public Media uploadFile(MultipartFile multipartFile, String userId, PostType postType) throws IOException {
        var u =  cloudinary.uploader().upload(multipartFile.getBytes(),
                ObjectUtils.asMap("public_id", UUID.randomUUID().toString(),
                        "resource_type", "auto",
                        "folder", userId + "/" +postType.name()));

        return Media.builder().id(u.get("asset_id").toString()).link(u.get("secure_url").toString()).type(MediaType.valueOf(u.get("resource_type").toString().toUpperCase())).build();
    }

    @Override
    public Media uploadFile(MultipartFile multipartFile, String userId, String path) throws IOException {
        var u =  cloudinary.uploader().upload(multipartFile.getBytes(),
                ObjectUtils.asMap("public_id", UUID.randomUUID().toString(),
                        "resource_type", "auto",
                        "folder", userId + "/" + path));

        return Media.builder().id(u.get("asset_id").toString()).link(u.get("secure_url").toString()).type(MediaType.valueOf(u.get("resource_type").toString().toUpperCase())).build();
    }

    @Override
    public Media uploadFile(MultipartFile multipartFile) throws IOException {
        var u =  cloudinary.uploader().upload(multipartFile.getBytes(),
                ObjectUtils.asMap("public_id", UUID.randomUUID().toString(),
                        "resource_type", "auto",
                        "folder", ""));

        return Media.builder().id(u.get("asset_id").toString()).link(u.get("secure_url").toString()).type(MediaType.valueOf(u.get("resource_type").toString().toUpperCase())).build();
    }

    @Override
    public Object getFile(String assetId) throws Exception{
        return cloudinary.api().resourceByAssetID(assetId, ObjectUtils.emptyMap());
    }

    @Override
    public List<Media> uploadFiles(MultipartFile[] multipartFiles, String userId, PostType postType) {
        List<Media> result = new ArrayList<>();
        Arrays.stream(multipartFiles).toList().forEach(file -> {
            try {
                result.add(this.uploadFile(file, userId, postType));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return result;
    }
}
