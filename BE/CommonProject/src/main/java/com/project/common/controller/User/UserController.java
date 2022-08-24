package com.project.common.controller.User;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.project.common.config.Jwt.JwtTokenProvider;
import com.project.common.dto.Mail.MailDto;
import com.project.common.dto.User.UserDto;
import com.project.common.dto.User.UserSignDto;
import com.project.common.entity.User.UserEntity;
import com.project.common.repository.User.UserRepository;
import com.project.common.service.MailService;
import com.project.common.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;


@Api("회원관리 API")
@RestController
@RequiredArgsConstructor    // autowired 안써도됨
@RequestMapping("/api/user")
public class UserController {
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    private final UserService userService;
    private final MailService mailService;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * 일반 회원 가입
     *
     * @param userSignDto
     * @return success / fail
     */

    @ApiOperation(value = "일반 회원가입", response = String.class)
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@ApiParam(value = "회원 가입 : 회원정보 ( 아이디, 비밀번호, 닉네임, 성별, 생년월일 ) ", required = true) @RequestBody UserSignDto userSignDto, BindingResult bindingResult) {

        // validation
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }
    
        UserEntity userEntity = UserEntity.builder()
                .userSeq(0)
                .userId(userSignDto.getUserId())
                .userPassword(passwordEncoder.encode(userSignDto.getUserPassword()))
                .userBirth(userSignDto.getUserBirth())
                .userNickname(userSignDto.getUserNickname())
                .userGender(userSignDto.getUserGender())
                .userRegistedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .userUpdatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .socialLoginType("none")
                .fcmToken("")
                .profileImgUrl("")
                .isDeleted('N')
                .evalUpdatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .pushSettingStatus('Y')
                .build();

        userRepository.save(userEntity);
        return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
    }


    /**
     * 회원가입 이메일 인증
     *
     * @param userId
     * @return success / fail
     */
    @GetMapping("/emailAuth/{userId}")
    @ApiOperation(value = "회원가입 이메일 인증", response = String.class)
    public ResponseEntity<String> emailAuth(@ApiParam(value = "사용자 ID ( Email )", required = true) @PathVariable("userId") String userId) {

        // 번호 랜덤으로 만들어주기
        String num = "";
        for (int i = 0; i < 6; i++) {
            num += (int) (Math.random() * 10);
        }
        // 이메일 생성
        MailDto mailDto = mailService.createMail(num, userId);

        // 이메일 전송
        mailService.sendMail(mailDto);

        // 이메일이 생성되었으면 success
        if (mailDto != null) {
            return new ResponseEntity<String>(num, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 회원 탈퇴 기능
     *
     * @param userId
     * @return success / fail
     */

    @PutMapping("/resign/{userId}")
    @ApiOperation(value = "회원탈퇴기능", response = String.class)
    public ResponseEntity<String> resign(@ApiParam(value = "사용자 ID ( Email )", required = true) @PathVariable("userId") String userId) {

        // 회원이 존재하지 X -> 삭제 불가
        if (!userService.resignUser(userId)) {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }
        // 회원이 존재 -> 삭제 완료
        else {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }
    }

    /**
     * 일반 로그인, JWT 발급
     *
     * @param userInfo
     * @return token
     */

    @ApiOperation(value = "일반 로그인", response = String.class)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> userInfo) {
        UserEntity userEntity = userRepository.findByUserId(userInfo.get("userId"));
        // 아이디가 없는 경우 , 아이디가 탈퇴되어 있는 경우
        if (userEntity == null || userEntity.getIsDeleted() == 'Y') {
            return new ResponseEntity<String>(FAIL + " id", HttpStatus.BAD_REQUEST);
        }

        // 소셜 아이디 인 경우
        if (!userEntity.getSocialLoginType().equals("none")) {
            return new ResponseEntity<String>(FAIL + " socialUser", HttpStatus.BAD_REQUEST);
        }

        // 비밀번호가 없는 경우
        if (!passwordEncoder.matches(userInfo.get("userPassword"), userEntity.getUserPassword())) {
            return new ResponseEntity<String>(FAIL + " password", HttpStatus.BAD_REQUEST);
        }


       // 토큰 생성해서 리턴
        String token = jwtTokenProvider.createToken(userEntity.getUserSeq(), userEntity.getUsername(), userEntity.getUserNickname(), userEntity.getUserGender(), userEntity.getUserBirth(), userEntity.getSocialLoginType(), userEntity.getProfileImgUrl());
        if (token != null) {
            return new ResponseEntity<String>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(FAIL + " token", HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 이메일 중복 검사
     *
     * @param userId
     * @return success / fail
     */

    @GetMapping("/check/email/{userId}")
    @ApiOperation(value = "이메일 중복 검사", response = String.class)
    public ResponseEntity<String> checkEmail(@ApiParam(value = "사용자 ID ( Email )", required = true) @PathVariable("userId") String userId) {
        if (userService.checkEmail(userId)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 닉네임 중복 검사
     *
     * @param userNickname
     * @return success / fail
     */

    @GetMapping("/check/nickname/{userNickname}")
    @ApiOperation(value = "닉네임 중복 검사", response = String.class)
    public ResponseEntity<String> checkNickname(@ApiParam(value = "사용자 닉네임 ( Nickname )", required = true) @PathVariable("userNickname") String userNickname) {
        if (userService.checkNickname(userNickname)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    // 토큰에서 사용자 정보 가져오기
    @GetMapping("/me")
    public UserDto getCurrentUser(HttpServletRequest request) { //(1)
        String token = request.getHeader("X-AUTH-TOKEN");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return null;
        } else {
            String userId = jwtTokenProvider.getUserId(token);
            return userService.find(userId);
        }
    }

}

