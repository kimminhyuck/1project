package com.shop.dto;
// 이 클래스가 속한 패키지: 데이터 전송 객체(DTO)를 관리하는 "dto" 패키지에 위치함.

import lombok.Getter;
import lombok.Setter;
// Lombok의 @Getter와 @Setter 어노테이션을 사용하여 getter와 setter 메서드를 자동으로 생성한다.

@Getter
@Setter
// Lombok 어노테이션:
// @Getter: 모든 필드에 대해 getter 메서드를 자동으로 생성.
// @Setter: 모든 필드에 대해 setter 메서드를 자동으로 생성.

public class FaqResponseDto {
    // FaqResponseDto: FAQ 데이터를 클라이언트에게 응답으로 전달하기 위한 DTO 클래스.

    private Long id;
    // FAQ 항목의 고유 ID (Primary Key)

    private String question;
    // FAQ의 질문 내용

    private String answer;
    // FAQ의 답변 내용


}
