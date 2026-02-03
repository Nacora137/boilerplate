package com.example.boilerplate.domain.member.model;

import lombok.Builder;

@Builder
public record MemberModel(
        Long id,
        String email,
        String name) {
}
