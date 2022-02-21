package com.nexters.duckji.domain;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Point {
	@NotNull
	@DecimalMin(value = "0.0", message = "invalid point x")
	private Double x;
	@NotNull
	@DecimalMin(value = "0.0", message = "invalid point y")
	private Double y;
}
