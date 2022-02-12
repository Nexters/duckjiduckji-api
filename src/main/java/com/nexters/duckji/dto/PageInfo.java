package com.nexters.duckji.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Copyright 2021 NAVER Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @author smean.lee on 22. 2. 10..
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo {
	private String next;
}
