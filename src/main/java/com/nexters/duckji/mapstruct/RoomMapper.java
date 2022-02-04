package com.nexters.duckji.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.nexters.duckji.domain.Room;
import com.nexters.duckji.model.RoomRegisterRequest;

@Mapper
public interface RoomMapper {
	RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

	Room toEntity(RoomRegisterRequest registerRequest);
}
