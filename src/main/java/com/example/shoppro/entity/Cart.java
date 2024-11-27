package com.example.shoppro.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString //(exclude = "itemImgList")  //toString 변수 제외할 변수명
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cart")
public class Cart {

    @Id
    @Column(name = "cart")       //테이블에서 매핑될 컬럼
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne   //회원 엔티티와 일대일로 매핑
    @JoinColumn(name = "member_id") //매핑할 외래키 지정
    private Member member;

    //name 속성에는 매핑할 외래키의 이름을 설정합니다.
    //설정하지 않으면 JPA가 알아서 id를 찾지만 컬럼명이 원하는대로
    //생성되지 않을 수 있기 때문

}
