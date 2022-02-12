package com.nexters.duckji.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.nexters.duckji.domain.Content;
import com.nexters.duckji.model.ContentRegisterRequest;
import com.nexters.duckji.model.PolaroidRegisterRequest;

@Mapper
public interface ContentMapper {
	ContentMapper INSTANCE = Mappers.getMapper(ContentMapper.class);

	default Content toEntity(ContentRegisterRequest registerRequest) {
		//if (registerRequest instanceof PolaroidRegisterRequest) {
		//
		//}
		return toEntity((PolaroidRegisterRequest) registerRequest);
	}

	Content toEntity(PolaroidRegisterRequest registerRequest);
}
