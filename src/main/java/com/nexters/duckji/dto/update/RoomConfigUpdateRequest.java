package com.nexters.duckji.dto.update;

import javax.validation.constraints.NotNull;

import com.nexters.duckji.domain.RoomBackground;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomConfigUpdateRequest {
	@NotNull
	private String title;
	private RoomBackground background;
}
