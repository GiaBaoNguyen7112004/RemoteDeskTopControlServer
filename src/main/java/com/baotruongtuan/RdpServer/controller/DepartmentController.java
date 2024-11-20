package com.baotruongtuan.RdpServer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.baotruongtuan.RdpServer.payload.request.DepartmentCreationRequest;
import com.baotruongtuan.RdpServer.payload.response.ResponseData;
import com.baotruongtuan.RdpServer.service.imp.IDepartmentService;
import com.baotruongtuan.RdpServer.utils.FeedbackMessage;
import com.baotruongtuan.RdpServer.utils.UrlMapping;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(UrlMapping.DEPARTMENTS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentController {
    IDepartmentService iDepartmentService;

    @PostMapping(UrlMapping.CREATE_DEPARTMENT)
    public ResponseEntity<ResponseData> createDepartment(
            @RequestBody DepartmentCreationRequest departmentCreationRequest) {
        ResponseData responseData = ResponseData.builder()
                .data(iDepartmentService.createDepartment(departmentCreationRequest))
                .message(FeedbackMessage.CREATE_SUCCESS)
                .build();
        return ResponseEntity.ok().body(responseData);
    }

    @GetMapping(UrlMapping.GET_ALL_DEPARTMENTS)
    public ResponseEntity<ResponseData> getAllDepartments() {
        ResponseData responseData = ResponseData.builder()
                .data(iDepartmentService.getAllDepartments())
                .message(FeedbackMessage.GET_SUCCESS)
                .build();
        return ResponseEntity.ok().body(responseData);
    }

    @GetMapping(UrlMapping.GET_MEMBERS_IN_DEPARTMENT)
    public ResponseEntity<ResponseData> getMembersInDepartment(@PathVariable int departmentId) {
        ResponseData responseData = ResponseData.builder()
                .data(iDepartmentService.getMembersInDepartment(departmentId))
                .message(FeedbackMessage.GET_SUCCESS)
                .build();
        return ResponseEntity.ok().body(responseData);
    }
}
