package com.jinho.hellosecurity1.repository;

import com.jinho.hellosecurity1.model.Member;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JPA Repository가 들게 한다.
// @Repository라는 어노테이션이 없어도 IoC된다.
// JpaRepository를 상속했는데 이 안에 있음.
public interface MemberRepository extends JpaRepository<Member, Integer> {

    // JPA 쿼리 메서드
    // findBy 규칙 -> Username 문법
    // select * from user where username = 1?
    public Member findByUsername(String username);
    public Member findByEmail(String email);
}
