package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.location.service.LocationResponse;
import com.metaverse.workflow.location.service.LocationService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class LocationExcelGenerator {

    @Autowired
    private LocationService locationService;

    public void locationsExportToExcel(HttpServletResponse response, Long locationId) throws IOException {
        List<LocationResponse> locations;

        if (locationId == -1) {
            WorkflowResponse res = locationService.getLocations();
            locations = (List<LocationResponse>) res.getData();
        } else {
            locations = locationService.getLocationByAgencyId(locationId);
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Location Details");

        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        HSSFRow headerRow = sheet.createRow(0);
        String[] headers = {
                "Agency", "Program Location", "District", "Mandal", "Type Of Venue",
                "Max Capacity", "Type Of Ownership", "Date", "Latitude", "Longitude", "Google Map Url"
        };

        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = 1;
        for (LocationResponse res : locations) {
            HSSFRow dataRow = sheet.createRow(rowIndex++);
            setCellValue(dataRow, 0, res.getAgencyName());
            setCellValue(dataRow, 1, res.getLocationName());
            setCellValue(dataRow, 2, res.getDistrict());
            setCellValue(dataRow, 3, res.getMandal());
            setCellValue(dataRow, 4, res.getTypeOfVenue());
            setCellValue(dataRow, 5, res.getCapacity());
            setCellValue(dataRow, 6, res.getOwnershipType());
            setCellValue(dataRow, 7, res.getCreatedOn());
            setCellValue(dataRow, 8, res.getLatitude());
            setCellValue(dataRow, 9, res.getLongitude());
            setCellValue(dataRow, 10, res.getGoogleMapUrl());
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    // Helper method for null-safe value setting
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
            String formattedDate = DateUtil.dateToString((Date) value, "dd-MM-yyyy");
            cell.setCellValue(formattedDate);
        } else {
            cell.setCellValue(value.toString());
        }
    }
}
