package com.example.boilerplate.web.member.service;

import com.example.boilerplate.web.member.dto.MemberRequest;
import com.example.boilerplate.web.member.dto.MemberResponse;
import com.example.boilerplate.domain.member.model.MemberModel;
import com.example.boilerplate.domain.member.service.MemberService;
import com.example.boilerplate.web.member.mapper.MemberWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberWebService {

    private final MemberService memberService;
    private final MemberWebMapper memberMapper;

    public MemberResponse createMember(MemberRequest request) {
        // Request -> Model
        MemberModel model = memberMapper.toModel(request);

        // Business Logic
        MemberModel savedModel = memberService.createMember(model);

        // Model -> Response
        return memberMapper.toResponse(savedModel);
    }

    public MemberResponse getMember(Long id) {
        // Business Logic
        MemberModel model = memberService.getMember(id);

        // Model -> Response
        return memberMapper.toResponse(model);
    }
}
