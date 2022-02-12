package com.nexters.duckji.api;

import java.util.List;
import java.util.function.BiFunction;

import com.nexters.duckji.domain.Content;
import com.nexters.duckji.model.ContentsResponse;
import com.nexters.duckji.model.PageInfo;

import reactor.core.publisher.Mono;

public class ContentsResponseCombinator {

	protected BiFunction<? super List<Content>, Long, Mono<ContentsResponse>> combine(int originLimit) {
		return (contents, count) -> {
			int size = contents.size();

			Content last = null;
			if (size > originLimit) {
				last = contents.remove(size - 1);
			}

			PageInfo pageInfo = null;
			if (last != null) {
				pageInfo = PageInfo.builder()
						.next(last.getId())
						.build();
			}

			return Mono.just(ContentsResponse.builder()
					.contents(contents)
					.totalCount(count)
					.pageInfo(pageInfo)
					.build());
		};
	}
}
