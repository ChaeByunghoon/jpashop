package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch){
        // 동적 쿼리를 어케 써버릴까... 뭐 값이 없다면 어떻게 어떻게 해야하는데

        return em.createQuery("select o from Order o join o.member m where o.status = :status and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
//                .setFirstResult(100) 페이징
//                .setMaxResults()
                .getResultList();
    }

    /**
     * JPA Criteria
     * @return
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch){
        // 빌드하고 나면 되긴 함 JPQL을 조립해주는 역할
        // Criteria 동적 쿼리 작성할 때 유리함 근데;;;.....
        // 치명적인 단점? 무슨 쿼리가 생성될지? 코드 readability가 너무 떨어짐.
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();
        if (orderSearch.getOrderStatus() != null) {
            Predicate predicate = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(predicate);
        }
        if (orderSearch.getMemberName() != null){
            Predicate predicate = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(predicate);
        }

        cq.where(criteria.toArray(new Predicate[criteria.size()]));
        return em.createQuery(cq).setMaxResults(10000).getResultList();
    }

    /**
     * 동적 쿼리를 쉽게 해보자 QueryDSL!!!
    **/
    public List<Order> findAllByQueryDSL(OrderSearch orderSearch){
        return new ArrayList<Order>();
    }

}
