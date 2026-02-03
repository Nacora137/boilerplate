package com.example.boilerplate.web.member.controller;

import com.example.boilerplate.common.dto.ApiResponse;
import com.example.boilerplate.web.member.dto.MemberRequest;
import com.example.boilerplate.web.member.dto.MemberResponse;
import com.example.boilerplate.web.member.service.MemberWebService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "Member Management API")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberWebService memberWebService;

    @Operation(summary = "Create a new member", description = "Creates a new member with the given email and name.")
    @PostMapping
    public ApiResponse<MemberResponse> createMember(@RequestBody MemberRequest request) {
        return ApiResponse.success(memberWebService.createMember(request));
    }

    @Operation(summary = "Get member by ID", description = "Retrieves member details using the member ID.")
    @GetMapping("/{id}")
    public ApiResponse<MemberResponse> getMember(@PathVariable Long id) {
        return ApiResponse.success(memberWebService.getMember(id));
    }
}
