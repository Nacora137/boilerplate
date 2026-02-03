package com.example.boilerplate.web.member.mapper;

import com.example.boilerplate.domain.member.model.MemberModel;
import com.example.boilerplate.web.member.dto.MemberRequest;
import com.example.boilerplate.web.member.dto.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberWebMapper {

    MemberWebMapper INSTANCE = Mappers.getMapper(MemberWebMapper.class);

    // Request -> Model
    @Mapping(target = "id", ignore = true)
    MemberModel toModel(MemberRequest request);

    // Model -> Response
    MemberResponse toResponse(MemberModel model);
}
