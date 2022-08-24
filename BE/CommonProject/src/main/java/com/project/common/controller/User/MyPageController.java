package com.project.common.controller.User;
import java.util.List;

import com.project.common.dto.Group.Response.ResGroupEvalDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.common.dto.AR.StampDto;
import com.project.common.dto.Heritage.HeritageDto;
import com.project.common.dto.My.MyScheduleDto;
import com.project.common.dto.User.UserEvalDto;
import com.project.common.dto.User.UserKeywordDto;
import com.project.common.dto.User.UserResponseEvalDto;
import com.project.common.service.ARService;
import com.project.common.service.HeritageService;
import com.project.common.service.MyPageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Api(tags = {"마이페이지 API"})
@RequestMapping("/api/mypage")
public class MyPageController {

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    private final HeritageService heritageService;
    private final MyPageService myPageService;
    private final ARService arService;

    /**
     * 내 문화 유산 스크랩 목록 불러오기
     *
     * @param userSeq
     * @return List
     */

    @GetMapping("/scrap/{userSeq}")
    @ApiOperation(value = "내 문화 유산 스크랩 목록 불러오기", response = List.class)
    public ResponseEntity<?> myScrapList(@PathVariable("userSeq") int userSeq) {
        List<HeritageDto> list = heritageService.myScrapList(userSeq);
        if (list == null) {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<List<HeritageDto>>(list, HttpStatus.OK);
        }
    }

    /**
     * 내 문화 유산 스크랩 삭제하기
     *
     * @param userSeq, heritageSeq
     * @return List
     */

