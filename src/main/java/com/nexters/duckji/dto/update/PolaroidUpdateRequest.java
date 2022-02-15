package com.nexters.duckji.dto.update;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.nexters.duckji.domain.Background;
import com.nexters.duckji.domain.ContentType;
import com.nexters.duckji.domain.Image;
import com.nexters.duckji.domain.Point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PolaroidUpdateRequest implements ContentUpdateRequest {
	@NotNull
	@Builder.Default
	private ContentType contentType = ContentType.POLAROID;
	@NotBlank(message = "roomId must not be blank")
	private String roomId;
	@Size(min = 1, max = 60, message = "invalid content length")
	private String content;
	@Size(max = 5, message = "invalid images count")
	private List<Image> images;
	private String frameType;
	@Valid
	private Point point;
	private String rotation;
	private Double opacity;
	private String font;
	private Background background;
	private Double width;
	private Double height;
	private List<String> tags;
}
