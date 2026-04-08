package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.OrganizationNotFoundException;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.dto.request.OrganizationRequestDto;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.dto.response.OrganizationResponseDto;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.entity.Organization;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.repository.OrganizationRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationResponseDto createOrganization(OrganizationRequestDto requestDto) {
        Organization organization = Organization.builder()
                .name(requestDto.getName())
                .timezone(requestDto.getTimezone())
                .addressLine1(requestDto.getAddressLine1())
                .addressLine2(requestDto.getAddressLine2())
                .city(requestDto.getCity())
                .state(requestDto.getState())
                .postalCode(requestDto.getPostalCode())
                .country(requestDto.getCountry())
                .phoneNumber(requestDto.getPhoneNumber())
                .contactEmail(requestDto.getContactEmail())
                .isActive(true)
                .build();

        Organization savedOrganization = organizationRepository.save(organization);
        log.info("Created organization with id: {}", savedOrganization.getId());
        return mapToResponseDto(savedOrganization);
    }

    @Override
    public OrganizationResponseDto getOrganizationById(UUID id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException(id));
        log.info("Fetched organization with id: {}", id);
        return mapToResponseDto(organization);
    }

    @Override
    public List<OrganizationResponseDto> getAllOrganizations() {
        List<Organization> organizations = organizationRepository.findAll();
        log.info("Fetched {} organizations", organizations.size());
        return organizations.stream().map(this::mapToResponseDto).toList();
    }

    @Override
    public OrganizationResponseDto updateOrganization(UUID id, OrganizationRequestDto requestDto) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException(id));

        organization.setName(requestDto.getName());
        organization.setTimezone(requestDto.getTimezone());
        organization.setAddressLine1(requestDto.getAddressLine1());
        organization.setAddressLine2(requestDto.getAddressLine2());
        organization.setCity(requestDto.getCity());
        organization.setState(requestDto.getState());
        organization.setPostalCode(requestDto.getPostalCode());
        organization.setCountry(requestDto.getCountry());
        organization.setPhoneNumber(requestDto.getPhoneNumber());
        organization.setContactEmail(requestDto.getContactEmail());

        Organization updatedOrganization = organizationRepository.save(organization);
        log.info("Updated organization with id: {}", id);
        return mapToResponseDto(updatedOrganization);
    }

    @Override
    public void deleteOrganization(UUID id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException(id));

        if (!organization.isActive()) {
            log.warn("Organization with id {} is already inactive", id);
            return;
        }

        organization.setActive(false); // soft delete
        organizationRepository.save(organization);

        log.info("Soft deleted organization with id: {}", id);
    }

    private OrganizationResponseDto mapToResponseDto(Organization organization) {
        return OrganizationResponseDto.builder()
                .id(organization.getId())
                .name(organization.getName())
                .timezone(organization.getTimezone())
                .addressLine1(organization.getAddressLine1())
                .addressLine2(organization.getAddressLine2())
                .city(organization.getCity())
                .state(organization.getState())
                .postalCode(organization.getPostalCode())
                .country(organization.getCountry())
                .phoneNumber(organization.getPhoneNumber())
                .contactEmail(organization.getContactEmail())
                .isActive(organization.isActive()) // boolean now
                .createdAt(organization.getCreatedAt()) // audit fields
                .updatedAt(organization.getUpdatedAt())
                .build();
    }
}

