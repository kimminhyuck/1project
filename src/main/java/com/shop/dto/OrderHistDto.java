package com.shop.dto;

import com.shop.constant.OrderStatus;
import com.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {
    public OrderHistDto(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
        this.memberName = order.getMember().getName();
        this.memberEmail = order.getMember().getEmail();
    }

    private Long orderId; //주문 아이디
    private String orderDate; //주문 날짜
    private OrderStatus orderStatus; //주문 상태
    private String memberName;
    private String memberEmail;
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }

    // 대시보드용 메서드 추가
    public String getRepresentativeItemName() {
        if (orderItemDtoList.isEmpty()) return "";
        return orderItemDtoList.get(0).getItemNm();
    }

    public int getTotalPrice() {
        return orderItemDtoList.stream()
                .mapToInt(item -> item.getOrderPrice() * item.getCount())
                .sum();
    }
}