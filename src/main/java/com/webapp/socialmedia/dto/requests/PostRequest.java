package com.webapp.socialmedia.dto.requests;

import com.webapp.socialmedia.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String postType;
    private String postMode;
    private String caption;
    private Set<String> tagList;
}
