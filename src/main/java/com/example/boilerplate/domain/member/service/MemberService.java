package com.example.boilerplate.domain.member.service;

import com.example.boilerplate.common.exception.BusinessException;
import com.example.boilerplate.common.exception.ErrorCode;

import com.example.boilerplate.domain.member.entity.Member;
import com.example.boilerplate.domain.member.mapper.MemberMapper;
import com.example.boilerplate.domain.member.model.MemberModel;
import com.example.boilerplate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Transactional
    public MemberModel createMember(MemberModel model) {
        if (memberRepository.existsByEmail(model.email())) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }
        Member member = memberMapper.toEntity(model);
        Member savedMember = memberRepository.save(member);
        return memberMapper.toModel(savedMember);
    }

    public MemberModel getMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return memberMapper.toModel(member);
    }
}
