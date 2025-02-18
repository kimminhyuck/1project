package com.shop.dto;

import com.shop.constant.QnaStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class QnaResponseDto {
    private Long id;
    private String title;
    private String name;
    private String content;
    private String answer;
    private QnaStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String modifiedBy;
    private boolean isAnsweredByAdmin;
}