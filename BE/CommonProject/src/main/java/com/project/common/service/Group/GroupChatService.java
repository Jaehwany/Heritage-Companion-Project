package com.project.common.service.Group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.common.dto.Chat.GroupChatDto;
import com.project.common.entity.Group.GroupChatEntity;
import com.project.common.entity.User.UserEntity;
import com.project.common.repository.Group.GroupChatRepository;
import com.project.common.repository.User.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupChatService{

    private final GroupChatRepository groupChatRepository;
    private final UserRepository userRepository;
    
    public void saveMessage(GroupChatDto chat){
    	GroupChatEntity entity = chat.toEntity();
    	entity.setCreatedTime(new Date());
    	groupChatRepository.save(entity);
    }
    
    // 저장 메시지 보내기
    public List<GroupChatDto> getMessages(int groupSeq) {
        List<GroupChatDto> messages =new ArrayList<>();
        for(GroupChatEntity entity: groupChatRepository.findAll()) {
        	UserEntity user = userRepository.findByUserSeq(entity.getUserSeq());
        	if(entity.getGroupSeq()==groupSeq)
        		messages.add(new GroupChatDto(entity,user));
        }
        return messages;
    }
    

}
