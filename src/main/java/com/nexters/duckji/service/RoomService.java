package com.nexters.duckji.service;

import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.client.result.DeleteResult;
import com.nexters.duckji.api.ResponseCombinator;
import com.nexters.duckji.domain.Room;
import com.nexters.duckji.dto.ListResponse;
import com.nexters.duckji.dto.PageInfoParams;
import com.nexters.duckji.dto.RoomRegisterRequest;
import com.nexters.duckji.dto.update.RoomConfigUpdateRequest;
import com.nexters.duckji.mapstruct.RoomMapper;
import com.nexters.duckji.repository.RoomRepository;
import com.nexters.duckji.util.JsonUtils;
import com.nexters.duckji.util.MongoUtils;

import reactor.core.publisher.Mono;

@Service
public class RoomService extends ResponseCombinator<Room> {
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

	public Mono<ListResponse<Room>> findAll(PageInfoParams pageInfoParams) {
		int originLimit = pageInfoParams.getLimit();
		PageRequest pageRequest = pageInfoParams.pageRequest();

		return template.find(query(new Criteria()).with(pageRequest), Room.class)
				.collectList()
				.flatMap(rooms -> combine(originLimit).apply(rooms, 0L));
	}

	public Mono<Room> patchConfigById(RoomConfigUpdateRequest updateRequest, String roomId) {
		Criteria criteria = where("_id").is(roomId);
		FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
		Map<String, Object> partial = JsonUtils.convert(updateRequest, new TypeReference<>(){});

		return Mono.just(MongoUtils.toUpdate(partial))
				.map(update -> update.set("edtAt", LocalDateTime.now()))
				.flatMap(update -> template.update(Room.class)
						.matching(query(criteria))
						.apply(update)
						.withOptions(options)
						.findAndModify()
				);
	}

	public Mono<DeleteResult> deleteById(String roomId) {
		return template.remove(query(where("_id").is(roomId)), Room.class);
	}
}
