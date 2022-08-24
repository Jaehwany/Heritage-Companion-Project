package com.project.common.controller;

import com.project.common.dto.AR.MyLocationDto;
import com.project.common.dto.Heritage.HeritageDto;
import com.project.common.dto.Heritage.HeritageReivewDto;
import com.project.common.dto.Heritage.HeritageScrapDto;
import com.project.common.dto.Heritage.SortHeritageDto;
import com.project.common.service.HeritageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Api("문화유산 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/heritage")
public class HeritageController {
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    private final HeritageService heritageService;

    /**
     * 문화 유산 정보 불러오기
     * @param
     * @return List
     */
    
    @ApiOperation(value="문화 유산 상세 정보 불러오기, 문화유산 번호를 통해 상세정보를 불러온다.", response = List.class)
    @GetMapping("/list")
    public ResponseEntity<List<HeritageDto>> listInfo() throws Exception {
        return new ResponseEntity<List<HeritageDto>>(heritageService.listInfo(), HttpStatus.OK);
    }

    /**
     * 문화 유산 리뷰 작성
     * @param  heritageReivewDto
     * @return String
     */

    @ApiOperation(value = "문화 유산 리뷰 작성 ", response = String.class)
    @PostMapping("/review")
    public ResponseEntity<String> createReview(@ApiParam(value = "회원 번호, 문화 유산 번호", required = true) @RequestBody HeritageReivewDto heritageReivewDto){
        heritageReivewDto.setHeritageReviewSeq(0);
        heritageReivewDto.setHeritageReviewRegistedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // 성공적으로 입력된다면
        if(heritageService.createReview(heritageReivewDto)){
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }else{
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 문화 유산 리뷰 삭제
     * @param  heritageReviewSeq, heritageSeq
     * @return String
     */

    @ApiOperation(value = "문화 유산 리뷰 삭제 ", response = String.class)
    @DeleteMapping("/review/{heritageReviewSeq}/{heritageSeq}")
    public ResponseEntity<String> deleteReview(@PathVariable("heritageReviewSeq") int heritageReviewSeq, @PathVariable("heritageSeq") int heritageSeq){

        // 성공적으로 입력된다면
        if(heritageService.deleteReview(heritageReviewSeq, heritageSeq)){
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }else{
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 문화 유산 리뷰 목록
     * @param heritageSeq
     * @return List
     */

    @ApiOperation(value = "문화 유산 리뷰 목록", response = List.class)
    @GetMapping("/reviews/{heritageSeq}")
    public ResponseEntity<?> reviewList(@PathVariable("heritageSeq") int heritageSeq){
        List<HeritageReivewDto> list = heritageService.reviewList(heritageSeq);
        if(list == null){
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<List<HeritageReivewDto>>(list,HttpStatus.OK);
        }
    }

    /**
     * 문화 유산 스크랩 등록
     * @param heritageScrapDto
     * @return String
     */

    @ApiOperation(value = "문화 유산 스크랩 등록", response = String.class)
    @PostMapping("/scrap")
    public ResponseEntity<String> createScrap(@RequestBody HeritageScrapDto heritageScrapDto){

        // 성공적으로 입력 되었으면
        if(heritageService.createScrap(heritageScrapDto)){
            return new ResponseEntity<String>(SUCCESS,HttpStatus.OK);
        }else{
            return new ResponseEntity<String>(FAIL,HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 문화 유산 본인 위치 기준으로 가까운 순으로 정렬
     * @param myLocationDto
     * @return List
     */
    @ApiOperation(value = "문화 유산 본인 위치 기준으로 가까운 순으로 정렬", response = List.class)
    @PostMapping("/heritage-info")
    public ResponseEntity<?> sortHeritage(@RequestBody MyLocationDto myLocationDto){
        List<HeritageDto> list = heritageService.sortHeritage(myLocationDto);
        if(list.size() == 0){
            return new ResponseEntity<String>(FAIL,HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<List<HeritageDto>>(list,HttpStatus.OK);
        }
    }


    /**
     * 카테고리 , 정렬별로 문화유산 리스트 가져오기
     * @param sortHeritageDto
     * @return List
     */
    @ApiOperation(value = "카테고리 , 정렬별로 문화유산 리스트 가져오기", response = List.class)
    @PostMapping("/heritage-info/sort")
    public ResponseEntity<?> categorySortHeritage(@RequestBody SortHeritageDto sortHeritageDto){
        List<HeritageDto> list = heritageService.categorySortHeritage(sortHeritageDto);
        // 번호 입력이 잘못된 경우
        if(list == null){
            return new ResponseEntity<String>(FAIL,HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<List<HeritageDto>>(list,HttpStatus.OK);
        }
    }

}
