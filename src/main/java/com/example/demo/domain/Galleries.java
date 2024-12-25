package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "galleries")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Galleries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gallery_id")
    private Integer galleryId;

    // 사용자 ID (외래 키)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType; // 'Photo', 'Video'

    private String mediaUrl; // 미디어 파일 URL

    @Builder.Default
    private LocalDateTime uploadedAt = LocalDateTime.now(); // 업로드 시간

    public enum MediaType {
        Photo, Video
    }
}
