package com.shop.dto;
// 이 클래스가 속한 패키지: 데이터 전송 객체(DTO)를 관리하는 "dto" 패키지에 위치함.

public class HuggingFaceRequest {
    // HuggingFaceRequest: Hugging Face API 요청에 사용되는 데이터 전송 객체(DTO) 클래스

    private String inputs;
    // API 요청에 전달할 입력 데이터를 저장하는 필드
    // "inputs"는 Hugging Face API의 요청 JSON 필드명과 일치하도록 설정됨

    public HuggingFaceRequest(String inputs) {
        // 생성자: 이 클래스를 생성할 때 입력값을 받아 필드를 초기화함
        this.inputs = inputs;
    }

    public String getInputs() {
        // inputs 필드의 값을 반환하는 Getter 메서드
        return inputs;
    }

    public void setInputs(String inputs) {
        // inputs 필드의 값을 설정하는 Setter 메서드
        this.inputs = inputs;
    }
}
