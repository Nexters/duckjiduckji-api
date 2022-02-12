package com.nexters.duckji.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Content")
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Content implements AuditableDocument {

	@Id
	private String id;
	private ContentType contentType;
	private String roomId;
	private String ownerId;
	private String content;
	private List<Image> images;
	private String frameType;
	private Point point;
	private String rotation;
	private Double opacity;
	private String font;
	private Background background;
	private Double width;
	private Double height;
	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime edtAt;
	private List<String> tags;

	public boolean validRoomId(String roomId) {
		return this.roomId != null && this.roomId.equals(roomId);
	}
}