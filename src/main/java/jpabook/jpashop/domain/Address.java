package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

// 값 타입은 Immutable하게 설계되어야함
// 생성할 때만 값이 생성되어야하고, setter로 제공 X
@Getter
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // JPA 제약 리플랙션
    // 그래서 일단 만들긴 하는데 protected로...
    protected Address(){

    }

    // 생성자에서 값 초기화하고 셋터에서는 못하게
    public Address(String city, String street, String zipcode){
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }



}
