package com.example.shoppro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.aspectj.bridge.Message;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CartItemDTO {

    @NotNull(message = "상품 아이디는 필수 입력 값 입니다.")
    private Long itemid;

    @Min(value = 1, message = "최소수량은 1개 입니다.")
    private int count;
}
