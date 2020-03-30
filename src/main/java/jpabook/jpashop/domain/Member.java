package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Entity 설계시 주의점
// Entity에서는 Setter를 사용하지 말자
// TODO 모든 연관관계는 지연 로딩으로 설정 (외우자)
// 즉시 로딩 : 멤버를 조회하면 그에 따른 오더나 이런 것들을 같이 조회해버림 -> 예측이 어렵고 어떤 SQL이 실행될지 몰라.
// 지연 로딩 (Lazy Loading) : 그때 그때 로딩
// X to one, 은 기본 전략 자체가 EAGER

@Entity
@Getter @Setter
// 만약에 테이블 이름 지정을 안하면 스프링 부트에서는 SpringPhysical 어쩌구 씀
// 자바의 카멜케이스 => 언더바로 바꿈
// 대문자 => 소문자
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    // 내장타입으로 쓰겠다.
    @Embedded
    private Address address;

    // 나는 오더 테이블에 의한 멤버 필드를 참조해.
    // 무조건 new ArrayList(); 초기화에서 한다.
    // 어떤 문제가 있냐면 Hibernate가 Entity를 Persist한 순간 걔네가 제공하는 컬렉션으로 변경?

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();


}
