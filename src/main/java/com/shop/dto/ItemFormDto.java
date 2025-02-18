package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명을 입력해주세요.")
    private String itemNm;
    @NotNull(message = "가격을 입력해주세요.")
    private Integer price;

    @NotBlank(message = "상세설명을 입력해주세요.")
    private String itemDetail;

    @NotNull(message = "판매수량을 입력해주세요.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private String category; //카테고리

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class); //Item 클래스에서 Item객체를 복사해서 Item객체를(인스턴스) 생성 하고 복사한 Item 객체를 반환해주는 메소드
    }

    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }
}
