package com.nexters.duckji.service;

import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;

import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mongodb.client.result.DeleteResult;
import com.nexters.duckji.api.ResponseCombinator;
import com.nexters.duckji.domain.Content;
import com.nexters.duckji.dto.update.ContentUpdateRequest;
import com.nexters.duckji.dto.update.LocationUpdateRequest;
import com.nexters.duckji.mapstruct.ContentMapper;
import com.nexters.duckji.dto.ContentRegisterRequest;
import com.nexters.duckji.dto.params.ContentsApiParams;
import com.nexters.duckji.dto.ListResponse;
import com.nexters.duckji.dto.PageInfoParams;
import com.nexters.duckji.repository.ContentsRepository;

import reactor.core.publisher.Mono;

@Service
public class ContentsService extends ResponseCombinator<Content> {
	private final ContentsRepository contentsRepository;
	private final ReactiveMongoTemplate template;

	public ContentsService(ContentsRepository contentsRepository,
			ReactiveMongoTemplate template) {
		this.contentsRepository = contentsRepository;
		this.template = template;
	}

	public Mono<Content> register(ContentRegisterRequest registerRequest) {
		return Mono.just(registerRequest)
				.map(ContentMapper.INSTANCE::toEntity)
				.flatMap(contentsRepository::save);
	}

	public Mono<Content> findById(String contentId) {
		return contentsRepository.findById(contentId);
	}

	public Mono<ListResponse<Content>> findAll(ContentsApiParams apiParams, PageInfoParams pageInfoParams) {
		int originLimit = pageInfoParams.getLimit();
		PageRequest pageRequest = pageInfoParams.pageRequest();

		return template.find(query(apiParams.criteria()).with(pageRequest), Content.class)
				.collectList()
				.flatMap(contents -> combine(originLimit).apply(contents, 0L));
	}

	public Mono<Content> drag(LocationUpdateRequest updateRequest, String contentId) {
		String roomId = updateRequest.getRoomId();
		Criteria criteria = where("_id").is(contentId).and("roomId").is(roomId);

		return template.update(Content.class)
				.matching(query(criteria))
				.apply(updateRequest.getUpdate()
						.set("edtAt", LocalDateTime.now())
				)
				.withOptions(FindAndModifyOptions.options().returnNew(true))
				.findAndModify();
	}

	public Mono<Content> replaceById(ContentUpdateRequest updateRequest, String contentId) {
		String roomId = updateRequest.getRoomId();

		return contentsRepository.findById(contentId)
				.filter(content -> content.validRoomId(roomId))
				.map(content -> ContentMapper.INSTANCE.updateContent(updateRequest, content))
				.flatMap(contentsRepository::save)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
	}

	public Mono<DeleteResult> deleteById(String contentId, String roomId) {
		return template.remove(query(where("_id").is(contentId).and("roomId").is(roomId)), Content.class);
	}
}
