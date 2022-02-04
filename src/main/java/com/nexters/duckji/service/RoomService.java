package com.nexters.duckji.service;

import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.DeleteResult;
import com.nexters.duckji.domain.Room;
import com.nexters.duckji.model.RoomRegisterRequest;
import com.nexters.duckji.mapstruct.RoomMapper;
import com.nexters.duckji.repository.RoomRepository;

import reactor.core.publisher.Mono;

@Service
public class RoomService {
	private final RoomRepository roomRepository;
	private final ReactiveMongoTemplate template;

	public RoomService(RoomRepository roomRepository,
			ReactiveMongoTemplate template) {
		this.roomRepository = roomRepository;
		this.template = template;
	}

	public Mono<Room> register(RoomRegisterRequest roomRegisterRequest) {
		return Mono.just(roomRegisterRequest)
				.map(RoomMapper.INSTANCE::toEntity)
				.flatMap(roomRepository::save);
	}

	public Mono<Room> findById(String roomId) {
		return roomRepository.findById(roomId);
	}

	public Mono<DeleteResult> deleteById(String roomId) {
		return template.remove(query(where("_id").is(roomId)), Room.class);
	}
}
