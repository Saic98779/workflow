package com.metaverse.workflow.visitorcount.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.VisitorCount;
import com.metaverse.workflow.visitorcount.repository.VisitorCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class VisitorCountServiceAdapter implements VisitorCountService {

    @Autowired
    private VisitorCountRepository visitorCountRepository;

    private static final Long DEFAULT_ID = 1L;

    @Override
    public WorkflowResponse getVisitorCount() {
        try {
            Optional<VisitorCount> visitorCountOptional = visitorCountRepository.findById(DEFAULT_ID);

            if (visitorCountOptional.isEmpty()) {
                // Initialize if not found
                VisitorCount visitorCount = VisitorCount.builder()
                        .id(DEFAULT_ID)
                        .totalCount(0L)
                        .version(0)
                        .build();
                VisitorCount savedCount = visitorCountRepository.save(visitorCount);
                return WorkflowResponse.builder()
                        .status(200)
                        .message("Visitor count initialized and retrieved successfully")
                        .data(VisitorCountResponseMapper.map(savedCount))
                        .build();
            }

            return WorkflowResponse.builder()
                    .status(200)
                    .message("Visitor count retrieved successfully")
                    .data(VisitorCountResponseMapper.map(visitorCountOptional.get()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return WorkflowResponse.builder()
                    .status(500)
                    .message("Error retrieving visitor count: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public WorkflowResponse updateVisitorCount(VisitorCountRequest request) {
        try {
            Optional<VisitorCount> visitorCountOptional = visitorCountRepository.findById(DEFAULT_ID);

            VisitorCount visitorCount;
            if (visitorCountOptional.isEmpty()) {
                // If record doesn't exist, create new one with initial count of 1
                visitorCount = VisitorCount.builder()
                        .id(DEFAULT_ID)
                        .totalCount(1L)
                        .version(0)
                        .build();
            } else {
                // If record exists, increment the existing count by 1
                visitorCount = visitorCountOptional.get();
                Long currentCount = visitorCount.getTotalCount();
                visitorCount.setTotalCount(currentCount + 1);
            }

            VisitorCount savedCount = visitorCountRepository.save(visitorCount);

            return WorkflowResponse.builder()
                    .status(200)
                    .message("Visitor count updated successfully")
                    .data(VisitorCountResponseMapper.map(savedCount))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return WorkflowResponse.builder()
                    .status(500)
                    .message("Error updating visitor count: " + e.getMessage())
                    .build();
        }
    }
}

