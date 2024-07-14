package com.myapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString

public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //상품코드

    @Column(nullable = false, length = 50)
    private String itemNm; // 상품명

    @Column(nullable = false, name = "price")
    private int price; // 가격

    @Column(nullable = false)
    private int stockNumber; // 재고수량

    @Lob // (문자형 대용량 파일을 저장하는데 사용하는 데이터 타입), BLOB(이미지, 사운드, 비디오 같은 멀티미디어 데이터를 다룰 때 사용하는 데이터 타입) 타입을 매핑하기 위해서 사용한다.
    @Column(nullable = false)
    private String itemDetail; // 상품설명

    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    private LocalDateTime regDate; // 등록시간

    private LocalDateTime updateTime; // 수정시간
}
