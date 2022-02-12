package com.nexters.duckji.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.nexters.duckji.dto.RoomRegisterRequest;

import reactor.test.StepVerifier;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.jackson.generator.JacksonArbitraryGenerator;

@ActiveProfiles(value = "default")
@SpringBootTest
public class RoomServiceTest {
	@Autowired
	private RoomService roomService;

	@RepeatedTest(10)
	public void 방_등록_테스트_성공() {
		FixtureMonkey fixture = FixtureMonkey.builder()
				.defaultGenerator(JacksonArbitraryGenerator.INSTANCE)
				.build();

		RoomRegisterRequest registerRequest = fixture.giveMeOne(RoomRegisterRequest.class);

		StepVerifier.create(roomService.register(registerRequest))
				.assertNext(room -> {
					assertNotNull(room.getId());
					assertEquals(room.getTitle(), registerRequest.getTitle());
					assertEquals(room.getHeadCount(), registerRequest.getHeadCount());
					assertEquals(room.getBackground(), registerRequest.getBackground());
					roomService.deleteById(room.getId()).block();
				})
				.verifyComplete();
	}

	@Test
	public void 존재하지않는방_조회시_empty_리턴() {
		String roomId = UUID.randomUUID().toString();

		StepVerifier.create(roomService.findById(roomId))
				.expectSubscription()
				.expectNextCount(0)
				.verifyComplete();
	}

	@Test
	public void 존재하지않는방_삭제시_count_0_리턴() {
		String roomId = UUID.randomUUID().toString();

		StepVerifier.create(roomService.deleteById(roomId))
				.assertNext(deleteResult -> {
					assertEquals(deleteResult.getDeletedCount(), 0);
				})
				.verifyComplete();
	}
}