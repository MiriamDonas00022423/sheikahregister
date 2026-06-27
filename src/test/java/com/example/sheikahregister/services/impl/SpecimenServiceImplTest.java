package com.example.sheikahregister.services.impl;

import com.example.sheikahregister.common.mappers.SpecimenMapper;
import com.example.sheikahregister.domain.dto.request.CreateSpecimenRequest;
import com.example.sheikahregister.domain.dto.request.UpdateSpecimenRequest;
import com.example.sheikahregister.domain.dto.response.PageableResponse;
import com.example.sheikahregister.domain.dto.response.specimen.SpecimenResponse;
import com.example.sheikahregister.domain.entities.Specimen;
import com.example.sheikahregister.exceptions.ResourceNotFoundException;
import com.example.sheikahregister.repositories.SpecimenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecimenServiceImplTest {

    @Mock
    private SpecimenRepository specimenRepository;

    @Mock
    private SpecimenMapper specimenMapper;

    @InjectMocks
    private SpecimenServiceImpl specimenService;

    private UUID specimenId;
    private CreateSpecimenRequest createRequest;
    private UpdateSpecimenRequest updateRequest;
    private Specimen specimenEntity;
    private Specimen updatedSpecimenEntity;
    private SpecimenResponse specimenResponse;

    @BeforeEach
    void setUp() {
        specimenId = UUID.randomUUID();

        createRequest = CreateSpecimenRequest.builder()
                .name("Lynel")
                .region("Akkala")
                .dangerLevel(10)
                .isFriendly(false)
                .build();

        updateRequest = UpdateSpecimenRequest.builder()
                .name("Blue Lynel")
                .region("Hebra")
                .dangerLevel(9)
                .isFriendly(false)
                .build();

        specimenEntity = Specimen.builder()
                .id(specimenId)
                .name("Lynel")
                .region("Akkala")
                .dangerLevel(10)
                .isFriendly(false)
                .build();

        updatedSpecimenEntity = Specimen.builder()
                .id(specimenId)
                .name("Blue Lynel")
                .region("Hebra")
                .dangerLevel(9)
                .isFriendly(false)
                .build();

        specimenResponse = SpecimenResponse.builder()
                .id(specimenId)
                .name("Lynel")
                .region("Akkala")
                .dangerLevel(10)
                .isFriendly(false)
                .build();
    }

    @Test
    void createSpecimen_shouldSaveAndReturnSpecimen() {
        when(specimenMapper.toEntityCreate(createRequest)).thenReturn(specimenEntity);
        when(specimenRepository.save(specimenEntity)).thenReturn(specimenEntity);
        when(specimenMapper.toDto(specimenEntity)).thenReturn(specimenResponse);

        SpecimenResponse result = specimenService.createSpecimen(createRequest);

        assertThat(result).isEqualTo(specimenResponse);

        verify(specimenMapper).toEntityCreate(createRequest);
        verify(specimenRepository).save(specimenEntity);
        verify(specimenMapper).toDto(specimenEntity);
    }

    @Test
    void getSpecimenById_shouldReturnSpecimen_whenSpecimenExists() {
        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(specimenEntity));
        when(specimenMapper.toDto(specimenEntity)).thenReturn(specimenResponse);

        SpecimenResponse result = specimenService.getSpecimenById(specimenId);

        assertThat(result).isEqualTo(specimenResponse);

        verify(specimenRepository).findById(specimenId);
        verify(specimenMapper).toDto(specimenEntity);
    }

    @Test
    void getSpecimenById_shouldThrowException_whenSpecimenDoesNotExist() {
        when(specimenRepository.findById(specimenId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            specimenService.getSpecimenById(specimenId);
        });

        verify(specimenRepository).findById(specimenId);
        verify(specimenMapper, never()).toDto(any());
    }

    @Test
    void getAllSpecimens_shouldReturnPageableResponse_whenSpecimensExist() {
        Page<Specimen> specimenPage = new PageImpl<>(
                List.of(specimenEntity),
                PageRequest.of(0, 5, Sort.by("name").ascending()),
                1
        );

        PageableResponse pageableResponse = PageableResponse.builder()
                .content(List.of(specimenResponse))
                .pageNumber(0)
                .pageSize(5)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(specimenRepository.findAll(any(Pageable.class))).thenReturn(specimenPage);
        when(specimenMapper.toPageableResponse(specimenPage)).thenReturn(pageableResponse);

        PageableResponse result = specimenService.getAllSpecimens(0, 5, "name", "asc");

        assertThat(result).isEqualTo(pageableResponse);

        verify(specimenRepository).findAll(any(Pageable.class));
        verify(specimenMapper).toPageableResponse(specimenPage);
    }

    @Test
    void getAllSpecimens_shouldThrowException_whenPageIsEmpty() {
        Page<Specimen> emptyPage = Page.empty();

        when(specimenRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> {
            specimenService.getAllSpecimens(0, 5, "name", "asc");
        });

        verify(specimenRepository).findAll(any(Pageable.class));
        verify(specimenMapper, never()).toPageableResponse(any());
    }

    @Test
    void updateSpecimen_shouldUpdateAndReturnSpecimen_whenSpecimenExists() {
        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(specimenEntity));
        when(specimenMapper.toDto(specimenEntity)).thenReturn(specimenResponse);
        when(specimenMapper.toEntityUpdate(updateRequest, specimenId)).thenReturn(updatedSpecimenEntity);
        when(specimenRepository.save(updatedSpecimenEntity)).thenReturn(updatedSpecimenEntity);
        when(specimenMapper.toDto(updatedSpecimenEntity)).thenReturn(specimenResponse);

        SpecimenResponse result = specimenService.updateSpecimen(specimenId, updateRequest);

        assertThat(result).isEqualTo(specimenResponse);

        verify(specimenRepository).findById(specimenId);
        verify(specimenMapper).toEntityUpdate(updateRequest, specimenId);
        verify(specimenRepository).save(updatedSpecimenEntity);
    }

    @Test
    void deleteSpecimen_shouldDeleteAndReturnSpecimen_whenSpecimenExists() {
        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(specimenEntity));
        when(specimenMapper.toDto(specimenEntity)).thenReturn(specimenResponse);

        SpecimenResponse result = specimenService.deleteSpecimen(specimenId);

        assertThat(result).isEqualTo(specimenResponse);

        verify(specimenRepository).findById(specimenId);
        verify(specimenRepository).deleteById(specimenId);
    }
}