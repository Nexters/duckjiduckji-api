package com.nexters.duckji.dto.params;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.ObjectUtils;

import com.nexters.duckji.domain.ContentType;
import com.nexters.duckji.dto.CriteriaParams;
import com.nexters.duckji.validator.ObjectIdFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class ContentsApiParams implements CriteriaParams {
	@ApiModelProperty(required = true, value = "room 아이디")
	private String roomId;
	@ApiModelProperty(value = "owner 아이디")
	private String ownerId;
	@ApiModelProperty(value = "컨텐츠 타입", example = "POLAROID / POSTIT")
	private ContentType contentType;
	@ObjectIdFormat
	@ApiModelProperty
	private String next;

	public Criteria criteria() {
		Criteria criteria = new Criteria();
		criteria.and("roomId").is(roomId);

		if (!ObjectUtils.isEmpty(ownerId)) {
			criteria.and("ownerId").is(ownerId);
		}

		if (!ObjectUtils.isEmpty(contentType)) {
			criteria.and("contentType").is(contentType);
		}

		if (!ObjectUtils.isEmpty(next)) {
			criteria.and("_id").lt(new ObjectId(next));
		}

		return criteria;
	}
}
