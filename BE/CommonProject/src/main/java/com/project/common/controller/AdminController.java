package com.project.common.controller;

import com.project.common.dto.Admin.AdminReportDto;
import com.project.common.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api("관리자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    /**
     * 신고 접수 받기
     *
     * @param adminReportDto
     * @return String
     */

    @ApiOperation(value = "신고 접수 받기", response = String.class)
    @PostMapping("/report")
    public ResponseEntity<String> reportCreate(@RequestBody AdminReportDto adminReportDto) {
        if (adminService.reportCreate(adminReportDto)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 신고 리스트
     *
     * @param
     * @return List
     */

    @ApiOperation(value = "신고 리스트", response = List.class)
    @GetMapping("/list")
    public ResponseEntity<?> listReport() {
        List<AdminReportDto> list = adminService.listReport();
        return new ResponseEntity<List<AdminReportDto>>(list, HttpStatus.OK);
    }

    /**
     * 신고에 따라 게시글 삭제하기
     *
     * @param reportSeq
     * @return String
     */

    @ApiOperation(value = "신고에 따라 게시글 삭제하기", response = String.class)
    @PostMapping("/report/{reportSeq}")
    public ResponseEntity<String> deleteReport(@PathVariable("reportSeq") int reportSeq) {
        if(adminService.deleteReport(reportSeq)){
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
    }

    /**
     * 삭제 알림 보내기
     *
     * @param reportSeq
     * @return String
     */

    @ApiOperation(value = "삭제 알림 보내기", response = String.class)
    @PostMapping("/report/warn/{reportSeq}")
    public ResponseEntity<String> warningAlarm(@PathVariable("reportSeq") int reportSeq) {
        if(adminService.warningAlarm(reportSeq)){
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }else{
            return new ResponseEntity<String>(FAIL, HttpStatus.OK);
        }
    }

    /**
     * 신고 내용 취소 처리
     *
     * @param reportSeq
     * @return String
     */

    @ApiOperation(value = "신고 내용 취소 처리", response = String.class)
    @PutMapping("/report/pass/{reportSeq}")
    public ResponseEntity<String> passReport(@PathVariable("reportSeq") int reportSeq) {
        if(adminService.passReport(reportSeq)){
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }else{
            return new ResponseEntity<String>(FAIL, HttpStatus.OK);
        }
    }

}
