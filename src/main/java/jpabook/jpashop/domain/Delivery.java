package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    // 웬만하면 무조건 스트링으로 가즈
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deleveryStatus; // READY, COMP

}
