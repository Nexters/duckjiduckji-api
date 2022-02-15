package com.nexters.duckji.service;

import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mongodb.client.result.DeleteResult;
import com.nexters.duckji.api.ContentsResponseCombinator;
import com.nexters.duckji.domain.Content;
import com.nexters.duckji.dto.update.ContentUpdateRequest;
import com.nexters.duckji.mapstruct.ContentMapper;
import com.nexters.duckji.dto.ContentRegisterRequest;
import com.nexters.duckji.dto.ContentsApiParams;
import com.nexters.duckji.dto.ContentsResponse;
import com.nexters.duckji.dto.PageInfoParams;
import com.nexters.duckji.repository.ContentsRepository;

import reactor.core.publisher.Mono;

@Service
public class ContentsService extends ContentsResponseCombinator {
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

	public Mono<ContentsResponse> findAll(ContentsApiParams apiParams, PageInfoParams pageInfoParams) {
		int originLimit = pageInfoParams.getLimit();
		PageRequest pageRequest = pageInfoParams.pageRequest();

		return template.find(query(apiParams.criteria()).with(pageRequest), Content.class)
				.collectList()
				.flatMap(contents -> combine(originLimit).apply(contents, 0L));
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
