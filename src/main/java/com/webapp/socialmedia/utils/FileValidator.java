package com.webapp.socialmedia.utils;

import org.springframework.web.multipart.MultipartFile;

public class FileValidator {
    public static boolean isImage(MultipartFile file) {
        try {
            String mimeType = file.getContentType();
            return mimeType.startsWith("image/");
        }
        catch (Exception e) {
            return false;
        }
    }

    public static boolean isVideo(MultipartFile file) {
        try {
            String mimeType = file.getContentType();
            return mimeType.startsWith("video/");
        }
        catch (Exception e) {
            return false;
        }
    }
}
