package com.project.common.dto.Group;

import com.project.common.entity.Group.GroupMemberEntity;
import com.project.common.entity.User.UserEntity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GroupMemberListDto {

	private int memberSeq;
	private int groupSeq;
	private int memberStatus;
	private String memberAppeal;
	private char memberIsEvaluated;
	private String memberNickname;
	
	private String userId;
	private char memberGender;
	private int eval1;
	private int eval2;
	private int eval3;
	private int eval4;
	private int eval5;
	//private int mystamp;
	
	@Builder
	public GroupMemberListDto(GroupMemberEntity member,UserEntity user) {
		this.memberSeq = member.getMemberSeq();
		this.groupSeq = member.getGroup().getGroupSeq();
		this.memberStatus = member.getMemberStatus();
		this.memberAppeal = member.getMemberAppeal();
		this.memberIsEvaluated = member.getMemberIsEvaluated();
		this.memberNickname=member.getGroup().getUser().getUserNickname();
		this.userId=user.getUserId();
		this.memberGender=user.getUserGender();
		this.eval1=user.getEvalList1();
		this.eval2=user.getEvalList2();
		this.eval3=user.getEvalList3();
		this.eval4=user.getEvalList4();
		this.eval5=user.getEvalList5();
	}
	
}
