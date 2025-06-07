package com.metaverse.workflow.programoutcome.service;

import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.outcomes.UdyamData;
import com.metaverse.workflow.programoutcome.repository.UdyamDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UdyamService {
    @Autowired
    private UdyamDataRepository udyamDataRepository;

    public void saveUdyamDataFromExcel(MultipartFile file) throws DataException {
        try {
            List<UdyamData> msmes = ExcelHelper.excelToUdyamList(file.getInputStream());
            udyamDataRepository.saveAll(msmes);
        } catch (Exception e) {
            throw new DataException(e.getMessage(), "Failed to store Excel data: ", 400);
        }
    }
}
