package com.webapp.socialmedia.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator {
    public static boolean isImage(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        return "jpg".equalsIgnoreCase(extension) ||
                "jpeg".equalsIgnoreCase(extension) ||
                "png".equalsIgnoreCase(extension) ||
                "gif".equalsIgnoreCase(extension) ||
                "bmp".equalsIgnoreCase(extension);
    }
}
