package com.metaverse.workflow.common.util;

import com.metaverse.workflow.activity.sevice.ActivityService;
import com.metaverse.workflow.agency.service.AgencyService;
import com.metaverse.workflow.districtswithmandals.service.DistrictService;
import com.metaverse.workflow.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommonUtil {

    private final DistrictService districtService;
    private final ActivityService activityService;
    private final AgencyService agencyService;
    public static Map<Integer, String> districtMap;
    public static Map<Integer, String> mandalMap;
    public static Map<Long, String> activityMap;
    public static Map<Long, String> subActivityMap;
    public static Map<Long, String> agencyMap;

    public void init() {
        List<District> districtList = districtService.getAllDistrictsEntity();
        districtMap = districtList.stream().collect(Collectors.toMap(
                District::getDistrictId,
                District::getDistrictName,
                (existing, replacement) -> existing
        ));

        List<Mandal> mandalList = districtService.getAllMandalsEntity();
        mandalMap = mandalList.stream().collect(Collectors.toMap(
                Mandal::getMandalId,
                Mandal::getMandalName,
                (existing, replacement) -> existing
        ));

        List<Activity> activityList = activityService.getActivityEntities();
        activityMap = activityList.stream().collect(Collectors.toMap(
                Activity::getActivityId,
                Activity::getActivityName,
                (existing, replacement) -> existing
        ));

        List<SubActivity> subActivityList = activityService.getSubActivityEntities();
        subActivityMap = subActivityList.stream().collect(Collectors.toMap(
                SubActivity::getSubActivityId,
                SubActivity::getSubActivityName,
                (existing, replacement) -> existing
        ));

        List<Agency> agencyList = agencyService.getAllAgencies();
        agencyMap = agencyList.stream().collect(Collectors.toMap(
                Agency::getAgencyId,
                Agency::getAgencyName,
                (existing, replacement) -> existing
        ));
    }


}