package com.nexters.duckji.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.nexters.duckji.domain.Content;
import com.nexters.duckji.dto.ContentRegisterRequest;
import com.nexters.duckji.dto.update.ContentUpdateRequest;
import com.nexters.duckji.dto.PolaroidRegisterRequest;
import com.nexters.duckji.dto.update.PolaroidUpdateRequest;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ContentMapper {
	public static final ContentMapper INSTANCE = Mappers.getMapper(ContentMapper.class);

	public Content toEntity(ContentRegisterRequest registerRequest) {
		//if (registerRequest instanceof PolaroidRegisterRequest) {
		//
		//}
		return toEntity((PolaroidRegisterRequest) registerRequest);
	}

	public Content updateContent(ContentUpdateRequest updateRequest, @MappingTarget Content content) {
		//if (registerRequest instanceof PolaroidRegisterRequest) {
		//
		//}
		return updateContent((PolaroidUpdateRequest) updateRequest, content);

	}

	abstract Content updateContent(PolaroidUpdateRequest updateRequest, @MappingTarget Content content);

	abstract Content toEntity(PolaroidRegisterRequest registerRequest);
}
