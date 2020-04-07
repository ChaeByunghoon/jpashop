package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문

    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member, delivery, orderItem);
        orderRepository.save(order);
        // 원래 는 delivery OrderItem 이런거 다 설정 해줘야함 리포지토리에 그런데
        // CasCade 옵션을 사용해서
        // 참조 주인관계에만 쓰는게 좋음 여기서는 persis 라이프 사이클 자체가 괜찮아서..
        // 라이프 사이클을 생각해보자
        return order.getId();
    }
    // 취소
    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findOne(orderId);
        // cancel로 하면 업데이트를 때려야햠.
        // 더티 체킹을 통해서 알아서 업데이트 쿼리가 날라감 .
        order.cancel();
    }

//    public List<Order> findOrders(OrderSearch orderSearch){
//
//    }
}
