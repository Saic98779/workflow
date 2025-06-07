package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.organization.service.OrganizationResponse;
import com.metaverse.workflow.organization.service.OrganizationService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class OrganizationExcelGenerator {

    @Autowired
    private OrganizationService organizationService;

    public void exportOrganizationsToExcel(HttpServletResponse response) throws IOException {
        WorkflowResponse res = organizationService.getOrganizations();
        List<OrganizationResponse> organizations = (List<OrganizationResponse>) res.getData();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Organizations");

        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        // Header row
        HSSFRow headerRow = sheet.createRow(0);
        String[] headers = {
                "Type Of Organization", "Organization Name", "Name Of Director/Promoter/Leader",
                "Contact", "Email", "Address"
        };

        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Data rows
        int rowIndex = 1;
        for (OrganizationResponse org : organizations) {
            HSSFRow dataRow = sheet.createRow(rowIndex++);
            setCellValue(dataRow, 0, org.getOrganizationType());
            setCellValue(dataRow, 1, org.getOrganizationName());
            setCellValue(dataRow, 2, org.getOwnerName());
            setCellValue(dataRow, 3, org.getContactNo());
            setCellValue(dataRow, 4, org.getEmail());
            setCellValue(dataRow, 5, org.getOwnerAddress());
        }

        // Write to response
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    // ✅ Helper method for safe null handling
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
        } else if (value instanceof Date) {
            // Replace this with your utility if needed
            cell.setCellValue(DateUtil.dateToString((Date) value, "dd-MM-yyyy"));
        } else {
            cell.setCellValue(value.toString());
        }
    }
}
