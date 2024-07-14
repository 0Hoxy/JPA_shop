package com.myapp.repository;

import com.myapp.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
//QuerydslPredicateExecutor<E>는 상속을 추가해야 한다.
public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

    List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    //@Query문을 이용
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
        // :itemDetail은 itemDetail의 매개변수를 나타내고, 메소드에 전달된 itemDetail 변수의 값이 이 위치에 들어간다
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
    //@Param을 이용하여 파라미터로 넘어온 값을 JPQL에 들어갈 변수로 지정해줄 수 있다.

    //@QueryNative를 이용
    @Query(value = "select * from Item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
}
