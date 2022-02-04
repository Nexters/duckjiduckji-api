package com.nexters.duckji.api;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexters.duckji.domain.Room;
import com.nexters.duckji.model.ApiResponse;
import com.nexters.duckji.model.RoomRegisterRequest;
import com.nexters.duckji.service.RoomService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rooms")
public class RoomController {
	private final RoomService roomService;

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@PostMapping
	public Mono<ApiResponse<Room>> register(@RequestBody @Valid RoomRegisterRequest roomRegisterRequest) {
		return roomService.register(roomRegisterRequest)
				.map(ApiResponse::create);
	}
}
