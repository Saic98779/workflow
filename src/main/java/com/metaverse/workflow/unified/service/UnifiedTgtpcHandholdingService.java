package com.metaverse.workflow.unified.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.tgtpc_handholding.request_dto.*;
import com.metaverse.workflow.tgtpc_handholding.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UnifiedTgtpcHandholdingService {

    private final TGTPCHandholdingMasterService tgtpcHandholdingMasterService;
    private final IECRegistrationCertificationService iecService;
    private final LocalTestingLabAttachmentService testingLabAttachmentService;
    private final RawMaterialSupportService rawMaterialSupportService;
    private final TestingQualityCertificationSupportService certificationSupportService;
    private final ObjectMapper mapper = new ObjectMapper();

    public WorkflowResponse save(String type, String data, MultipartFile file) throws DataException {
        try {
            WorkflowResponse response;
            String lower = type == null ? "" : type.trim().toLowerCase();

            switch (lower) {
                case "iecregistrationcertification":
                    IECRegistrationCertificationRequest request =
                            mapper.readValue(data, IECRegistrationCertificationRequest.class);
                    response = iecService.save(request);
                    break;

                case "localtestinglabattachment":
                    LocalTestingLabAttachmentRequest labAttachmentRequest =
                            mapper.readValue(data, LocalTestingLabAttachmentRequest.class);
                    response = testingLabAttachmentService.save(labAttachmentRequest);
                    break;

                case "rawmaterialsupport":
                    RawMaterialSupportRequest materialSupportRequest =
                            mapper.readValue(data, RawMaterialSupportRequest.class);
                    response = rawMaterialSupportService.save(materialSupportRequest);
                    break;

                case "testingqualitycertificationsupport":
                    TestingQualityCertificationSupportRequest certificationSupportRequest =
                            mapper.readValue(data, TestingQualityCertificationSupportRequest.class);
                    response = certificationSupportService.save(certificationSupportRequest, file);
                    break;

                case "packagingstandardssupport":
                case "brandingsupport":
                    TGTPCHandholdingSupportRequest supportRequest =
                            mapper.readValue(data, TGTPCHandholdingSupportRequest.class);
                    response = WorkflowResponse.builder()
                            .data(HandholdingResponseMapper.mapToTGTPCHandholdingSupportResponse(tgtpcHandholdingMasterService.save(supportRequest)))
                            .build();
                    break;

                default:
                    throw new DataException("Invalid type: " + lower, "INVALID_TYPE", 400);
            }

            return response;

        } catch (JsonProcessingException ex) {
            throw new DataException("Invalid JSON format in 'data' parameter", "INVALID_JSON", 400);
        } catch (DataException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataException("Unexpected error: " + ex.getMessage(), "INTERNAL_ERROR", 500);
        }
    }


    public WorkflowResponse get(String type, Long id, Long subActivityId) throws DataException {

        String lower = type == null ? "" : type.trim().toLowerCase();
        WorkflowResponse response;

        boolean hasId = id != null;
        boolean hasSub = subActivityId != null;

        switch (lower) {

            case "iecregistrationcertification":
                if (hasId) {
                    response = iecService.getById(id);
                } else if (hasSub) {
                    response = iecService.getBySubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for iecregistrationcertification", "MISSING_PARAM", 400);
                }
                break;

            case "localtestinglabattachment":
                if (hasId) {
                    response = testingLabAttachmentService.getById(id);
                } else if (hasSub) {
                    response = testingLabAttachmentService.getBySubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for localtestinglabattachment", "MISSING_PARAM", 400);
                }
                break;

            case "rawmaterialsupport":
                if (hasId) {
                    response = rawMaterialSupportService.getById(id);
                } else if (hasSub) {
                    response = rawMaterialSupportService.getBySubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for rawmaterialsupport", "MISSING_PARAM", 400);
                }
                break;

            case "testingqualitycertificationsupport":
                if (hasId) {
                    response = certificationSupportService.getById(id);
                } else if (hasSub) {
                    response = certificationSupportService.getBySubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for testingqualitycertificationsupport", "MISSING_PARAM", 400);
                }
                break;

            case "packagingstandardssupport":
            case "brandingsupport":
                if (hasId) {
                    response = tgtpcHandholdingMasterService.getById(id);
                } else if (hasSub) {
                    response = tgtpcHandholdingMasterService.getBySubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for handholding support", "MISSING_PARAM", 400);
                }
                break;

            default:
                throw new DataException("Unknown type: " + type, "UNKNOWN_TYPE", 400);
        }

        return response;
    }

    public WorkflowResponse update(Long id, String type, String data, MultipartFile file) throws DataException {

        try {
            WorkflowResponse response;
            String lower = type == null ? "" : type.trim().toLowerCase();

            switch (lower) {

                case "iecregistrationcertification":
                    IECRegistrationCertificationRequest iecReq =
                            mapper.readValue(data, IECRegistrationCertificationRequest.class);
                    response = iecService.update(id, iecReq);
                    break;

                case "localtestinglabattachment":
                    LocalTestingLabAttachmentRequest labReq =
                            mapper.readValue(data, LocalTestingLabAttachmentRequest.class);
                    response = testingLabAttachmentService.update(id, labReq);
                    break;

                case "rawmaterialsupport":
                    RawMaterialSupportRequest rawReq =
                            mapper.readValue(data, RawMaterialSupportRequest.class);
                    response = rawMaterialSupportService.update(id, rawReq);
                    break;

                case "testingqualitycertificationsupport":
                    TestingQualityCertificationSupportRequest certReq =
                            mapper.readValue(data, TestingQualityCertificationSupportRequest.class);
                    response = certificationSupportService.update(id, certReq, file);
                    break;

                case "packagingstandardssupport":
                case "brandingsupport":
                    TGTPCHandholdingSupportRequest handReq =
                            mapper.readValue(data, TGTPCHandholdingSupportRequest.class);
                    response = WorkflowResponse.builder()
                            .data(HandholdingResponseMapper.mapToTGTPCHandholdingSupportResponse(tgtpcHandholdingMasterService.update(id, handReq)))
                            .build();
                    break;

                default:
                    throw new DataException("Unknown type for update: " + type, "UNKNOWN_TYPE", 400);
            }

            return response;

        } catch (Exception e) {
            throw new DataException("Failed to parse or update request", "UPDATE_FAILED", 400);
        }
    }

    public WorkflowResponse delete(String type, Long id) throws DataException {

        if (id == null) {
            throw new DataException("Missing id for delete operation", "MISSING_ID", 400);
        }

        String lower = type == null ? "" : type.trim().toLowerCase();

        switch (lower) {

            case "iecregistrationcertification":
                iecService.delete(id);
                break;

            case "localtestinglabattachment":
                testingLabAttachmentService.delete(id);
                break;

            case "rawmaterialsupport":
                rawMaterialSupportService.delete(id);
                break;

            case "testingqualitycertificationsupport":
                certificationSupportService.delete(id);
                break;

            case "packagingstandardssupport":
            case "brandingsupport":
                tgtpcHandholdingMasterService.delete(id);
                break;

            default:
                throw new DataException("Unknown type for delete: " + type, "UNKNOWN_TYPE", 400);
        }

        return WorkflowResponse.builder()
                .status(200)
                .message("Deleted successfully")
                .build();
    }


}
