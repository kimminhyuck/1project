package com.shop.controller;


import com.shop.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;
    // 챗봇 서비스 로직을 처리하는 ChatbotService 주입을 받는다.
    // final로 선언했으므로 생성자를 통해 주입받는다.

    @Autowired
    // 스프링의 의존성 주입(Dependency Injection)을 통해 ChatbotService 인스턴스를 주입받는다.
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/ask")
    // 클라이언트가 "/ask" 경로로 POST 요청을 보내면 이 메서드가 호출된다.
    // 예를 들어, AJAX 요청이나 HTML form, 또는 다른 클라이언트에서 POST 요청을 보내면 이 메서드가 응답한다.
    public String askChatbot(@RequestParam String question) {
        // @RequestParam을 사용해 클라이언트가 전달한 "question" 파라미터 값을 메서드 인자로 받는다.
        // question: 사용자가 묻는 질문 문자열

        // chatbotService의 getChatbotResponse() 메서드를 호출해 question에 대한 응답을 얻는다.
        return chatbotService.getChatbotResponse(question);
        // 서비스로부터 받은 응답 문자열을 그대로 반환한다.
        // 이 반환값은 클라이언트에게 HTTP 응답 바디로 전달된다.
    }
}


