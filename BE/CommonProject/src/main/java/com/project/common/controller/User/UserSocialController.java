package com.project.common.controller.User;

import com.project.common.config.Jwt.JwtTokenProvider;
import com.project.common.dto.User.UserDto;
import com.project.common.dto.User.UserSignDto;
import com.project.common.entity.User.UserEntity;
import com.project.common.repository.User.UserRepository;
import com.project.common.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Api("소셜 로그인 API")
@RequestMapping("/api/user/social")
public class UserSocialController {

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;


    /**
     * 소셜 아이디 확인
     *  
     * @param userId
     * @return success / fail
     */
    
    @ApiOperation(value="소셜 아이디 확인", response = String.class)
    @GetMapping("/checkId/{userId}")
    public ResponseEntity<String> checkId(@ApiParam(value = "소셜 아이디 확인 : 아이디 ( 이메일 )", required = true) @PathVariable String userId){
        // 아이디가 있는지 없는지 체크
        UserDto userDto = userService.find(userId);
        
        // 유저 아이디가 없는 경우
        if(userDto == null){
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }else{
            // 소셜 아이딘지 일반 아이딘지 확인
            if(userDto.getSocialLoginType().equals("social")){
                return new ResponseEntity<String>(FAIL+" social", HttpStatus.OK);
            }else if(userDto.getSocialLoginType().equals("none")){
                return new ResponseEntity<String>(FAIL+" none", HttpStatus.OK);
            }
            // 둘다 아닌 경우
            else{
                return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
            }
        }




    }
    
    /**
     * 소셜 회원 가입
     *
     * @param userSignDto
     * @return success / fail
     */
    @ApiOperation(value = "소셜 회원가입", response = String.class)
    @PostMapping("/signup")
    public ResponseEntity<String> socialSignup(@ApiParam(value = "소셜 회원 가입 : 회원정보 ( 아이디, 비밀번호, 닉네임, 성별, 생년월일 ) ", required = true) @RequestBody UserSignDto userSignDto, BindingResult bindingResult) {

        // validation
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }

        userRepository.save(UserEntity.builder()
                .userId(userSignDto.getUserId())
                .userPassword(passwordEncoder.encode(userSignDto.getUserPassword()))
                .userBirth(userSignDto.getUserBirth())
                .userNickname(userSignDto.getUserNickname())
                .userGender(userSignDto.getUserGender())
                .userRegistedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .userUpdatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .socialLoginType("social")
                .fcmToken("")
                .profileImgUrl("")
                .isDeleted('N')
                .evalUpdatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .pushSettingStatus('Y')
                .build());
        return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);

    }



    /**
     * 소셜 로그인, JWT 발급
     *
     * @param userInfo
     * @return token
     */

    @ApiOperation(value = "소셜 로그인", response = String.class)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> userInfo) {
        UserEntity userEntity = userRepository.findByUserId(userInfo.get("userId"));
        
        // 아이디가 없는 경우 나 이미 탈퇴한 경우
        if(userEntity == null || userEntity.getIsDeleted()=='Y'){
            return new ResponseEntity<String>(FAIL+" id", HttpStatus.BAD_REQUEST);
        }

        // 소셜 사용자가 아닌 경우
        if(!userEntity.getSocialLoginType().equals("social")){
            return new ResponseEntity<String>(FAIL+" noneUser", HttpStatus.BAD_REQUEST);
        }


        // 토큰 생성해서 리턴
        String token = jwtTokenProvider.createToken(userEntity.getUserSeq(),userEntity.getUsername(), userEntity.getUserNickname(), userEntity.getUserGender(), userEntity.getUserBirth(), userEntity.getSocialLoginType(), userEntity.getProfileImgUrl());
        if(token != null){
            return new ResponseEntity<String>(token, HttpStatus.OK);
        }else{
            return new ResponseEntity<String>(FAIL+" token", HttpStatus.BAD_REQUEST);
        }
    }
}