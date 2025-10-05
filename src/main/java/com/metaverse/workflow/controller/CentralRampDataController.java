package com.metaverse.workflow.controller;

import com.metaverse.workflow.dto.CentralRampDataDto;
import com.metaverse.workflow.encryption.CentralRampData;
import com.metaverse.workflow.encryption.Employee;
import com.metaverse.workflow.encryption.SecureServiceA;
import com.metaverse.workflow.service.CentralRampDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/central-ramp")
@RequiredArgsConstructor
public class CentralRampDataController {

    private final CentralRampDataService centralRampDataService;

    private final SecureServiceA secureService;


    @PostMapping("api/data")
    public ResponseEntity<String> insertCentralRampData(@RequestBody String data,
                                                        @RequestHeader("username") String username,
                                                        @RequestHeader("apikey") String apikey) throws Exception {
        CentralRampDataDto centralRampDataDto = secureService.decryptAndVerify(data);
        CentralRampData centralRampData = centralRampDataService.saveCentralRampData(centralRampDataDto);
        if(centralRampData == null){
            return ResponseEntity.status(500).body("Failed to save data");
        }
        return ResponseEntity.ok("Data received and processed successfully");
    }

    @PostMapping("/encrypt-data")
    public String sendToB(@RequestBody CentralRampDataDto centralRampData) throws Exception {
        return secureService.encryptAndSign(centralRampData);
    }
}