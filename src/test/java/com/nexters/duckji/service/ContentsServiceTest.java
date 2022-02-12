package com.nexters.duckji.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import com.nexters.duckji.config.TestEmbeddedMongoConfig;
import com.nexters.duckji.domain.Content;
import com.nexters.duckji.domain.ContentType;
import com.nexters.duckji.dto.PolaroidRegisterRequest;
import com.nexters.duckji.dto.update.PolaroidUpdateRequest;

import reactor.test.StepVerifier;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.jackson.generator.JacksonArbitraryGenerator;

@ContextConfiguration(classes = {
		TestEmbeddedMongoConfig.class,
		ContentsService.class
})
@ExtendWith(SpringExtension.class)
class ContentsServiceTest {
	@Autowired
	private ContentsService contentsService;

	final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
			.defaultGenerator(JacksonArbitraryGenerator.INSTANCE)
			.build();

	@RepeatedTest(5)
	public void 폴라로이드_컨텐츠_생성_성공() {
		PolaroidRegisterRequest registerRequest = fixtureMonkey.giveMeBuilder(PolaroidRegisterRequest.class)
				.set("contentType", ContentType.POLAROID)
				.sample();

		StepVerifier.create(contentsService.register(registerRequest)
				.flatMap(c -> contentsService.findById(c.getId()))
		).consumeNextWith(content -> {
			String roomId = content.getRoomId();
			assertNotNull(roomId);
			assertEquals(content.getContentType(), ContentType.POLAROID);
			assertTrue(content.getContent().length() > 0);
			assertTrue(content.getImages().size() <= 5);
			assertNotNull(content.getPoint());
			contentsService.deleteById(content.getId(), roomId).block();
		}).verifyComplete();
	}

	@RepeatedTest(5)
	public void 폴라로이드_컨텐츠_patch_성공() {
		// insert
		PolaroidRegisterRequest registerRequest = fixtureMonkey.giveMeBuilder(PolaroidRegisterRequest.class)
				.set("contentType", ContentType.POLAROID)
				.sample();

		Content origin = contentsService.register(registerRequest).block();
		assert origin != null;
		String contentId = origin.getId();

		// update
		PolaroidUpdateRequest updateRequest = fixtureMonkey.giveMeBuilder(PolaroidUpdateRequest.class)
				.set("roomId", origin.getRoomId())
				.set("contentType", ContentType.POLAROID)
				.sample();

		StepVerifier.create(contentsService.patchById(updateRequest, contentId)
				.flatMap(c -> contentsService.findById(contentId))
		).consumeNextWith(content -> {
			String roomId = content.getRoomId();
			assertEquals(origin.getRoomId(), roomId);
			assertEquals(origin.getContentType(), content.getContentType());
			assertTrue(content.getContent().length() > 0);
			assertNotEquals(origin.getEdtAt(), content.getEdtAt());
			assertNotNull(content.getPoint());
			contentsService.deleteById(content.getId(), roomId).block();
		}).verifyComplete();
	}

	@Test
	public void 존재하지않는_폴라로이드_컨텐츠_수정시_ResponseStatusException_리턴() {
		String contentId = UUID.randomUUID().toString();
		PolaroidUpdateRequest updateRequest = fixtureMonkey.giveMeBuilder(PolaroidUpdateRequest.class)
				.set("contentType", ContentType.POLAROID)
				.sample();

		StepVerifier.create(contentsService.patchById(updateRequest, contentId))
				.expectError(ResponseStatusException.class)
				.verify();
	}
}