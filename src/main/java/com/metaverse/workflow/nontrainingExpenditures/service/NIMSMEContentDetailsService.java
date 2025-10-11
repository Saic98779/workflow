package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.model.NIMSMEContentDetails;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NIMSMEContentDetailsDto;
import com.metaverse.workflow.nontrainingExpenditures.repository.NIMSMEContentDetailsRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NIMSMEContentDetailsService {

    @Autowired
    private NIMSMEContentDetailsRepository repository;

    @Autowired
    private NonTrainingSubActivityRepository subActivityRepository;

    public NIMSMEContentDetailsDto saveContent(NIMSMEContentDetailsDto dto) {
        NonTrainingSubActivity subActivity = subActivityRepository.findById(dto.getSubActivityId())
                .orElseThrow(() -> new RuntimeException("SubActivity not found with id " + dto.getSubActivityId()));

        NIMSMEContentDetails content = new NIMSMEContentDetails();
        content.setContentType(dto.getContentType());
        content.setContentName(dto.getContentName());
        content.setDurationOrPages(dto.getDurationOrPages());
        content.setTopic(dto.getTopic());
        content.setStatus(dto.getStatus());
        content.setDateOfUpload(dto.getDateOfUpload());
        content.setUrl(dto.getUrl());
        content.setNonTrainingSubActivity(subActivity);

        return entityToDto(repository.save(content));
    }


    public List<NIMSMEContentDetailsDto> getAllContent() {
        return repository.findAll().stream().map(c -> entityToDto(c)).toList();
    }

    public NIMSMEContentDetailsDto getContentById(Long id) {
        NIMSMEContentDetails entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with id " + id));
        return entityToDto(entity);
    }


    public NIMSMEContentDetailsDto updateContent(Long id, NIMSMEContentDetailsDto
            dto) {
        return repository.findById(id).map(existing -> {
            existing.setContentType(dto.getContentType());
            existing.setContentName(dto.getContentName());
            existing.setDurationOrPages(dto.getDurationOrPages());
            existing.setTopic(dto.getTopic());
            existing.setStatus(dto.getStatus());
            existing.setDateOfUpload(dto.getDateOfUpload());
            existing.setUrl(dto.getUrl());

            if (dto.getSubActivityId() != null) {
                NonTrainingSubActivity subActivity = subActivityRepository.findById(dto.getSubActivityId())
                        .orElseThrow(() -> new RuntimeException("SubActivity not found with id " + dto.getSubActivityId()));
                existing.setNonTrainingSubActivity(subActivity);
            }

            return entityToDto(repository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Content not found with id " + id));
    }

    public void deleteContent(Long id) {
        repository.deleteById(id);
    }

    public void deleteBySubActivityId(Long subActivityId) {
        repository.deleteByNonTrainingSubActivity_SubActivityId(subActivityId);
    }

    public List<NIMSMEContentDetailsDto> getContentByNonTrainingSubActivityId(Long nonTrainingSubActivityId) {
        List<NIMSMEContentDetails> entity = repository.findByNonTrainingSubActivity_SubActivityId(nonTrainingSubActivityId);
        return entity.stream().map(e -> entityToDto(e)).toList();
    }


    public static NIMSMEContentDetailsDto entityToDto(NIMSMEContentDetails nimsmeContentDetails) {
        NIMSMEContentDetailsDto content = new NIMSMEContentDetailsDto();
        content.setId(nimsmeContentDetails.getId());
        content.setContentType(nimsmeContentDetails.getContentType());
        content.setContentName(nimsmeContentDetails.getContentName());
        content.setDurationOrPages(nimsmeContentDetails.getDurationOrPages());
        content.setTopic(nimsmeContentDetails.getTopic());
        content.setStatus(nimsmeContentDetails.getStatus());
        content.setDateOfUpload(nimsmeContentDetails.getDateOfUpload());
        content.setUrl(nimsmeContentDetails.getUrl());
        content.setSubActivityId(nimsmeContentDetails.getNonTrainingSubActivity().getSubActivityId());
        return content;
    }

}
