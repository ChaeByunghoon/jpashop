package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;

    @Transactional
    public void saveItem(Item item){
        repository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        // 더티 체킹으로 알아서 save
        Item item = repository.findOne(itemId);
        item.setPrice(price);
        item.setName(name);
        item.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems(){
        return repository.findAll();
    }

    public Item findOne(Long id){
        return repository.findOne(id);
    }
}
