package com.nexters.duckji.api;

import java.util.List;
import java.util.function.BiFunction;

import com.nexters.duckji.domain.BaseDocument;
import com.nexters.duckji.dto.ListResponse;
import com.nexters.duckji.dto.PageInfo;

import reactor.core.publisher.Mono;

public class ResponseCombinator<T extends BaseDocument> {

	protected BiFunction<? super List<T>, Long, Mono<ListResponse<T>>> combine(int originLimit) {
		return (contents, count) -> {
			int size = contents.size();

			T last = null;
			if (size > originLimit) {
				last = contents.remove(size - 1);
			}

			PageInfo pageInfo = null;
			if (last != null) {
				pageInfo = PageInfo.builder()
						.next(last.getId())
						.build();
			}

			return Mono.just(ListResponse.<T>builder()
					.contents(contents)
					.totalCount(count)
					.pageInfo(pageInfo)
					.build());
		};
	}
}
