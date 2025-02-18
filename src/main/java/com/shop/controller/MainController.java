package com.shop.controller;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.CommentService;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;
    private final CommentService commentService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        // 각 아이템의 평균 별점을 계산하여 모델에 추가
        items.getContent().forEach(item -> {
            Double averageStar = commentService.getAverageStar(item.getId());
            item.setAverageStar(averageStar); // MainItemDto에 평균 별점 추가
        });

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return "main";
    }
    private Double calculateAverageStar(Long itemId) {
        return commentService.getAverageStar(itemId);
    }

}
