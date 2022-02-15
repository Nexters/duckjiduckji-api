package com.nexters.duckji.api;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.result.DeleteResult;
import com.nexters.duckji.domain.Content;
import com.nexters.duckji.dto.ApiResponse;
import com.nexters.duckji.dto.ContentRegisterRequest;
import com.nexters.duckji.dto.update.ContentUpdateRequest;
import com.nexters.duckji.dto.ContentsApiParams;
import com.nexters.duckji.dto.ContentsResponse;
import com.nexters.duckji.dto.PageInfoParams;
import com.nexters.duckji.service.ContentsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

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

	@ApiOperation("컨텐츠 수정 / replace")
	@PutMapping("/{contentId}")
	public Mono<ApiResponse<Content>> replace(@PathVariable String contentId,
			@RequestBody @Valid ContentUpdateRequest contentUpdateRequest) {
		return contentsService.replaceById(contentUpdateRequest, contentId)
				.map(ApiResponse::create)
				.switchIfEmpty(Mono.just(ApiResponse.empty()));
	}

	@ApiOperation("컨텐츠 삭제")
	@DeleteMapping("/{contentId}")
	public Mono<ApiResponse<DeleteResult>> replace(@PathVariable String contentId, @RequestParam String roomId) {
		return contentsService.deleteById(contentId, roomId)
				.map(ApiResponse::create)
				.switchIfEmpty(Mono.just(ApiResponse.empty()));
	}

	@ApiOperation("컨텐츠 단건 조회")
	@GetMapping("/{contentId}")
	public Mono<ApiResponse<Content>> getById(@PathVariable String contentId) {
		return contentsService.findById(contentId)
				.map(ApiResponse::create)
				.switchIfEmpty(Mono.just(ApiResponse.empty()));
	}

	@ApiOperation("컨텐츠 리스트 조회")
	@GetMapping
	public Mono<ApiResponse<ContentsResponse>> getContents(@Valid ContentsApiParams params, @Valid PageInfoParams pageInfoParams) {
		return contentsService.findAll(params, pageInfoParams)
				.map(ApiResponse::create)
				.switchIfEmpty(Mono.just(ApiResponse.empty()));
	}
}
