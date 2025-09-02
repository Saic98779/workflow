package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.metaverse.workflow.nontrainingExpenditures.Dto.StaffDto;
import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingResourceDTO;
import com.metaverse.workflow.nontrainingExpenditures.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService resourceService;


    @PostMapping(path = "/save")
    public ResponseEntity<NonTrainingResourceDTO> save(@RequestBody StaffDto dto) {
        return ResponseEntity.ok(resourceService.save(dto));
    }


}
