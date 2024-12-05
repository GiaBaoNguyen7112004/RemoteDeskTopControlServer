package com.baotruongtuan.RdpServer.mapper;

import org.mapstruct.Mapper;

import com.baotruongtuan.RdpServer.dto.AccessRestrictionDTO;
import com.baotruongtuan.RdpServer.entity.AccessRestriction;
import com.baotruongtuan.RdpServer.payload.request.AccessRestrictionCreationRequest;

@Mapper(componentModel = "spring")
public interface AccessRestrictionMapper {
    AccessRestrictionDTO toAccessRestrictionDTO(AccessRestriction accessRestriction);

    AccessRestriction toAccessRestriction(AccessRestrictionCreationRequest accessRestrictionCreationRequest);
}
