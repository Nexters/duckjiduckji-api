package com.nexters.duckji.api;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexters.duckji.domain.Room;
import com.nexters.duckji.dto.ApiResponse;
import com.nexters.duckji.dto.RoomRegisterRequest;
import com.nexters.duckji.service.RoomService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@Api(tags = "Room")
@RestController
@RequestMapping("/rooms")
public class RoomController {
	private final RoomService roomService;

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@ApiOperation("방 등록")
	@PostMapping
	public Mono<ApiResponse<Room>> register(@RequestBody @Valid RoomRegisterRequest roomRegisterRequest) {
		return roomService.register(roomRegisterRequest)
				.map(ApiResponse::create)
				.switchIfEmpty(Mono.just(ApiResponse.empty()));
	}

	@ApiOperation("방 조회")
	@GetMapping("/{roomId}")
	public Mono<ApiResponse<Room>> getById(@PathVariable String roomId) {
		return roomService.findById(roomId)
				.map(ApiResponse::create)
				.switchIfEmpty(Mono.just(ApiResponse.empty()));
	}
}
