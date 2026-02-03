package com.example.boilerplate.domain.member.mapper;

import com.example.boilerplate.domain.member.entity.Member;
import com.example.boilerplate.domain.member.model.MemberModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    // Model -> Entity
    Member toEntity(MemberModel model);

    // Entity -> Model
    MemberModel toModel(Member entity);
}
