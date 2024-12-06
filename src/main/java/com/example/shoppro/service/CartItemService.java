package com.example.shoppro.service;


import com.example.shoppro.dto.CartItemDTO;
import com.example.shoppro.entity.Cart;
import com.example.shoppro.entity.CartItem;
import com.example.shoppro.entity.Member;
import com.example.shoppro.repository.CartItemRepository;
import com.example.shoppro.repository.MemberRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    //카트에 담긴 카트 아이템의 수량을 변경
    //카트id는 필요가 없어요 //왜? 카트 아이템 id자식의 직접적인 pk값을 알고 있어서

    public Long updateCartItemId(CartItemDTO cartItemDTO, String email) throws Exception {
        //내 카트가 맞는지 확인 << email로 받게함
        Member member =
        memberRepository.findByEmail(email);
        //카트 아이디를 받아서 로그인한 회원정보와 비교 후에 작성한다.
        //카트 아이템 아이디만 있는 상태


        //컨트롤러에서 받은 CartItemDTO에 카트아이템아이디로

        Optional<CartItem> optionalCartItem =
        cartItemRepository.findById(cartItemDTO.getItemid());

        CartItem cartItem =
        optionalCartItem.orElseThrow(EntityNotFoundException::new);



//       if(cartItem.getCart()!=null && member !=null    && cartItem.getCart().getMember().getId() != member.getId() ){
        if(cartItem.getCart()!=null && member !=null    && cartItem.getCart().getMember().getId() != member.getId() ){
            throw new Exception();
        }


        cartItem.setCount(cartItemDTO.getCount());

        return cartItem.getId();

    }

}
