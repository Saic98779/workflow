package com.metaverse.workflow.controller;

import com.metaverse.workflow.encryption.EncryptService;
import com.metaverse.workflow.service.CentralRampDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/central-ramp")
@RequiredArgsConstructor
public class CentralRampDataController {

    private final CentralRampDataService centralRampDataService;

    private final EncryptService secureService;


    @PostMapping("/encrypt-data")
    public String sendToB(@RequestBody String centralRampData) throws Exception {
        return secureService.encryptAndSign(centralRampData);
    }
}