package com.nexters.duckji.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nexters.duckji.domain.ContentType;
import com.nexters.duckji.domain.Point;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "contentType", visible = true)
@JsonSubTypes({
		@JsonSubTypes.Type(name = "POLAROID", value = PolaroidRegisterRequest.class)
		//@JsonSubTypes.Type(name = "POSTIT", value = .class),
		//@JsonSubTypes.Type(name = "STICKER", value = .class)
})
public interface ContentRegisterRequest {
	ContentType getContentType();
	String getRoomId();
	Point getPoint();
}
