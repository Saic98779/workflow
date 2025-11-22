package com.metaverse.workflow.MoMSMEReport.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.MoMSMEReport.repository.MoMSMEReportRepo;
import com.metaverse.workflow.MoMSMEReport.repository.MoMSMEReportSubmittedRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.dto.CentralRampRequestDto;
import com.metaverse.workflow.encryption.EncryptService;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.MoMSMEReport;
import com.metaverse.workflow.model.MoMSMEReportSubmitted;
import com.metaverse.workflow.model.MoMSMEReportSubmittedMonthly;
import com.metaverse.workflow.model.MoMSMEReportSubmittedQuarterly;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoMSMEReportSubmittedService {
    private final MoMSMEReportSubmittedRepository submittedRepository;
    private final MoMSMEReportRepo moMSMEReportRepo;
    private final com.metaverse.workflow.MoMSMEReport.repository.MoMSMEReportSubmittedMonthlyRepository monthlyRepo;
    private final com.metaverse.workflow.MoMSMEReport.repository.MoMSMEReportSubmittedQuarterlyRepository quarterlyRepo;
    private final EncryptService encryptService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String MOMSME_URL = "https://ramp.msme.gov.in/ramp_staging/api/recieve.php";
    private static final String USERNAME = "Telangana";
    private static final String API_KEY = "Telanganakey";
    private static final int STATE_CODE = 36;

    @Transactional
    public WorkflowResponse saveReport(MoMSMEReportSubmittedDto dto) throws DataException {

        // Step 1: Validate MSME Report reference
        MoMSMEReport moMSMEReport = moMSMEReportRepo.findById(dto.getMoMSMEActivityId())
                .orElseThrow(() -> new DataException("MoMSME Report not found", "MOMSME-REPORT-DATA-NOT-FOUND", 400));

        // Step 2: Get or create parent record
        MoMSMEReportSubmitted parent = submittedRepository.findByFinancialYearAndMoMSMEReport_MoMSMEActivityId(
                        dto.getFinancialYear(), dto.getMoMSMEActivityId())
                .orElse(MoMSMEReportSubmitted.builder()
                        .financialYear(dto.getFinancialYear())
                        .moMSMEReport(moMSMEReport)
                        .build());

        MoMSMEReportSubmitted savedParent = submittedRepository.save(parent);

        // Step 3: Save or update Monthly record
        MoMSMEReportSubmittedMonthly monthly = monthlyRepo
                .findByMoMSMEReportSubmitted_SubmittedIdAndMonth(savedParent.getSubmittedId(), dto.getMonth())
                .orElse(MoMSMEReportSubmittedMonthly.builder()
                        .moMSMEReportSubmitted(savedParent)
                        .month(dto.getMonth())
                        .moMSMEReport(moMSMEReport)
                        .moMSMEReport(moMSMEReport)
                        .month(dto.getMonth())
                        .build());

        monthly.setPhysicalAchievement(dto.getPhysicalAchievement());
        monthly.setFinancialAchievement(dto.getFinancialAchievement());
        monthly.setTotal(dto.getTotal());
        monthly.setWomen(dto.getWomen());
        monthly.setSc(dto.getSc());
        monthly.setSt(dto.getSt());
        monthly.setObc(dto.getObc());

        monthlyRepo.save(monthly);

        String quarter = getQuarterForMonth(dto.getMonth());

        List<MoMSMEReportSubmittedMonthly> quarterlyMonths =
                monthlyRepo.findByMoMSMEReportSubmitted_SubmittedId(savedParent.getSubmittedId())
                        .stream()
                        .filter(m -> getQuarterForMonth(m.getMonth()).equals(quarter))
                        .toList();

        double totalPhysical = quarterlyMonths.stream()
                .mapToDouble(m -> m.getPhysicalAchievement() != null ? m.getPhysicalAchievement() : 0)
                .sum();

        double totalFinancial = quarterlyMonths.stream()
                .mapToDouble(m -> m.getFinancialAchievement() != null ? m.getFinancialAchievement() : 0)
                .sum();

        int totalBeneficiaries = quarterlyMonths.stream()
                .mapToInt(m -> m.getTotal() != null ? m.getTotal() : 0)
                .sum();

        int totalWomen = quarterlyMonths.stream()
                .mapToInt(m -> m.getWomen() != null ? m.getWomen() : 0)
                .sum();

        int totalSc = quarterlyMonths.stream()
                .mapToInt(m -> m.getSc() != null ? m.getSc() : 0)
                .sum();

        int totalSt = quarterlyMonths.stream()
                .mapToInt(m -> m.getSt() != null ? m.getSt() : 0)
                .sum();

        int totalObc = quarterlyMonths.stream()
                .mapToInt(m -> m.getObc() != null ? m.getObc() : 0)
                .sum();

        MoMSMEReportSubmittedQuarterly quarterly = quarterlyRepo
                .findByMoMSMEReportSubmitted_SubmittedIdAndQuarter(savedParent.getSubmittedId(), quarter)
                .orElse(MoMSMEReportSubmittedQuarterly.builder()
                        .moMSMEReportSubmitted(savedParent)
                        .moMSMEReport(moMSMEReport)
                        .moMSMEReport(moMSMEReport)
                        .quarter(quarter)
                        .build());

        quarterly.setPhysicalAchievement(totalPhysical);
        quarterly.setFinancialAchievement(totalFinancial);
        quarterly.setTotal(totalBeneficiaries);
        quarterly.setWomen(totalWomen);
        quarterly.setSc(totalSc);
        quarterly.setSt(totalSt);
        quarterly.setObc(totalObc);

        MoMSMEReportSubmittedQuarterly savedQuarterly = quarterlyRepo.save(quarterly);

        savedParent.setQuarterlyReport(savedQuarterly);
        submittedRepository.save(savedParent);

        return WorkflowResponse.builder()
                .data(MoMSMEReportSubmittedMapper.toDTO(savedParent))
                .message("Monthly and Quarterly data updated successfully")
                .status(200)
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException { MoMSMEReportSubmittedDto dto = submittedRepository.findById(id) .map(MoMSMEReportSubmittedMapper::toDTO) .orElseThrow(() -> new DataException("MoMSMEReport not found with id " + id,"MO-MSME_REPORT_NOT_FOUND ",400)); return WorkflowResponse.builder() .status(200) .message("Submission retrieved successfully.") .data(dto) .build(); }

    public WorkflowResponse getMonthlyReport(Long moMSMEActivityId, String financialYear, String month) throws DataException {

        List<MoMSMEReport> reports;

        if (moMSMEActivityId == -1) {
            // Fetch all reports
            reports = moMSMEReportRepo.findAll();
            if (reports.isEmpty()) {
                throw new DataException(
                        "No MoMSME Reports found in the system",
                        "MO-MSME_REPORT_NOT_FOUND",
                        404);
            }
        } else {
            MoMSMEReport singleReport = moMSMEReportRepo.findById(moMSMEActivityId)
                    .orElseThrow(() -> new DataException(
                            "MoMSME Report not found for ID: " + moMSMEActivityId,
                            "MO-MSME_REPORT_NOT_FOUND",
                            400));
            reports = List.of(singleReport);
        }

        List<MoMSMEReportDto> dtoList = reports.stream()
                .map(entity -> MoMSMEReportSubmittedMapper.MoMSMEReportMapper.toDTOReport(entity, month, financialYear))
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Monthly report(s) retrieved successfully with quarterly achievements.")
                .data(dtoList)
                .totalElements(dtoList.size())
                .build();
    }

    public WorkflowResponse getMonthlyReportByQuarter(Long moMSMEActivityId, String financialYear, String quarter) throws DataException {

        List<MoMSMEReport> reports;
        if(moMSMEActivityId == -1 ){
            reports = moMSMEReportRepo.findAll();
            if (reports.isEmpty()) {
                throw new DataException(
                        "No MoMSME Reports found for financial year: " + financialYear,
                        "MO-MSME_REPORT_NOT_FOUND",
                        404);
            }

        }else {
            Optional<MoMSMEReport> entity = Optional.ofNullable(moMSMEReportRepo.findById(moMSMEActivityId)
                    .orElseThrow(() -> new DataException(
                            "MoMSME Report not found for the given criteria",
                            "MO-MSME_REPORT_NOT_FOUND",
                            400)));
            reports = List.of(entity.get());
        }
        List<MoMSMEReportDto> dtoList = reports.stream()
                .map(entity -> MoMSMEReportSubmittedMapper.MoMSMEReportMapper.toDTOReportByQuarter(entity, quarter, financialYear))
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Quarterly report retrieved successfully with quarterly achievements.")
                .data(dtoList)
                .build();
    }

    public WorkflowResponse getCumulativeReport(Long moMSMEActivityId, String financialYear) throws DataException {

        List<MoMSMEReport> all = new ArrayList<>();

        if (Long.valueOf(-1).equals(moMSMEActivityId)) {
            all = moMSMEReportRepo.findAll();
        } else {
            MoMSMEReport entity = moMSMEReportRepo.findById(moMSMEActivityId)
                    .orElseThrow(() -> new DataException(
                            "MoMSME Report not found for the given criteria",
                            "MO-MSME_REPORT_NOT_FOUND",
                            400));
            all.add(entity);
        }

        // Map to DTO with cumulative data
        List<MoMSMEReportDto> reportDtos = all.stream()
                .map(report -> MoMSMEReportSubmittedMapper.MoMSMEReportMapper
                        .toDTOReportByCumulative(report, financialYear))
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Cumulative report generated successfully.")
                .data(reportDtos)
                .build();
    }

    public WorkflowResponse getAllIntervention() throws DataException {
        List<MoMSMEReport> moMSMEReports = moMSMEReportRepo.findAll();

        if (moMSMEReports.isEmpty()) {
            throw new DataException("Intervention not found", "INTERVENTION_NOT_FOUND", 400);
        }

        // Map only id and name (use HashMap to avoid type inference issues)
        List<Map<String, Object>> interventions = moMSMEReports.stream()
                .map(report -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("moMSMEActivityId", report.getMoMSMEActivityId());
                    map.put("intervention", report.getIntervention());
                    return map;
                })
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Interventions retrieved successfully.")
                .data(interventions)
                .build();
    }

    // ------------------ New dropdown helper methods ------------------

    public WorkflowResponse getUniqueInterventions() throws DataException {
        List<String> interventions = moMSMEReportRepo.findDistinctInterventions();
        if (interventions == null || interventions.isEmpty()) {
            throw new DataException("Intervention not found", "INTERVENTION_NOT_FOUND", 400);
        }
        return WorkflowResponse.builder()
                .status(200)
                .message("Unique interventions retrieved successfully.")
                .data(interventions)
                .totalElements(interventions.size())
                .build();
    }

    public WorkflowResponse getComponentsByIntervention(String intervention) throws DataException {
        if (intervention == null || intervention.trim().isEmpty()) {
            throw new DataException("Intervention must be provided", "INTERVENTION_REQUIRED", 400);
        }
        List<String> components = moMSMEReportRepo.findDistinctComponentsByIntervention(intervention);
        if (components == null || components.isEmpty()) {
            throw new DataException("Components not found for intervention: " + intervention, "COMPONENTS_NOT_FOUND", 400);
        }
        return WorkflowResponse.builder()
                .status(200)
                .message("Components retrieved successfully for intervention: " + intervention)
                .data(components)
                .totalElements(components.size())
                .build();
    }

    public WorkflowResponse getActivitiesByComponent(String component) throws DataException {
        if (component == null || component.trim().isEmpty()) {
            throw new DataException("Component must be provided", "COMPONENT_REQUIRED", 400);
        }
        List<Object[]> rows = moMSMEReportRepo.findDistinctActivityAndMoMSMEActivityIdByComponent(component);
        if (rows == null || rows.isEmpty()) {
            throw new DataException("Activities not found for component: " + component, "ACTIVITIES_NOT_FOUND", 400);
        }

        List<Map<String, Object>> activities = rows.stream()
                .map(r -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("activity", r[0] != null ? String.valueOf(r[0]) : null);
                    m.put("moMSMEActivityId", r[1] != null ? ((Number) r[1]).longValue() : null);
                    return m;
                })
                .collect(Collectors.toList());

        return WorkflowResponse.builder()
                .status(200)
                .message("Activities retrieved successfully for component: " + component)
                .data(activities)
                .totalElements(activities.size())
                .build();
    }

    private String getQuarterForMonth(String month) {
        if (month == null) return null;
        return switch (month.toLowerCase()) {
            case "april", "may", "june" -> "Q1";
            case "july", "august", "september" -> "Q2";
            case "october", "november", "december" -> "Q3";
            case "january", "february", "march" -> "Q4";
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
    }


    /**
     * Main method to handle full flow:
     * 1 Convert request to JSON
     * 2️ Encrypt JSON
     * 3️ Push to MoMSME endpoint
     * 4️ Return result
     */
    public ResponseEntity<?> pushToMoMSME(CentralRampRequestDto requestDto) {
        try {
            // Step 1: Inject static username, apikey, and state
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("username", USERNAME);
            requestMap.put("apikey", API_KEY);

            Map<String, Object> data = new HashMap<>();
            data.put("State", STATE_CODE);
            data.put("StateRAMPDashbrdData", requestDto.getStateRAMPDashbrdData());

            requestMap.put("data", data);

            // Step 2: Convert to JSON
            String jsonData = new ObjectMapper().writeValueAsString(requestMap);

            // Step 3: Encrypt JSON
            String encryptedPayload = encryptPayload(jsonData);

            // Step 4: Send to MoMSME
            ResponseEntity<String> responseEntity = sendToMoMSME(encryptedPayload);

            // Step 5: Return combined response
            return ResponseEntity.ok(buildResponse(encryptedPayload, responseEntity));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    // 🔹 Step 1: Convert DTO to JSON
    private String convertToJson(CentralRampRequestDto requestDto) throws Exception {
        return objectMapper.writeValueAsString(requestDto);
    }

    // 🔹 Step 2: Encrypt JSON using EncryptService
    private String encryptPayload(String jsonData) throws Exception {
        return encryptService.encryptAndSign(jsonData);
    }

    // 🔹 Step 3: Send encrypted payload using OkHttp (Trust-All SSL)
    private ResponseEntity<String> sendToMoMSME(String finalPayload) throws Exception {
        OkHttpClient client = createTrustAllOkHttpClient();

        RequestBody body = RequestBody.create(finalPayload, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(MOMSME_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            return new ResponseEntity<>(responseBody, HttpStatus.valueOf(response.code()));
        }
    }

    // OkHttp Trust-All SSL configuration (for staging)
    private OkHttpClient createTrustAllOkHttpClient() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
        }};

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .connectTimeout(Duration.ofSeconds(15))
                .readTimeout(Duration.ofSeconds(30))
                .build();
    }

    // 🔹 Step 4: Build final response
    private Map<String, Object> buildResponse(String encryptedPayload, ResponseEntity<String> responseEntity) {
        return Map.of(
                "statusCode", responseEntity.getStatusCodeValue(),
                "responseFromMoMSME", responseEntity.getBody()
        );
    }

    // 🔹 Error Response
    private Map<String, Object> createErrorResponse(String message) {
        return Map.of("error", message);
    }
}
