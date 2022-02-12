package com.nexters.duckji.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.nexters.duckji.domain.RoomBackground;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomRegisterRequest {
	@Size(min = 1, max = 30)
	private String title;
	@Builder.Default
	@Min(1) @Max(100)
	private Integer headCount = 8;
	private RoomBackground background;
}
