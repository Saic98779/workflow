package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.agency.service.AgencyResponseMapper;
import com.metaverse.workflow.agency.service.AgencyService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.resouce.service.ResourceResponse;
import com.metaverse.workflow.resouce.service.ResourceService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ResourceExcelGenerator {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private AgencyService agencyService;

    public void exportAgencyResourcesToExcel(HttpServletResponse response, Long agencyId) throws IOException {
        List<ResourceResponse> resources;

        if (agencyId == -1) {
            WorkflowResponse res = resourceService.getResources();
            resources = (List<ResourceResponse>) res.getData();
        } else {
            Agency agency = agencyService.getAgencyById(agencyId);
            resources = AgencyResponseMapper.mapResources(agency.getResources());
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Resources");

        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        HSSFRow headerRow = sheet.createRow(0);
        String[] headers = {
                "Agency", "Organization Name", "Resource Name",
                "Designation", "Specialization", "Mobile Number", "Email", "Is VIP","Resource Id"
        };

        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = 1;
        for (ResourceResponse resource : resources) {
            HSSFRow dataRow = sheet.createRow(rowIndex++);
            setCellValue(dataRow, 0, resource.getAgencyNames() != null ? String.join(", ", resource.getAgencyNames()) : "");
            setCellValue(dataRow, 1, resource.getOrganizationName());
            setCellValue(dataRow, 2, resource.getName());
            setCellValue(dataRow, 3, resource.getDesignation());
            setCellValue(dataRow, 4, resource.getSpecialization());
            setCellValue(dataRow, 5, resource.getMobileNo());
            setCellValue(dataRow, 6, resource.getEmail());
            setCellValue(dataRow, 7, resource.getIsVIP());
            setCellValue(dataRow,8,resource.getResourceId());
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    // ✅ Helper method for clean cell setting and null safety
    private void setCellValue(HSSFRow row, int columnIndex, Object value) {
        HSSFCell cell = row.createCell(columnIndex);
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }
}
