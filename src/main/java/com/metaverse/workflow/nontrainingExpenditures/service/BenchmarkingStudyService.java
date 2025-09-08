package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.BenchmarkingStudyRequest;
import org.springframework.web.multipart.MultipartFile;

public interface BenchmarkingStudyService {
    WorkflowResponse createBenchmarkingStudy(BenchmarkingStudyRequest request, MultipartFile file) throws DataException;
    WorkflowResponse updateBenchmarkingStudy(Long benchmarkingStudyId, BenchmarkingStudyRequest request) throws DataException;
    WorkflowResponse getBenchmarkingStudyBySubActivityId(Long subActivityId) throws DataException;
    WorkflowResponse deleteBenchmarkingStudy(Long benchmarkingStudyId) throws DataException;
    WorkflowResponse getBenchmarkingStudyById(Long benchmarkingStudyId) throws DataException;

}
