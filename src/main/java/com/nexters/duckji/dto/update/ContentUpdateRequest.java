package com.nexters.duckji.dto.update;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nexters.duckji.domain.Point;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "contentType", visible = true)
@JsonSubTypes({
		@JsonSubTypes.Type(name = "POLAROID", value = PolaroidUpdateRequest.class)
		//@JsonSubTypes.Type(name = "POSTIT", value = .class),
		//@JsonSubTypes.Type(name = "STICKER", value = .class)
})
public interface ContentUpdateRequest {
	String getRoomId();
	String getContent();
	Point getPoint();
	Double getWidth();
	Double getHeight();
}
