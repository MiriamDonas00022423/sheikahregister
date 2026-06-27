package com.example.sheikahregister.common.mappers;

import com.example.sheikahregister.domain.dto.request.CreateSpecimenRequest;
import com.example.sheikahregister.domain.dto.request.UpdateSpecimenRequest;
import com.example.sheikahregister.domain.dto.response.PageableResponse;
import com.example.sheikahregister.domain.dto.response.specimen.SpecimenResponse;
import com.example.sheikahregister.domain.entities.Specimen;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SpecimenMapper {

    public Specimen toEntityCreate(CreateSpecimenRequest request) {
        return Specimen.builder()
                .name(request.getName())
                .region(request.getRegion())
                .dangerLevel(request.getDangerLevel())
                .isFriendly(request.getIsFriendly())
                .build();
    }

    public Specimen toEntityUpdate(UpdateSpecimenRequest request, UUID id) {
        return Specimen.builder()
                .id(id)
                .name(request.getName())
                .region(request.getRegion())
                .dangerLevel(request.getDangerLevel())
                .isFriendly(request.getIsFriendly())
                .build();
    }

    public SpecimenResponse toDto(Specimen specimen) {
        return SpecimenResponse.builder()
                .id(specimen.getId())
                .name(specimen.getName())
                .region(specimen.getRegion())
                .dangerLevel(specimen.getDangerLevel())
                .isFriendly(specimen.getIsFriendly())
                .build();
    }

    public PageableResponse toPageableResponse(Page<Specimen> specimens) {
        return PageableResponse.builder()
                .content(specimens.map(this::toDto).getContent())
                .pageNumber(specimens.getNumber())
                .pageSize(specimens.getSize())
                .totalElements(specimens.getTotalElements())
                .totalPages(specimens.getTotalPages())
                .first(specimens.isFirst())
                .last(specimens.isLast())
                .build();
    }
}
