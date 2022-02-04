package com.nexters.duckji.domain;

import java.time.LocalDateTime;

public interface AuditableDocument extends BaseDocument {
	LocalDateTime getCreatedAt();
	LocalDateTime getEdtAt();
}
