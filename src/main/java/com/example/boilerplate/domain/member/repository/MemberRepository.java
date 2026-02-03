package com.example.boilerplate.domain.member.repository;

import com.example.boilerplate.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
