package com.nexters.duckji.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import net.jqwik.api.Arbitrary;

import com.nexters.duckji.config.TestEmbeddedMongoConfig;
import com.nexters.duckji.domain.Room;
import com.nexters.duckji.dto.RoomRegisterRequest;
import com.nexters.duckji.dto.update.RoomConfigUpdateRequest;

import reactor.test.StepVerifier;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.jackson.generator.JacksonArbitraryGenerator;

@ContextConfiguration(classes = {
		TestEmbeddedMongoConfig.class,
		RoomService.class
})
@ExtendWith(SpringExtension.class)
public class RoomServiceTest {
	@Autowired
	private RoomService roomService;

	FixtureMonkey fixture = FixtureMonkey.builder()
			.defaultGenerator(JacksonArbitraryGenerator.INSTANCE)
			.build();

	Arbitrary<RoomConfigUpdateRequest> updateRequestArbitrary = fixture.giveMeBuilder(RoomConfigUpdateRequest.class)
			.setNotNull("title")
			.setNotNull("background")
			.build();

	@RepeatedTest(10)
	public void 방_등록_테스트_성공() {

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

	@Test
	public void 존재하지않는_방_설정_변경시_empty_리턴() {
		String roomId = UUID.randomUUID().toString();
		RoomConfigUpdateRequest updateRequest = updateRequestArbitrary.sample();

		StepVerifier.create(roomService.patchConfigById(updateRequest, roomId))
				.expectSubscription()
				.expectNextCount(0)
				.verifyComplete();
	}

	@RepeatedTest(5)
	public void 방_patch_성공() {
		RoomRegisterRequest registerRequest = fixture.giveMeOne(RoomRegisterRequest.class);
		Room origin = roomService.register(registerRequest).block();

		assert origin != null;
		String roomId = origin.getId();
		RoomConfigUpdateRequest updateRequest = updateRequestArbitrary.sample();

		StepVerifier.create(roomService.patchConfigById(updateRequest, roomId)
				.flatMap(r -> roomService.findById(r.getId()))
		).consumeNextWith(r -> {
			assertNotEquals(r.getTitle(), origin.getTitle());
			assertNotEquals(r.getBackground(), origin.getBackground());
		}).verifyComplete();

		roomService.deleteById(roomId).block();
	}
}