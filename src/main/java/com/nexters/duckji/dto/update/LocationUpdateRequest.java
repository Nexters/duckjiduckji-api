package com.nexters.duckji.dto.update;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.query.Update;

import com.nexters.duckji.domain.Point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateRequest implements UpdatePartialRequest {
	@NotBlank
	private String roomId;
	@Valid
	@NotNull
	private Point point;
	@NotBlank
	private String rotation;

	@Override
	public Update getUpdate() {
		Update update = new Update();
		update.set("point.x", point.getX());
		update.set("point.y", point.getY());
		update.set("rotation", rotation);

		return update;
	}
}
