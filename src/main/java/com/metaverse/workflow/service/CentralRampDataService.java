package com.metaverse.workflow.service;

import com.metaverse.workflow.dto.CentralRampRequestDto;
import com.metaverse.workflow.dto.StateRAMPDashbrdDataDto;
import com.metaverse.workflow.encryption.CentralRampData;
import com.metaverse.workflow.repository.CentralRampDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CentralRampDataService {

    @Autowired
    private CentralRampDataRepository centralRampDataRepository;

    public CentralRampData saveCentralRampData(CentralRampRequestDto dto) {
        CentralRampData entity = new CentralRampData();
        if (dto.getData().getStateRAMPDashbrdData() != null && !dto.getData().getStateRAMPDashbrdData().isEmpty()) {
            for (StateRAMPDashbrdDataDto dashDto : dto.getData().getStateRAMPDashbrdData()) {
                entity.setStatelgdCode(dto.getData().getState());
                entity.setIntervention(dashDto.getIntervention());
                entity.setComponent(dashDto.getComponent());
                entity.setActivity(dashDto.getActivity());
                entity.setYear(dashDto.getYear());
                entity.setQuarter(dashDto.getQuarter());
                entity.setPhysicalTarget(dashDto.getPhysicalTarget());
                entity.setPhysicalAchieved(dashDto.getPhysicalAchieved());
                entity.setFinancialTarget(dashDto.getFinancialTarget());
                entity.setFinancialAchieved(dashDto.getFinancialAchieved());
                entity.setMsmesBenefittedTotal(dashDto.getMSMEsBenefittedTotal());
                entity.setMsmesBenefittedWoman(dashDto.getMSMEsBenefittedWoman());
                entity.setMsmesBenefittedSC(dashDto.getMSMEsBenefittedSC());
                entity.setMsmesBenefittedST(dashDto.getMSMEsBenefittedST());
                entity.setMsmesBenefittedOBC(dashDto.getMSMEsBenefittedOBC());
            }
        }
        return centralRampDataRepository.save(entity);
    }
}
