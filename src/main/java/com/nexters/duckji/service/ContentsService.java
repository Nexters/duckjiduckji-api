package com.nexters.duckji.service;

import static org.springframework.data.mongodb.core.query.Query.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import com.nexters.duckji.api.ContentsResponseCombinator;
import com.nexters.duckji.domain.Content;
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
}
