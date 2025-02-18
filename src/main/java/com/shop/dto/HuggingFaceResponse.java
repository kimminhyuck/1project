package com.shop.dto;
// 이 클래스가 속한 패키지: 데이터 전송 객체(DTO)를 관리하는 "dto" 패키지에 위치함.

public class HuggingFaceResponse {
    // HuggingFaceResponse: Hugging Face API 응답 데이터를 담는 DTO 클래스

    private String generated_text;
    // API 응답으로 반환되는 "generated_text" 값을 저장하는 필드
    // "generated_text"는 Hugging Face API에서 반환하는 JSON의 키와 일치해야 한다.

    public String getGenerated_text() {
        // generated_text 값을 반환하는 Getter 메서드
        return generated_text;
    }

    public void setGenerated_text(String generated_text) {
        // generated_text 값을 설정하는 Setter 메서드
        this.generated_text = generated_text;
    }
}
