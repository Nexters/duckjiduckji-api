package com.nexters.duckji.service;

import org.springframework.stereotype.Service;

import com.nexters.duckji.domain.Room;
import com.nexters.duckji.model.RoomRegisterRequest;
import com.nexters.duckji.mapstruct.RoomMapper;
import com.nexters.duckji.repository.RoomRepository;

import reactor.core.publisher.Mono;

@Service
public class RoomService {
	private final RoomRepository roomRepository;

	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	public Mono<Room> register(RoomRegisterRequest roomRegisterRequest) {
		return Mono.just(roomRegisterRequest)
				.map(RoomMapper.INSTANCE::toEntity)
				.flatMap(roomRepository::save);
	}
}
