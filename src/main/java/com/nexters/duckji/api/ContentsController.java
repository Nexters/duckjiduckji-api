package com.nexters.duckji.api;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexters.duckji.domain.Content;
import com.nexters.duckji.model.ApiResponse;
import com.nexters.duckji.model.ContentRegisterRequest;
import com.nexters.duckji.service.ContentsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

/**
 * Copyright 2021 NAVER Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @author smean.lee on 22. 2. 4..
 */
@Api(tags = "Contents")
@RestController
@RequestMapping("/contents")
public class ContentsController {
	private final ContentsService contentsService;

	public ContentsController(ContentsService contentsService) {
		this.contentsService = contentsService;
	}

	@ApiOperation("컨텐츠 등록")
	@PostMapping
	public Mono<ApiResponse<Content>> register(@RequestBody @Valid ContentRegisterRequest contentRegisterRequest) {
		return contentsService.register(contentRegisterRequest)
				.map(ApiResponse::create)
				.switchIfEmpty(Mono.just(ApiResponse.empty()));
	}
}
