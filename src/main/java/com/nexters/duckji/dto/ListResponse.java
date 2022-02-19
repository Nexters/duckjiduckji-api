package com.nexters.duckji.dto;

import java.util.List;

import com.nexters.duckji.domain.Content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListResponse<T> {
	private List<T> contents;
	private Long totalCount;
	private PageInfo pageInfo;
}
