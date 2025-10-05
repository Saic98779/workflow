package com.metaverse.workflow.service;

import com.metaverse.workflow.dto.CentralRampDataDto;
import com.metaverse.workflow.dto.StateRAMPDashbrdDataDto;
import com.metaverse.workflow.encryption.CentralRampData;
import com.metaverse.workflow.repository.CentralRampDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CentralRampDataService {

    @Autowired
    private CentralRampDataRepository centralRampDataRepository;

    public CentralRampData saveCentralRampData(CentralRampDataDto dto) {
        CentralRampData entity = new CentralRampData();
        if (dto.getStateRAMPDashbrdData() != null) {
            for (StateRAMPDashbrdDataDto dashDto : dto.getStateRAMPDashbrdData()) {
                entity.setStatelgdCode(dto.getStatelgdCode());
                entity.setIntervention(dashDto.getIntervention());
                entity.setComponent(dashDto.getComponent());
                entity.setActivity(dashDto.getActivity());
                entity.setYear(dashDto.getYear());
                entity.setQuarter(dashDto.getQuarter());
                entity.setPhysicalTarget(dashDto.getPhysicalTarget());
                entity.setPhysicalAchieved(dashDto.getPhysicalAchieved());
                entity.setFinancialTarget(dashDto.getFinancialTarget());
                entity.setFinancialAchieved(dashDto.getFinancialAchieved());
                entity.setMsmesBenefittedTotal(dashDto.getMsmesBenefittedTotal());
                entity.setMsmesBenefittedWoman(dashDto.getMsmesBenefittedWoman());
                entity.setMsmesBenefittedSC(dashDto.getMsmesBenefittedSC());
                entity.setMsmesBenefittedST(dashDto.getMsmesBenefittedST());
                entity.setMsmesBenefittedOBC(dashDto.getMsmesBenefittedOBC());
            }
        }
        return centralRampDataRepository.save(entity);
    }
}
