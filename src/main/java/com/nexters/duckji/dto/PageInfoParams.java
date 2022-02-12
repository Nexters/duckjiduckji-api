package com.nexters.duckji.dto;

import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class PageInfoParams {
	@Range(min = 0, max = 100)
	@ApiModelProperty
	private Integer limit = 20;
	/**
	 * sort 파라미터는 일단 하나만 지원 > 추후 확장되는 스펙 보고 지원
	 */
	@ApiModelProperty(hidden = true)
	private String sort = "_id";

	public PageRequest pageRequest() {
		return PageRequest.of(0, limit + 1, Sort.by(sort).descending());
	}
}
