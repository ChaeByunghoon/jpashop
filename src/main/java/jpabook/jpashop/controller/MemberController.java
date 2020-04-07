package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        //모델은
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    // Thymeleaf with spring boot 이거 참고해
    // 야 근데 멤버 엔티티를 안넣고 멤버 폼을 넣냐? => 좀 안맞음; Address같은 경우도 그렇고 Validation 어케할꺼?
    // 그니까 컨트롤러에서 넘어오는 validation과 Entity가 요구하는 validation이 안맞는 경우는
    // 차라리 화면에 Fit한 폼 객체를 만들어 버리자.
    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result){

        if (result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);
        memberService.join(member);
        return "redirect:/";
    }

    // API 쓸 때는 Entity 절때 쓰지마라 = > DTO를 만들즌지 해라
    // 웬만하면 DTO를 써라 새끼야.
    @GetMapping("/members/list")
    public String list(Model model){
        List<Member> members = memberService.findMembers();

        model.addAttribute("members", members);
        return "members/list";
    }
}
