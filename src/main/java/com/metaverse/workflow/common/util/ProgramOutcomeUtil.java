package com.metaverse.workflow.common.util;

import com.metaverse.workflow.model.outcomes.ProgramOutcomeTable;
import com.metaverse.workflow.programoutcome.service.ProgramOutcomeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class ProgramOutcomeUtil {
    @Autowired
    public static ProgramOutcomeService programOutcomeService;

    public static Map<String, String> outcomeTableMap;

    static {
        List<ProgramOutcomeTable> outcomeTableList = programOutcomeService.getProgramOutcomeTables();
        outcomeTableMap = outcomeTableList.stream().collect(Collectors.toMap(table -> table.getOutcomeTableDisplayName(), table -> table.getOutcomeTableName()));
    }

}
