package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerServiceController {

    @GetMapping("/customer-service")
    public String customerService() {
        return "redirect:/customer-service/index.html";
    }
}
