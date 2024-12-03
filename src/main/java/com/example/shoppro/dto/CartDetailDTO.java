package com.example.shoppro.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailDTO {

    private Long cartItemid;    //장바구니 상품 아이디

    private String itemNm;      //상품명

    private int price;          //상품금액

    private int count;          //수량

    private String imgUrl;      //상품 이미지 경로

    //생성자 >> @AllArgsConstructor 사용했기 때문에 작성x
//    public CartDetailDTO (Long cartItemId, String itemNm,
//                          int price, int count, String imgUrl){
//
//        this.cartItemid = cartItemId;
//        this.itemNm = itemNm;
//        this.price = price;
//        this.imgUrl = imgUrl;
//
//    }
}