    @DeleteMapping("/scrap/{userSeq}/{heritageSeq}")
    @ApiOperation(value = "내 문화 유산 스크랩 삭제하기", response = String.class)
    public ResponseEntity<String> deleteScrap(@PathVariable("userSeq") int userSeq, @PathVariable("heritageSeq") int heritageSeq) {
        if (heritageService.deleteScrap(userSeq, heritageSeq)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 내 키워드 생성하기
     *
     * @param userKeywordDto
     * @return String
     */

    @ApiOperation(value = "내 키워드 생성하기", response = String.class)
    @PostMapping("/keyword")
    public ResponseEntity<String> createKeyword(@RequestBody UserKeywordDto userKeywordDto) {
        if (myPageService.createKeyword(userKeywordDto)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 내 키워드 리스트 보여주기
     *
     * @param userSeq
     * @return List
     */

    @ApiOperation(value = "내 키워드 리스트 보여주기", response = List.class)
    @GetMapping("/keyword/list/{userSeq}")
    public ResponseEntity<?> listKeyword(@PathVariable("userSeq") int userSeq) {
        List<UserKeywordDto> list = myPageService.listKeyword(userSeq);
        if (list == null) {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<List<UserKeywordDto>>(list, HttpStatus.OK);
        }
    }

    /**
     * 내 키워드 삭제
     *
     * @param myKeywordSeq
     * @return String
     */

    @ApiOperation(value = "내 키워드 삭제", response = String.class)
    @DeleteMapping("/keyword/list/{myKeywordSeq}")
    public ResponseEntity<String> deleteKeyword(@PathVariable("myKeywordSeq") int myKeywordSeq) {
        if (myPageService.deleteKeyword(myKeywordSeq)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }
    }

//    /**
//     * 데일리 메모 생성
//     *
//     * @param myDailyMemoDto
//     * @return String
//     */
//
//    @ApiOperation(value = "데일리 메모 생성", response = String.class)
//    @PostMapping("/memo")
//    public ResponseEntity<String> createDailyMemo(@RequestBody MyDailyMemoDto myDailyMemoDto) {
//        if (myPageService.createDailyMemo(myDailyMemoDto)) {
//            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    /**
//     * 데일리 메모 수정하기
//     *
//     * @param myDailyMemoDto
//     * @return String
//     */
//
//    @ApiOperation(value = "데일리 메모 수정하기", response = String.class)
//    @PutMapping("/memo")
//    public ResponseEntity<String> modifyDailyMemo(@RequestBody MyDailyMemoDto myDailyMemoDto) {
//        if (myPageService.modifyDailyMemo(myDailyMemoDto)) {
//            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    /**
//     * 데일리 메모 불러오기
//     *
//     * @param myDailyMemoDto
//     * @return String
//     */
//
//    @ApiOperation(value = "데일리 메모 불러오기", response = String.class)
//    @PostMapping("/memo/daily")
//    public ResponseEntity<?> showDailyMemo(@RequestBody MyDailyMemoDto myDailyMemoDto) {
//        MyDailyMemoDto dto = myPageService.showDailyMemo(myDailyMemoDto);
//        if (dto == null) {
//            return new ResponseEntity<String>("FAIL", HttpStatus.BAD_REQUEST);
//        } else {
//            return new ResponseEntity<MyDailyMemoDto>(dto, HttpStatus.OK);
//        }
//    }
//
//    /**
//     * 데일리 메모 삭제하기
//     *
//     * @param myDailyMemoSeq
//     * @return String
//     */
//
//    @ApiOperation(value = "데일리 메모 삭제하기", response = String.class)
//    @DeleteMapping("/memo/{myDailyMemoSeq}")
//    public ResponseEntity<String> deleteDailyMemo(@PathVariable("myDailyMemoSeq") int myDailyMemoSeq) {
//        if (myPageService.deleteDailyMemo(myDailyMemoSeq)) {
//            return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<String>("FAIL", HttpStatus.OK);
//        }
//    }

    /**
     * 내 일정 생성
     *
     * @param myScheduleDto
     * @return String
     */

    @ApiOperation(value = "내 일정 생성", response = String.class)
    @PostMapping("/schedule")
    public ResponseEntity<String> createSchedule(@RequestBody MyScheduleDto myScheduleDto) {

        if (myPageService.createSchedule(myScheduleDto)) {
            return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 내 일정 불러오기
     *
     * @param userSeq
     * @return List
     */

    @ApiOperation(value = "내 일정 불러오기", response = List.class)
    @GetMapping("/schedule/{userSeq}/list")
    public ResponseEntity<?> listSchedule(@PathVariable("userSeq") int userSeq) {
        List<MyScheduleDto> list = myPageService.listSchedule(userSeq);
        if (list == null) {
            return new ResponseEntity<String>("FAIL", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<List<MyScheduleDto>>(list, HttpStatus.OK);
        }
    }

//    /**
//     * 내 일정 수정
//     *
//     * @param myScheduleDto
//     * @return String
//     */
//
//    @ApiOperation(value = "내 일정 수정", response = String.class)
//    @PutMapping("/schedule")
//    public ResponseEntity<String> modifySchedule(@RequestBody MyScheduleDto myScheduleDto) {
//        if (myPageService.modifySchedule(myScheduleDto)) {
//            return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<String>("FAIL", HttpStatus.BAD_REQUEST);
//        }
//    }


    /**
     * 내 일정 삭제
     *
     * @param myScheduleSeq
     * @return String
     */

    @ApiOperation(value = "내 일정 삭제", response = String.class)
    @DeleteMapping("/schedule/{myScheduleSeq}")
    public ResponseEntity<String> deleteSchedule(@PathVariable("myScheduleSeq") int myScheduleSeq) {
        if (myPageService.deleteSchedule(myScheduleSeq)) {
            return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 상호 평가 하기
     *
     * @param userEvalDtoList
     * @return String
     */

    @ApiOperation(value = "상호 평가 하기", response = String.class)
    @PostMapping("/eval")
    public ResponseEntity<String> evalMutual(@RequestBody List<UserEvalDto> userEvalDtoList) {
        System.out.println("리스트 사이즈 : " + userEvalDtoList.size());
        if(userEvalDtoList.size()==0){
            return new ResponseEntity<String>("size 0", HttpStatus.OK);
        }
        myPageService.evalMutual(userEvalDtoList);
        return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
    }


    /**
     * 상호 평가 가져오기
     *
     * @param userSeq
     * @return userResponseEvalDto
     */

    @ApiOperation(value = "상호 평가 가져오기", response = Object.class)
    @GetMapping("/eval/{userSeq}")
    public ResponseEntity<?> evalMutualInfo(@PathVariable("userSeq") int userSeq) {
        UserResponseEvalDto userResponseEvalDto = myPageService.evalMutualInfo(userSeq);
        return new ResponseEntity<UserResponseEvalDto>(userResponseEvalDto, HttpStatus.OK);
    }

    /**
     * 그룹별로 상호 평가 가져오기
     *
     * @param userSeq, groupSeq
     * @return List
     */

    @ApiOperation(value = "그룹별로 상호 평가 가져오기", response = List.class)
    @GetMapping("/eval/{userSeq}/{groupSeq}")
    public ResponseEntity<List<ResGroupEvalDto>> evalMutualInfo(@PathVariable("userSeq") int userSeq, @PathVariable("groupSeq") int groupSeq) {
        List<ResGroupEvalDto> list = myPageService.groupEvalInfo(userSeq, groupSeq);
        return new ResponseEntity<List<ResGroupEvalDto>>(list, HttpStatus.OK);
    }


    /**
     * 내가 보유한 스탬프 리스트 보여주기
     * @param userSeq
     * @return List
     */

    @ApiOperation(value = "내가 보유한 스탬프 리스트 보여주기", response = List.class)
    @GetMapping("/stamp/{userSeq}")
    public ResponseEntity<?> listMyStamp(@PathVariable("userSeq") int userSeq){
        List<StampDto> list = arService.listMyStamp(userSeq);
        if(list == null){
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<List<StampDto>>(list, HttpStatus.OK);
        }
    }

}
