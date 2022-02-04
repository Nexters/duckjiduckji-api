package com.nexters.duckji.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Room")
@EqualsAndHashCode
public class Room implements AuditableDocument {

	@Id
	private String id;
	/**
	 * 타이틀
	 */
	private String title;
	/**
	 * 소유자 ID
	 */
	private String owner;
	/**
	 * 멤버 인원 제한
	 */
	private Integer headCount;
	/**
	 * background 설정
	 */
	private RoomBackground background;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime edtAt;

	/**
	 * 방 내의 컨텐츠 수정 시간
	 */
	private Integer contentsEdtAt;

	/**
	 * 초대된 멤버
	 */
	//private List<Member> members;

}
