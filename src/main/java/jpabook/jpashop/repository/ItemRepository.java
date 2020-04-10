package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        if (item.getId() == null){
            // 신규 등록 로직
            em.persist(item);
        }else{
            // merge 는 Update 로직.
            // 준 영속상태의 엔티티를 영속상태로 변경 하는 역할
            // 근데 병합을 사용해버리면 모든 속성ㅇ ㅣ다 변경됨.
            // 만약에 병합시 값이 없으면 다 null로 변경됨.
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
