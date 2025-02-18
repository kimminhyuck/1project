package com.shop.controller;

import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CategoryController {

    private final ItemService itemService;

    public CategoryController(ItemService itemService) {
        this.itemService = itemService;
    }

    // 카테고리별 상품 조회 (HTML 페이지 렌더링)
    @GetMapping("/category/items")
    public String getCategoryItems(@RequestParam("category") String category, Pageable pageable, Model model) {
//            // 카테고리별 상품 조회
            Page<MainItemDto> items = itemService.getMainItemsByCategory(category, pageable);
            model.addAttribute("items", items);
            model.addAttribute("category", category);
        return "category";
    }

    @GetMapping("/category/items/ajax")
    @ResponseBody
    public Map<String, Object> getCategoryItemsAjax(@RequestParam("category") String category, Pageable pageable) {
        // 카테고리에 맞는 상품들을 MainItemDto로 가져오기
        Page<MainItemDto> items = itemService.getMainItemsByCategory(category, pageable);

        // Ajax 응답을 위해 Map에 담아서 반환
        Map<String, Object> response = new HashMap<>();
        response.put("content", items.getContent()); // 실제 아이템 리스트
        response.put("totalPages", items.getTotalPages());
        response.put("totalElements", items.getTotalElements());
        return response; // 클라이언트에서 content를 사용할 수 있도록 반환
    }

}
