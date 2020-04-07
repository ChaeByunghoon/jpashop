package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {


    @Autowired
    OrderService orderService;

    @Autowired
    EntityManager manager;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{

        Member member = createMember();
        Item item = createItem("hoonki", 10000, 10);
        manager.persist(item);
        // when
        Long id = orderService.order(member.getId(), item.getId(), 2);

        //then
        Order order = orderRepository.findOne(id);

        assertEquals(OrderStatus.ORDER, order.getStatus());
        assertEquals(1, order.getOrderItems().size());
        assertEquals(10000 * 2, order.getTotalPrice());
        assertEquals(8, item.getStockQuantity());

    }

    @Test
    public void 상품주문_재고수량초과() throws Exception{
        // given
        Member member = createMember();
        Item item = createItem("hoonki", 10000, 10);
        manager.persist(member);
        manager.persist(item);

        // when
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), 11);
        });

        // then
//        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("훈키");
        member.setAddress(new Address("qwer", "qwer,", "qwer"));
        manager.persist(member);
        return member;
    }

    private Item createItem(String name, int price, int stockQuantity) {
        Item item = new Book();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        return item;
    }

    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = createMember();
        Item item = createItem("hoonki", 1000, 10);
        manager.persist(member);
        manager.persist(item);
        Long orderId = orderService.order(member.getId(), item.getId(), 5);
        // when

        orderService.cancelOrder(orderId);

        // then

        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, order.getStatus());
        assertEquals(10, item.getStockQuantity());

    }

}