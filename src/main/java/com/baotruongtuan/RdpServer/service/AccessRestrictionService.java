package com.baotruongtuan.RdpServer.service;

import java.net.URI;
import java.util.List;

import com.baotruongtuan.RdpServer.repository.AccessRestrictionsRepository;
import com.baotruongtuan.RdpServer.utils.DomainExtractHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.baotruongtuan.RdpServer.dto.AccessRestrictionDTO;
import com.baotruongtuan.RdpServer.entity.AccessRestriction;
import com.baotruongtuan.RdpServer.exception.AppException;
import com.baotruongtuan.RdpServer.exception.ErrorCode;
import com.baotruongtuan.RdpServer.mapper.AccessRestrictionMapper;
import com.baotruongtuan.RdpServer.payload.request.AccessRestrictionCreationRequest;
import com.baotruongtuan.RdpServer.service.imp.IAccessRestrictionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AccessRestrictionService implements IAccessRestrictionService {
    AccessRestrictionMapper accessRestrictionMapper;
    AccessRestrictionsRepository accessRestrictionsRepository;
    DomainExtractHelper domainExtractHelper;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<AccessRestrictionDTO> getAllAccessRestrictions() {
        return accessRestrictionsRepository.findAll().stream()
                .map(accessRestrictionMapper::toAccessRestrictionDTO)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public AccessRestrictionDTO createAccessRestriction(
            AccessRestrictionCreationRequest accessRestrictionCreationRequest) {
        String content = accessRestrictionCreationRequest.getContent();
        String processedContent = (domainExtractHelper.isValidUrl(content))
                ? domainExtractHelper.extractDomain(content) : content;

        if (accessRestrictionsRepository.existsAccessRestrictionByContent(processedContent)) {
            throw new AppException(ErrorCode.DUPLICATE_DATA);
        }
        AccessRestriction accessRestriction =
                AccessRestriction.builder().content(processedContent).build();
        return accessRestrictionMapper
                .toAccessRestrictionDTO(accessRestrictionsRepository.save(accessRestriction));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteAccessRestriction(String id) {
        accessRestrictionsRepository.findById(id)
                .ifPresentOrElse(accessRestrictionsRepository::delete, () -> {
            throw new AppException(ErrorCode.NO_DATA_EXCEPTION);
        });
    }
}
