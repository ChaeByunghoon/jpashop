package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form){

        Book book = Book.createInstanceByForm(form);
        service.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String bookList(Model model){

        List<Item> items = service.findItems();
        model.addAttribute("items", items);
        return "items/list";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book book = (Book)service.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(book.getId());
        form.setName(book.getName());
        form.setPrice(book.getPrice());
        form.setStockQuantity(book.getStockQuantity());
        form.setAuthor(book.getAuthor());
        form.setIsbn(book.getIsbn());

        model.addAttribute("form", form);
        return "items/update";
    }

    @PostMapping("items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form){

//        Book book = Book.createInstanceByForm(form);
        // 아이디가 셋팅? JPA에 한번 들어갔다온놈? => 준영속 상태의 객체라 명칭.
        // 즉 JPA가 식별할 수 있는 엔티티 JPA가 관리하는 놈이 아님. 더티 체킹이 안해.
//        book.setId(form.getId());

        // save Item을 하면 repository에서 save 감
        // 만약 아이디가 있으면 merge를 해버림

        //준영속 엔티티
        //영속성 컨텍스트가 더는 관리하지 않는 엔티티를 말함.
        // 준영속 엔티티를 수정하는 2가지 방법
        // 1) 더티 체킹
        // 2) 병합.
//        service.saveItem(book);

        //어설프게 엔티티를 생성하지 말자.
        // 서비스 계층에 아이디, 변경하고자 하는 파라미터를 명확하게 전달.
        service.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}
