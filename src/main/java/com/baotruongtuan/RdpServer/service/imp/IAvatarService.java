package com.baotruongtuan.RdpServer.service.imp;

import org.springframework.web.multipart.MultipartFile;

import com.baotruongtuan.RdpServer.dto.AvatarDTO;

public interface IAvatarService {
    AvatarDTO saveAvatar(int userId, MultipartFile file);

    void removeAvatar(int userId);
}
