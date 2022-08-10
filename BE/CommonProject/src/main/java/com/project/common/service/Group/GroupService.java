package com.project.common.service.Group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.common.dto.Group.GroupDto;
import com.project.common.dto.Group.Request.ReqGroupDto;
import com.project.common.dto.Group.Response.ResMyGroupDto;
import com.project.common.entity.Group.GroupEntity;
import com.project.common.entity.Group.GroupMemberEntity;
import com.project.common.entity.User.UserEntity;
import com.project.common.mapper.Group.GroupMapper;
import com.project.common.repository.Group.GroupMemberRepository;
import com.project.common.repository.Group.GroupRepository;
import com.project.common.repository.User.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService{
	private final GroupRepository groupRepository;
	private final GroupMemberRepository groupMemberRepository;
	private final UserRepository userRepository;
	
	//모임 개설
	@Transactional
	public GroupDto addGroup(String userId,ReqGroupDto groupDto) {
		UserEntity user = userRepository.findByUserId(userId);
		GroupEntity saved= groupDto.toEntity();
		saved.setGroupMaster(user.getUserNickname());
	 	saved.setCreatedTime(new Date());
		saved.setUpdatedTime(new Date());
		saved.setGroupStatus('R');
		saved.setGroupActive('Y');
		saved.addGroupMember(GroupMemberEntity.builder()
				.memberAppeal("방장")
				.userSeq(user.getUserSeq())
				.memberStatus(2)
				.memberIsEvaluated('N')
				.createdTime(new Date())
				.updatedTime(new Date())
				.approveTime(new Date())
				.build());
		groupRepository.save(saved);
		user.addGroup(saved);
	 	userRepository.save(user);
		return GroupMapper.MAPPER.toDto(saved);
	}
	
	//모임 목록 조회
	public List<GroupDto> getGroupList(){
		List<GroupEntity> groupList=groupRepository.findAll();
		return GroupMapper.MAPPER.toDtoList(groupList);
	}
	
	//모임 정보 보기
	public GroupDto getGroupInfo(int groupSeq) {
		GroupEntity groupInfo=groupRepository.findByGroupSeq(groupSeq);
		return GroupMapper.MAPPER.toDto(groupInfo);
	}
	
	//모임 삭제
	public String deleteGroup(int groupSeq){
		GroupEntity group = groupRepository.findByGroupSeq(groupSeq);
		for(GroupMemberEntity entity : group.getMembers()) {
			groupMemberRepository.deleteByMemberSeq(entity.getMemberSeq());
		}
		groupRepository.deleteById(groupSeq);
		return "Success";
	}
	
	//모임 정보 수정
	public GroupDto updateGroup(int groupSeq,ReqGroupDto groupDto) {
		GroupEntity Group =groupRepository.findByGroupSeq(groupSeq);
		
		Group.setGroupAccessType(groupDto.getGroupAccessType());
		Group.setGroupAgeRange(groupDto.getGroupAgeRange());
		Group.setGroupDescription(groupDto.getGroupDescription());
		Group.setGroupEndDate(groupDto.getGroupEndDate());
		Group.setGroupMaxCount(groupDto.getGroupMaxCount());
		Group.setGroupName(groupDto.getGroupName());
		Group.setGroupPassword(groupDto.getGroupPassword());
		Group.setGroupRegion(groupDto.getGroupRegion());
		Group.setGroupStartDate(groupDto.getGroupStartDate());
		Group.setGroupWithGlobal(Group.getGroupWithGlobal());
		Group.setGroupWithChild(Group.getGroupWithChild());
		Group.setUpdatedTime(new Date());
		
		groupRepository.save(Group);
		return GroupMapper.MAPPER.toDto(Group);
	}
	
	//내 모임 조회
	public List<ResMyGroupDto> getMyGroupList(String userId){
		List<ResMyGroupDto> groupList=new ArrayList<>();
		UserEntity user = userRepository.findByUserId(userId);
		for(GroupMemberEntity entity : groupMemberRepository.findAll()) {
			if(entity.getUserSeq()==user.getUserSeq())
				groupList.add(new ResMyGroupDto(entity));
		}
		return groupList;
	}
	
	//모임 이미지 수정
	public String updateGroupImage(int groupSeq,String fileDownloadUri) {
		GroupEntity Group =groupRepository.findByGroupSeq(groupSeq);
		Group.setGroupImgUrl(fileDownloadUri);
		groupRepository.save(Group);
		return fileDownloadUri;
	}

}
