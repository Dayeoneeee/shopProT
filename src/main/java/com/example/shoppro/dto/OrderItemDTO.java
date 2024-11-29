package com.example.shoppro.dto;

import com.example.shoppro.entity.Item;
import com.example.shoppro.entity.Order;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderItemDTO {

    private Long id;        //현재 저장된 주문 아이템 pk

//    private OrderDTO orderDTO;        //OrderItem이 가지고 있는 orderDTO

//    private ItemDTO itemDTO;      // 다 가져올 필요는 없으니

    private String itemNm;      //그 중에 상품명만

    private int orderPrice;     //주문가격

    private int count;  //수량

    private String imgUrl;      //대표이미지만

}
