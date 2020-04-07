package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
// 상속관계 전략을 잡아야함 여러 테이블 or Single Table
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //** 비즈니스 로직 == //
    // 재고 증가 시키는 로직.
    public void addStock(int quantity){
        stockQuantity += quantity;
    }


    //핵심 비즈니스 로직은 도메인에 있어야함.
    public void removeStock(int quantity){
        int realStock = this.stockQuantity - quantity;
        if (realStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = realStock;
    }

}
