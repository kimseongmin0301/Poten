package com.poten.hoohae.client.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String subject;
    private String body;
    private Long vote;
    private Long commentCnt;
    private Long thumbnail;
    private String userId;
    private Long age;
    private Boolean isAdopte;
    private String nickname;
    private Long category;
    private String type;
    private LocalDateTime createdAt;
    private List<String> images;
}
