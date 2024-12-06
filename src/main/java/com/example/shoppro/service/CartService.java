package com.example.shoppro.service;

import com.example.shoppro.dto.CartDetailDTO;
import com.example.shoppro.dto.CartItemDTO;
import com.example.shoppro.dto.CartOrderDTO;
import com.example.shoppro.dto.OrderDTO;
import com.example.shoppro.entity.Cart;
import com.example.shoppro.entity.CartItem;
import com.example.shoppro.entity.Item;
import com.example.shoppro.entity.Member;
import com.example.shoppro.repository.CartItemRepository;
import com.example.shoppro.repository.CartRepository;
import com.example.shoppro.repository.ItemRepository;
import com.example.shoppro.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final OrderService orderService;

    private final ItemRepository itemRepository;
    //아이템을 찾아야해서 findById (item.id)

    private final MemberRepository memberRepository;
    //누구의 장바구니인지 찾아야해서

    private final CartRepository cartRepository;
    //장바구니가 있는지 없는지 장바구니도 만들고 등등 하기위해서

    private final CartItemRepository cartItemRepository;
    //장바구니에 넣을 장바구니 아이템을 만들려면 아이템을 참조해서
    //그걸 가지고 장바구니 아이템을 만들어야해서 그리고 등등등...

    //등록. 장바구니 만들기
    //장바구니를 따로 만들지는 않고 장바구니에 넣을 아이템들이 컨트롤러로 들어오면
    //그 값을 가지고 넣을 것이고 컨트롤러에서 들어오는 email을 통해서 멤버를 찾을 예정
    public Long addCart(CartItemDTO cartItemDTO, String email){
        log.info("장바구니 서비스로 들어온 email " + email);
        log.info("장바구니 서비스로 들어온 cartItemDTO " + cartItemDTO);


        //회원찾기
        Member member = memberRepository.findByEmail(email);

        log.info("장바구니 서비스로 들어온 member " + member);


        //사용자가 산다고 해서 장바구니에 넣는다고 한 장바구니 아이템이
        //없는 아이템이면? 아이템이 잇긴한가?
        Item item = itemRepository.findById(  cartItemDTO.getItemid() )
                .orElseThrow(EntityNotFoundException::new);

        log.info("장바구니 서비스로 들어온 item " + item);

        Cart cart = cartRepository.findByMemberId( member.getId() );
        if (cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }
        //장바구니가 없으면 만들고 있으면 있는걸로
        // 결과 : 장바구니 아이템들을 만들어서 넣어주고 저장한다.
        //이미 장바구니에 동일 상품이 등록되어 있다면, 해당 등록된 아이템의 수량이 증가되어야 한다.
        CartItem savedCartItem =
                cartItemRepository.findByCartIdAndItemId(  cart.getId() , item.getId()  );

        //장바구니에 이미 있다면
        if (savedCartItem != null){
            //수량 증가
            savedCartItem.addCount(cartItemDTO.getCount());
            //저장된 장바구니에서 장바구니 아이템 pk를 반환
            return savedCartItem.getId();

        }else {
            CartItem cartItem =
                    CartItem.createCartItem(cart, item, cartItemDTO.getCount());

            //장바구니에 장바구니 아이템 저장
            cartItemRepository.save(cartItem);

            return cartItem.getId();

        }

    }



    public List<CartDetailDTO> getCartList(String email){
        //장바구니의 pk는 1:1 관계이기 때문에 그리고 email은 member테이블에서
        //유니크키이기에 유일하다. 멤버는 1, 그에 관계의 장바구니도 1

        List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();

        Member member =
                memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());
//        Cart cart = cartRepository.findByMemberEmail(email);


        if (cart == null) {
            //카트가 존재하지 않는다면

            log.info("여기니??");
            log.info("여기니??");
            log.info("여기니??");
            log.info("여기니??");

            return cartDetailDTOList;
        }

        //장바구니에 담겨잇는 상품 정보를 조회
        cartDetailDTOList = cartItemRepository.findCartDetailDTOList(cart.getId());

        log.info(cartDetailDTOList);
        log.info(cartDetailDTOList);
        log.info(cartDetailDTOList);
        log.info(cartDetailDTOList);
        log.info(cartDetailDTOList);

        return cartDetailDTOList;

    }

    public boolean validateCartItem(Long cartItemid, String email){

        Member member =
                memberRepository.findByEmail(email);

        CartItem cartItem =
                cartItemRepository.findById(cartItemid).orElseThrow(EntityNotFoundException::new);

        if (member != null && cartItem != null){

            if ( !member.getEmail().equals( cartItem.getCart().getMember().getEmail() ) ) {
                return false;
            }
        }

        return true;

    }

    public void deleteCartItem(Long cartItemid){

        CartItem cartItem =
        cartItemRepository.findById(cartItemid).orElseThrow(EntityNotFoundException::new);

        cartItemRepository.delete(cartItem);



    }

    //장바구니에서 들어온 주문

    public Long orderCartItem(List<CartOrderDTO> cartOrderDTOList, String email){
        //cartOrderDTOList cartItemId 들어있음

        List<OrderDTO> orderDTOList = new ArrayList<>();

        for (CartOrderDTO cartOrderDTO : cartOrderDTOList){
            //cartItemId를 하나씩 가지고

            CartItem cartItem =
                    cartItemRepository.findById(cartOrderDTO.getCartItemId())
                            .orElseThrow(EntityNotFoundException::new);


            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setItemId(cartItem.getItem().getId());
            orderDTO.setCount(cartItem.getCount());

            orderDTOList.add(orderDTO);

        }
        Long orderid =

        orderService.orders(orderDTOList, email); //장바구니 아이템들 저장.

        for ( CartOrderDTO cartOrderDTO : cartOrderDTOList){
            CartItem cartItem =
            cartItemRepository.findById(cartOrderDTO.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            cartItemRepository.delete(cartItem);
        }

        return orderid;
    }







}
