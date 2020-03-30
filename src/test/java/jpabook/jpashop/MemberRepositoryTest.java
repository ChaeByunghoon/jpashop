package jpabook.jpashop;

import jpabook.jpashop.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    void testMember() {
        //Given
        Member member = new Member();
        member.setUsername("Hello");

        //When
        Long memberId = memberRepository.save(member);
        Member member1 = memberRepository.find(memberId);

        //Then
        Assertions.assertThat(member.getId()).isEqualTo(member1.getId());




    }


}