package com.baotruongtuan.RdpServer.service;

import java.net.URI;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baotruongtuan.RdpServer.dto.AccessRestrictionDTO;
import com.baotruongtuan.RdpServer.entity.AccessRestriction;
import com.baotruongtuan.RdpServer.exception.AppException;
import com.baotruongtuan.RdpServer.exception.ErrorCode;
import com.baotruongtuan.RdpServer.mapper.AccessRestrictionMapper;
import com.baotruongtuan.RdpServer.payload.request.AccessRestrictionCreationRequest;
import com.baotruongtuan.RdpServer.repository.AccessRestrictionRepository;
import com.baotruongtuan.RdpServer.service.imp.IAccessRestrictionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AccessRestrictionService implements IAccessRestrictionService {
    AccessRestrictionMapper accessRestrictionMapper;
    AccessRestrictionRepository accessRestrictionRepository;

    @Override
    public List<AccessRestrictionDTO> getAllAccessRestrictions() {
        return accessRestrictionRepository.findAll().stream()
                .map(accessRestrictionMapper::toAccessRestrictionDTO)
                .toList();
    }

    @Override
    public AccessRestrictionDTO createAccessRestriction(
            AccessRestrictionCreationRequest accessRestrictionCreationRequest) {
        String domain = extractDomain(accessRestrictionCreationRequest.getDomain());

        if (accessRestrictionRepository.existsByDomain(domain)) {
            throw new AppException(ErrorCode.DUPLICATE_DATA);
        }
        AccessRestriction accessRestriction = AccessRestriction.builder()
                .domain(domain)
                .app(accessRestrictionCreationRequest.getApp())
                .build();
        return accessRestrictionMapper.toAccessRestrictionDTO(accessRestrictionRepository.save(accessRestriction));
    }

    @Override
    public void deleteAccessRestriction(String id) {
        accessRestrictionRepository.findById(id).ifPresentOrElse(accessRestrictionRepository::delete, () -> {
            throw new AppException(ErrorCode.NO_DATA_EXCEPTION);
        });
    }

    private String extractDomain(String url) {
        try {
            URI uri = new URI(url);
            return uri.getHost().replace("www.", "");
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
    }
}
