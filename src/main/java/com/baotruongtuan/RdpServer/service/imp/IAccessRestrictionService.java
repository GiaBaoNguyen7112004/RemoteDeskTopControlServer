package com.baotruongtuan.RdpServer.service.imp;

import java.util.List;

import com.baotruongtuan.RdpServer.dto.AccessRestrictionDTO;
import com.baotruongtuan.RdpServer.payload.request.AccessRestrictionCreationRequest;

public interface IAccessRestrictionService {
    List<AccessRestrictionDTO> getAllAccessRestrictions();

    AccessRestrictionDTO createAccessRestriction(AccessRestrictionCreationRequest accessRestrictionCreationRequest);

    void deleteAccessRestriction(String id);
}
