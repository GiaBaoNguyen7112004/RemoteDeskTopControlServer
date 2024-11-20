package com.baotruongtuan.RdpServer.service.imp;

import com.baotruongtuan.RdpServer.dto.AvatarDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IAvatarService {
    AvatarDTO saveAvatar(int userId, MultipartFile file);

    void removeAvatar(int userId);
}
