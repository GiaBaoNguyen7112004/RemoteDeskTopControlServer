package com.baotruongtuan.RdpServer.controller;

import com.baotruongtuan.RdpServer.payload.ResponseData;
import com.baotruongtuan.RdpServer.payload.request.UserCreationRequest;
import com.baotruongtuan.RdpServer.payload.request.UserUpdateRequest;
import com.baotruongtuan.RdpServer.service.imp.UserServiceImp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserServiceImp userServiceImp;

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody UserCreationRequest userCreationRequest)
    {
        ResponseData responseData = ResponseData.builder()
                .data(userServiceImp.createUser(userCreationRequest))
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAllUsers()
    {
        ResponseData responseData = ResponseData.builder()
                .data(userServiceImp.getAllUsers())
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestParam int id)
    {
        ResponseData responseData = ResponseData.builder()
                .data(userServiceImp.deleteUser(id))
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/findUserById")
    public ResponseEntity<?> getUserById(@RequestParam int id)
    {
        ResponseData responseData = ResponseData.builder()
                .data(userServiceImp.getUser(id))
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUserById(@RequestBody UserUpdateRequest userUpdateRequest)
    {
        ResponseData responseData = ResponseData.builder()
                .data(userServiceImp.updateUser(userUpdateRequest))
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
