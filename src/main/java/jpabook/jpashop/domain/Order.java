package jpabook.jpashop.domain;


import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// 엔티티에 대부분의 비즈니스 로직을 집어넣는 것을 도메인 모델 패턴이라고 한다.
// 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할.
// 반대로 엔티티에는 게터세터 밖에 없고 서비스에 대부분의 로직이 있는 경우 트랜잭션 스크립트 패턴 이라고 명명
@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 양방향 관계니까 연관관계의 주인을 정해줘야함.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // Cascade? orderItems에 JPA에 3개 저장한다고 가정하면 원래는 orderItems save하고 order도 save해줘야함.
    // Cascade 면 persist(order)만 해주면 끝.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 모든 Entity는 기본적으로 persist하려면 다 각각의 엔티티에 해줘야하는데 여기서는 캐스케이드 걸면 order에 cascade하면 다 저장.
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    protected Order(){

    }

    // 연관관계 메서드 //
    // 핵심 적으로 컨트롤 하는 쪽이 좋음. 양방향에서 원자적으로 해결.
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //== 주문 생성 메서도== //
    // 생성할 때부터 그냥 해버림// 완결
    // 복잡한 비즈니스 로직은 static으로 그냥
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem item: orderItems){
            order.addOrderItem(item);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //== 비즈니스 로직 //
    //주문 취소
    // 재고 올려줌

    public void cancel(){
        if(delivery.getDeleveryStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송 완료된 상품은 취소 불가능 ");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //== 조회 로직 == //
    public int getTotalPrice(){
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
