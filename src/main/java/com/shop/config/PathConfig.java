package com.shop.config;
// 이 클래스가 속한 패키지 경로: 설정 관련 클래스들을 관리하는 "config" 패키지에 위치함.

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// 필요한 스프링 어노테이션과 기능들을 사용하기 위해 import.

@Configuration
// 이 클래스는 스프링의 설정 클래스임을 나타낸다.
// 스프링 컨테이너가 이 클래스를 설정 정보로 읽어와 빈(Bean)을 등록할 수 있게 한다.

public class PathConfig {
    // PathConfig 클래스: 파일 업로드 경로를 설정하는 스프링 설정 클래스

    @Bean
    // @Bean 어노테이션은 해당 메서드의 리턴 값을 스프링 컨테이너의 빈(Bean)으로 등록한다.
    // 등록된 빈은 애플리케이션 어디서든 주입(@Autowired) 받아 사용할 수 있다.

    public String uploadPath() {
        // 파일 업로드에 사용할 경로를 반환하는 메서드

        return System.getProperty("user.home") + "/uploads";
        /*
         System.getProperty("user.home"): 현재 사용자의 홈 디렉토리 경로를 가져온다.
         - 예시 (Windows): C:/Users/username
         - 예시 (Mac/Linux): /Users/username

         "/uploads": 홈 디렉토리 경로 뒤에 "uploads" 폴더를 추가한다.
         - 최종 반환값 예시: /Users/username/uploads
         - 이 경로는 파일 업로드 시 저장될 기본 위치로 사용된다.
        */
    }
}
