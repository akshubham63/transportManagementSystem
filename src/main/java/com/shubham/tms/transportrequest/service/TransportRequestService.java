package com.shubham.tms.transportrequest.service;

import com.shubham.tms.transportrequest.dto.GetApiResponse;
import com.shubham.tms.transportrequest.dto.TransportRequestBodyDto;
import com.shubham.tms.transportrequest.dto.TransportRequestResponseDto;
import com.shubham.tms.transportrequest.entity.TransportRequest;
import com.shubham.tms.transportrequest.enums.SortDirection;
import com.shubham.tms.transportrequest.enums.TransportRequestStatus;
import com.shubham.tms.transportrequest.exceptions.TransportRequestNotFoundException;
import com.shubham.tms.transportrequest.exceptions.TransportRequestStatusException;
import com.shubham.tms.transportrequest.repository.TransportRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import jakarta.persistence.criteria.Predicate;

@Service
public class TransportRequestService {
    public final TransportRequestRepository transportRequestRepository;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "source", "destination", "material", "quantity", "createdAt", "updatedAt"
    );

    public TransportRequestService(TransportRequestRepository transportRequestRepository) {
        this.transportRequestRepository = transportRequestRepository;
    }

    public TransportRequestResponseDto saveTransportRequest(TransportRequestBodyDto transportRequestBodyDto) {
        TransportRequest transportRequest = TransportRequest.builder()
                .destination(transportRequestBodyDto.getDestination())
                .material(transportRequestBodyDto.getMaterial())
                .priority(transportRequestBodyDto.getPriority())
                .quantity(transportRequestBodyDto.getQuantity())
                .source(transportRequestBodyDto.getSource())
                .build();
        return TransportRequestResponseDto.fromEntity(transportRequestRepository.save(transportRequest));
    }

    public GetApiResponse<TransportRequestResponseDto> getAllTransportRequest(
            Integer page, Integer limit, TransportRequestStatus status, Integer priority, String source, String destination, String material, Double quantity, Date createdAt, Date updatedAt, SortDirection sortDirection, String sortBy
    ) {
        String finalSortBy;
        if(sortBy == null) {
            finalSortBy = "createdAt";
        } else if (ALLOWED_SORT_FIELDS.contains(sortBy)) {
            finalSortBy = sortBy;
        } else {
            throw new IllegalArgumentException("Invalid sort field: '" + sortBy +
                    "'. Allowed fields are: " + ALLOWED_SORT_FIELDS);
        }
        Sort sort = Sort.by(
                sortDirection != null && sortDirection.equals(SortDirection.DESC)? Sort.Direction.DESC : Sort.Direction.ASC,
                finalSortBy
        );
        Specification<TransportRequest> spec = buildSpecification(status, priority, source, destination, material, quantity, createdAt, updatedAt);
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<TransportRequest> transportRequestList = transportRequestRepository.findAll(spec, pageable);
        List<TransportRequestResponseDto> transportRequestResponseDtoList = transportRequestList.stream()
                .map(TransportRequestResponseDto::fromEntity)
                .toList();
        return GetApiResponse.<TransportRequestResponseDto>builder()
                    .count(transportRequestResponseDtoList.size())
                    .totalRecords(transportRequestList.getTotalElements())
                    .data(transportRequestResponseDtoList)
                    .build();
    }

    public TransportRequestResponseDto getTransportRequestById(UUID id) {
        TransportRequest transportRequest = transportRequestRepository.findById(id)
                .orElseThrow(() -> new TransportRequestNotFoundException("Transport request with id: " + id + " not found"));
        return TransportRequestResponseDto.fromEntity(transportRequest);
    }

    public TransportRequestResponseDto updateTransportRequestById(UUID id, TransportRequestBodyDto transportRequestBodyDto) {
        TransportRequest transportRequest = transportRequestRepository.findById(id)
                .orElseThrow(() -> new TransportRequestNotFoundException("Transport request with id: " + id + " not found"));
        if(!transportRequest.getStatus().equals(TransportRequestStatus.CREATED)) {
            throw new TransportRequestStatusException("Transport request with id: " + id + " is under process and cannot be updated!!!");
        }
        transportRequest.setDestination(transportRequestBodyDto.getDestination());
        transportRequest.setPriority(transportRequestBodyDto.getPriority());
        transportRequest.setMaterial(transportRequestBodyDto.getMaterial());
        transportRequest.setQuantity(transportRequestBodyDto.getQuantity());
        transportRequest.setSource(transportRequestBodyDto.getSource());

        TransportRequest updatedTransportRequest = transportRequestRepository.save(transportRequest);
        return TransportRequestResponseDto.fromEntity(updatedTransportRequest);
    }

    public void deleteTransportRequest(UUID id) {
        TransportRequest transportRequest = transportRequestRepository.findById(id)
                .orElseThrow(() -> new TransportRequestNotFoundException("Transport request with id: " + id + " not found"));
        if(!transportRequest.getStatus().equals(TransportRequestStatus.CREATED)) {
            throw new TransportRequestStatusException("Transport request with id: " + id + " is under process and cannot be updated!!!");
        }
        transportRequestRepository.delete(transportRequest);
    }

    private Specification<TransportRequest> buildSpecification(
            TransportRequestStatus status, Integer priority, String source, String destination, String material, Double quantity, Date createdAt, Date updatedAt
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(source != null) {
                predicates.add(criteriaBuilder.equal(root.get("source"), source));
            }
            if(destination != null) {
                predicates.add(criteriaBuilder.equal(root.get("destination"), destination));
            }
            if(material != null) {
                predicates.add(criteriaBuilder.equal(root.get("material"), material));
            }
            if(quantity != null) {
                predicates.add(criteriaBuilder.equal(root.get("quantity"), quantity));
            }
            if(createdAt != null) {
                predicates.add(criteriaBuilder.equal(root.get("createdAt"), createdAt));
            }
            if(updatedAt != null) {
                predicates.add(criteriaBuilder.equal(root.get("updatedAt"), updatedAt));
            }

            if(status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if(priority != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), priority));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
