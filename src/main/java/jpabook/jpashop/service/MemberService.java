package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true )
@RequiredArgsConstructor
public class MemberService {

    // 이런식으로하면 못바꿈.
    //@Autowired
    // 변경할 일이 없다. 따라서 Final로 .
    private final MemberRepository memberRepository;

    // 회원 가입.
    @Transactional
    public Long join(Member member) {
        // 여기 뭐 validation 코드를 넣거나 하면 됌.
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 디비 여러개는 걸릴 수 있음.
        // 실무에서는 DB 유니크 제약 조건 걸어주자.
        List<Member> members = memberRepository.findByName(member.getName());
        if (!members.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원이다.");
        }
    }

    // 회원 전체 조회
    // readonly 주면 성능 향상.
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    // readonly 주면 성능 향상.
    @Transactional(readOnly = true)
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }

}
