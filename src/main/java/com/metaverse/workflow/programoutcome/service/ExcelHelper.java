package com.metaverse.workflow.programoutcome.service;

import com.metaverse.workflow.model.outcomes.UdyamData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {

    public static List<UdyamData> excelToUdyamList(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)){

            Sheet sheet = workbook.getSheetAt(0);
            List<UdyamData> list = new ArrayList<>();

            Iterator<Row> rows = sheet.iterator();
            rows.next(); // Skip header

            while (rows.hasNext()) {
                Row row = rows.next();
//UdyogAadharNo	EnterpriseName	PINCode	MobileNo	Gender	SocialCategory	MajorActivity	CreateDate	DISTRICT_NAME	OrganisationType	EnterpriseType
                UdyamData data = UdyamData.builder()
                        .udyamRegistrationNo(getString(row.getCell(0)))
                        .enterpriseName(getString(row.getCell(1)))
                        .PINCode(getInteger(row.getCell(2)))
                        .mobileNo(getLong(row.getCell(3)))
                        .gender(getString(row.getCell(4)))
                        .socialCategory(getString(row.getCell(5)))
                        .majorActivity(getString(row.getCell(6)))
                        .createDate(getDate(row.getCell(7)))
                        .districtName(getString(row.getCell(8)))
                        .organisationType(getString(row.getCell(9)))
                        .enterpriseType(getString(row.getCell(10)))
                        .build();

                list.add(data);
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("Fail to parse Excel: " + e.getMessage());
        }
    }

    private static String getString(Cell cell) {
        return cell == null ? null : cell.toString().trim();
    }

    private static Date getDate(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        } else {
            try {
                return new SimpleDateFormat("dd-MM-yyyy").parse(cell.toString());
            } catch (Exception e) {
                return null;
            }
        }
    }
    private static Integer getInteger(Cell cell) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return (int) cell.getNumericCellValue();
                case STRING:
                    return Integer.parseInt(cell.getStringCellValue().trim());
                case FORMULA:
                    return (int) cell.getNumericCellValue();
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static Long getLong(Cell cell) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return (long) cell.getNumericCellValue();
                case STRING:
                    return Long.parseLong(cell.getStringCellValue().trim());
                case FORMULA:
                    return (long) cell.getNumericCellValue();
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

}
