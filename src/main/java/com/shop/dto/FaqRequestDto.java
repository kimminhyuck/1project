package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqRequestDto {
    // FAQ 생성 또는 수정 요청 시 사용되는 데이터 전달 객체 (DTO)

    private String question;
    // 클라이언트로부터 입력받는 FAQ의 질문 내용

    private String answer;
    // 클라이언트로부터 입력받는 FAQ의 답변 내용

    // Getter와 Setter가 필요하다면 Lombok(@Data, @Getter, @Setter 등)을 사용할 수 있습니다.
    // 또는 직접 Getter와 Setter 메서드를 추가할 수 있습니다.

    /*
    예시:
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    */

    /*
    이 클래스의 역할:
    - 클라이언트가 HTTP 요청(예: POST, PUT)을 통해 서버에 데이터를 보낼 때 사용됨
    - 주로 Controller에서 @RequestBody로 JSON 데이터를 받을 때 사용됨
    - 예를 들어:
      {
          "question": "영업시간이 어떻게 되나요?",
          "answer": "평일 오전 9시부터 오후 6시까지입니다."
      }
    */
}

