package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalleryDTO {

    private Integer galleryId;
    private Integer userId;
    private String mediaType; // "Photo" or "Video"
    private String mediaUrl;
    private String uploadedAt; // (yyyy-MM-dd HH:mm:ss) 등으로 포맷 가능
}
