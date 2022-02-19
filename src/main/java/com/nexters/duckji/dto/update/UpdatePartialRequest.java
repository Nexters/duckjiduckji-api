package com.nexters.duckji.dto.update;

import org.springframework.data.mongodb.core.query.Update;

public interface UpdatePartialRequest {
	Update getUpdate();
}
