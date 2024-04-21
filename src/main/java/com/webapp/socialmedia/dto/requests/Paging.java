package com.webapp.socialmedia.dto.requests;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paging {
    Integer pageNo;
    Integer pageSize;
}
