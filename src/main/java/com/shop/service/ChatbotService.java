package com.shop.service;


import com.shop.dto.HuggingFaceRequest;
import com.shop.dto.HuggingFaceResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatbotService {
    private final RestTemplate restTemplate = new RestTemplate();
    // 외부 API 호출을 위해 사용하는 Spring 제공 HTTP 클라이언트 (여기선 HuggingFace API를 호출하는데 사용)

    private static final String API_URL = "https://api-inference.huggingface.co/models/microsoft/DialoGPT-medium";
    // HuggingFace의 DialoGPT 모델을 호출하기 위한 API 엔드포인트 URL
    // 사용자 질문에 대해 AI 모델이 답변 텍스트를 생성하는데 사용

    // 미리 정의된 질문에 대한 답변을 담아둘 맵 (Key: 질문, Value: 답변)
    private final Map<String, String> predefinedResponses = new HashMap<>();

    public ChatbotService() {
        // 서비스 클래스가 생성될 때 미리 정의된 응답들을 초기화한다.
        initializePredefinedResponses();
    }

    private void initializePredefinedResponses() {
        // 자주 묻는 질문들에 대해 미리 준비된 답변을 맵에 넣는다.

        predefinedResponses.put("영업시간이 어떻게 되나요?",
                "저희 매장은 평일 오전 9시부터 오후 6시까지 운영됩니다. 주말은 오전 10시부터 오후 5시까지입니다. 공휴일은 휴무입니다.");

        predefinedResponses.put("반품 절차를 알려주세요.",
                "반품은 상품 수령 후 7일 이내에 가능합니다.\n1. 고객센터(1234-5678)로 반품 접수\n2. 반품 사유 작성\n3. 배송기사 방문 수거\n4. 상품 검수 후 환불 진행");

        predefinedResponses.put("배송조회는 어떻게 하나요?",
                "배송조회는 다음과 같은 방법으로 가능합니다:\n1. 홈페이지 상단 '배송조회' 메뉴 클릭\n2. 주문번호 또는 운송장번호 입력\n3. 실시간 배송현황 확인");

        predefinedResponses.put("취소/환불 규정이 궁금합니다.",
                "주문 취소는 배송 시작 전까지 가능합니다. 환불은 다음과 같이 진행됩니다:\n- 카드결제: 취소 후 3-5일 소요\n- 계좌이체: 취소 후 3-7일 소요\n- 포인트: 즉시 환불");

        predefinedResponses.put("적립금 사용방법 알려주세요.",
                "적립금은 1,000원 이상부터 사용 가능합니다.\n1. 결제 페이지에서 '적립금 사용' 선택\n2. 사용할 금액 입력\n3. 최대 구매금액의 5%까지 사용 가능");
    }

    public String getChatbotResponse(String userInput) {
        // 사용자가 입력한 질문(userInput)에 대해 챗봇의 응답을 반환하는 메서드

        // 먼저 미리 정의된 응답이 있는지 확인
        // predefinedResponses에 키로 질문이 있으면 미리 준비한 답변을 즉시 반환한다.
        if (predefinedResponses.containsKey(userInput)) {
            return predefinedResponses.get(userInput);
        }

        // 미리 정의되지 않은 질문이라면, HuggingFace API를 통해 AI 모델에 질문을 던져 답변을 받는다.

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 요청 헤더에 JSON 형식 사용을 명시

        headers.set("Authorization", "Bearer YOUR_API_KEY");
        // HuggingFace API 호출을 위한 인증 토큰 설정 (YOUR_API_KEY 부분은 실제 키로 교체해야 함)

        HuggingFaceRequest request = new HuggingFaceRequest(userInput);
        // 사용자 입력을 담은 요청 객체 생성 (HuggingFaceRequest는 요청 바디 구조를 정의한 DTO 클래스)

        HttpEntity<HuggingFaceRequest> entity = new HttpEntity<>(request, headers);
        // 요청 바디와 헤더를 함께 감싼 HttpEntity 객체 생성, 이걸로 실제 API 호출 시 전달한다.

        try {
            // restTemplate.exchange를 사용해 POST 방식으로 HuggingFace API에 요청을 보낸다.
            ResponseEntity<HuggingFaceResponse[]> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    entity,
                    HuggingFaceResponse[].class);

            // 응답 상태 코드가 200(OK)이고, 응답 바디에 데이터가 있다면
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // response.getBody()는 배열이므로 첫 번째 응답 객체에서 generated_text 필드를 꺼내 반환
                return response.getBody()[0].getGenerated_text();
            }
        } catch (Exception e) {
            // API 호출 중 예외가 발생하면 콘솔에 스택 트레이스 출력
            e.printStackTrace();
        }

        // 예외가 발생하거나 정상적으로 응답을 받지 못한 경우 정해진 에러 메시지 반환
        return "죄송합니다. 답변을 생성하는데 문제가 발생했습니다. 고객센터(1234-5678)로 문의해주세요.";
    }
}
