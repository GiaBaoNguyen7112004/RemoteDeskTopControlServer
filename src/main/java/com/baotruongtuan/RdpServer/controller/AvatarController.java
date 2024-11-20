package com.baotruongtuan.RdpServer.controller;


import com.baotruongtuan.RdpServer.payload.request.DepartmentCreationRequest;
import com.baotruongtuan.RdpServer.payload.response.ResponseData;
import com.baotruongtuan.RdpServer.service.imp.IAvatarService;
import com.baotruongtuan.RdpServer.utils.FeedbackMessage;
import com.baotruongtuan.RdpServer.utils.UrlMapping;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(UrlMapping.AVATARS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AvatarController {
    IAvatarService iAvatarService;

    @PostMapping(UrlMapping.SAVE_AVATAR)
    public ResponseEntity<ResponseData> saveAvatar(@RequestBody MultipartFile file, @PathVariable int userId) {
        ResponseData responseData = ResponseData.builder()
                .data(iAvatarService.saveAvatar(userId, file))
                .message(FeedbackMessage.CREATE_SUCCESS)
                .build();
        return ResponseEntity.ok().body(responseData);
    }

    @DeleteMapping(UrlMapping.REMOVE_AVATAR)
    public ResponseEntity<ResponseData> removeAvatar(@PathVariable int userId) {
        iAvatarService.removeAvatar(userId);
        ResponseData responseData = ResponseData.builder()
                .message(FeedbackMessage.DELETE_SUCCESS)
                .build();
        return ResponseEntity.ok().body(responseData);
    }
}
