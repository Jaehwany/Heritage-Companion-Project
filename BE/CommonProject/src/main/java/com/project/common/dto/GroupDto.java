package com.project.common.dto;



import java.util.Date;

import com.project.common.entity.GroupAttributeEntity;
import com.project.common.entity.GroupEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GroupDto {
	private long groupSeq;
	private String groupName;
	private String attachSeq;
	private UserDto groupMaker;
	private String groupDescription;
	private int groupMaxCount;
	private char groupAccessType;
	private String groupPassword;
	private String groupStatus;
	private char groupIsActive;
	private Date groupCreatedAt;
	private Date groupUpdatedAt;

    private GroupAttributeDto groupAttributeDto;
	

	@Builder
	public GroupDto(long groupSeq, String groupName, String attachSeq, UserDto groupMaker, String groupDescription,
			int groupMaxCount, char groupAccessType, String groupPassword, String groupStatus, char groupIsActive,
			Date groupCreatedAt, Date groupUpdatedAt, GroupAttributeDto groupAttributeDto) {
		super();
		this.groupSeq = groupSeq;
		this.groupName = groupName;
		this.attachSeq = attachSeq;
		this.groupMaker = groupMaker;
		this.groupDescription = groupDescription;
		this.groupMaxCount = groupMaxCount;
		this.groupAccessType = groupAccessType;
		this.groupPassword = groupPassword;
		this.groupStatus = groupStatus;
		this.groupIsActive = groupIsActive;
		this.groupCreatedAt = groupCreatedAt;
		this.groupUpdatedAt = groupUpdatedAt;
		this.groupAttributeDto = groupAttributeDto;
	}
	

	
    public GroupEntity toEntity(){
        return GroupEntity.builder()
                .groupSeq(groupSeq)
                .groupName(groupName)
                .attachSeq(attachSeq)
                .groupDescription(groupDescription)
                .groupMaxCount(groupMaxCount)
                .groupAccessType(groupAccessType)
                .groupPassword(groupPassword)
                .groupStatus(groupStatus)
                .groupIsActive(groupIsActive)
                .groupCreatedAt(groupCreatedAt)
                .groupUpdatedAt(groupUpdatedAt).build();

    }











}
