package com.metaverse.workflow.trainingtarget.service;

import com.metaverse.workflow.trainingtarget.dtos.TargetsAndAchievementsResponseDto;

import java.util.List;

public interface TargetsAndAchievementsService {
    List<TargetsAndAchievementsResponseDto> getTargetsAndAchievements(Long agencyId);
}
