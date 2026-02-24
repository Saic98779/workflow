package com.metaverse.workflow.ramp.repository;

import com.metaverse.workflow.model.RampEnrollment;
import com.metaverse.workflow.ramp.service.DistrictProgramReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RampEnrollmentRepository extends JpaRepository<RampEnrollment, Long> {

    @Query(value = """
            SELECT
                l.district AS district_name,
                COUNT(DISTINCT p.program_id) AS total_programs,
                COUNT(DISTINCT CASE
                    WHEN pp.participant_id IS NOT NULL
                    THEN p.program_id
                END) AS total_programs_completed,
                COUNT(DISTINCT CASE
                    WHEN pp.participant_id IS NULL
                    THEN p.program_id
                END) AS programs_scheduled,
                COUNT(DISTINCT pp.participant_id) AS participants,
                COUNT(DISTINCT CASE WHEN pr.gender = 'M' THEN pr.participant_id END) AS male,
                COUNT(DISTINCT CASE WHEN pr.gender = 'F' THEN pr.participant_id END) AS female,
                COUNT(DISTINCT CASE WHEN pr.gender = 'A' THEN pr.participant_id END) AS trans,

                COUNT(DISTINCT CASE WHEN pr.category = 'SC' THEN pr.participant_id END) AS sc,
                COUNT(DISTINCT CASE WHEN pr.category = 'ST' THEN pr.participant_id END) AS st,
                COUNT(DISTINCT CASE WHEN pr.category = 'BC' THEN pr.participant_id END) AS bc,
                COUNT(DISTINCT CASE WHEN pr.category = 'Minority' THEN pr.participant_id END) AS minority,
                COUNT(DISTINCT CASE WHEN pr.category = 'OC' THEN pr.participant_id END) AS oc,

                COUNT(DISTINCT CASE WHEN pr.disability = 'Y' THEN pr.participant_id END) AS disabled,

                COUNT(DISTINCT CASE WHEN org.organization_type = 'SHG' THEN pr.participant_id END) AS shgs,
                COUNT(DISTINCT CASE WHEN org.organization_type = 'SHGT' THEN pr.participant_id END) AS shgts,
                COUNT(DISTINCT CASE WHEN org.organization_type = 'MSME' THEN pr.participant_id END) AS msmes,
                COUNT(DISTINCT CASE WHEN org.organization_type = 'Start Up' THEN pr.participant_id END) AS startups,
                COUNT(DISTINCT CASE WHEN org.organization_type = 'Department' THEN pr.participant_id END) AS others
            FROM program p
            LEFT JOIN location l
                ON l.location_id = p.location_id
            LEFT JOIN program_participant pp
                ON pp.program_id = p.program_id
            LEFT JOIN participant pr
                ON pr.participant_id = pp.participant_id
            LEFT JOIN organization org
                ON org.organization_id = pr.organization_id
            GROUP BY l.district
            ORDER BY district_name;
        """, nativeQuery = true)
    List<DistrictProgramReport> getDistrictProgramReport();
}

