package com.project.common.controller;

import com.project.common.dto.Push.FcmHistoryDto;
import com.project.common.dto.Push.FcmRequestDto;
import com.project.common.dto.Push.FcmToken;
import com.project.common.entity.User.UserEntity;
import com.project.common.repository.User.UserRepository;
import com.project.common.service.FirebaseCloudMessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Api("FCM 토큰 API")
@RequiredArgsConstructor
@RequestMapping("/api/push")
public class FcmTokenController {

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final UserRepository userRepository;

    /**
     * 메세지 전송
     *
     * @param fcmRequestDto
     * @return String
     */

    @PostMapping("/sendMessageTo")
    public ResponseEntity<?> sendMessageTo(@RequestBody FcmRequestDto fcmRequestDto) throws IOException {
        firebaseCloudMessageService.sendMessageTo(fcmRequestDto.getTargetToken(), fcmRequestDto.getTitle(), fcmRequestDto.getBody());

        return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
    }

    /**
     * 전체 알림 설정 하기
     *
     * @param userSeq, userSetting
     * @return String
     */

    @PutMapping("/{userSeq}/{userSetting}/push-setting")
    @ApiOperation(value = "전체 알림 설정 하기", response = String.class)
    public ResponseEntity<String> pushSetting(@PathVariable("userSeq") int userSeq, @PathVariable("userSetting") char setting) {
        if (firebaseCloudMessageService.pushSetting(userSeq, setting)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 토큰 받고 User DB에 저장
     *
     * @param fcmToken
     * @return String
     */

    @PostMapping("/token")
    @ApiOperation(value = "토큰 받고 User DB에 저장", response = String.class)
    public ResponseEntity<String> saveToken(@RequestBody FcmToken fcmToken) {
        UserEntity userEntity = userRepository.findByUserSeq(fcmToken.getUserSeq());
        if (userEntity == null) {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        } else {
            userEntity.setFcmToken(fcmToken.getFcmToken());
            userRepository.save(userEntity);
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }
    }

    /**
     * 알림 기록하기
     *
     * @param fcmHistoryDto
     * @return String
     */

    @PostMapping("/history")
    @ApiOperation(value = "알림 기록하기", response = String.class)
    public ResponseEntity<String> createHistory(@RequestBody FcmHistoryDto fcmHistoryDto){
        if(firebaseCloudMessageService.createHistory(fcmHistoryDto)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }else{
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 알림 기록 리스트 보기
     *
     * @param userSeq
     * @return List
     */

    @GetMapping("/history/{userSeq}")
    @ApiOperation(value = "알림 기록 리스트 보기", response = List.class)
    public ResponseEntity<?> listHistory(@PathVariable("userSeq") int userSeq){
        List<FcmHistoryDto> list = firebaseCloudMessageService.listHistory(userSeq);
        if(list == null) {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<List<FcmHistoryDto>>(list, HttpStatus.OK);
        }
    }

    /**
     * 내 알림 설정 조회
     *
     * @param userSeq
     * @return String
     */

    @GetMapping("/{userSeq}")
    @ApiOperation(value = "내 알림 설정 조회", response = List.class)
    public ResponseEntity<String> settingInfo(@PathVariable("userSeq") int userSeq){
        String setting = firebaseCloudMessageService.settingInfo(userSeq);
        if(setting == null) {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<String>(setting, HttpStatus.OK);
        }
    }
}
