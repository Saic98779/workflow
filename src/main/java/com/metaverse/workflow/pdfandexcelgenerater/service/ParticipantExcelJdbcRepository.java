package com.metaverse.workflow.pdfandexcelgenerater.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParticipantExcelJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL = """
        SELECT
            ROW_NUMBER() OVER (ORDER BY p.participant_id) AS sno,
            ag.agency_name AS agency_name,
            pr.program_title AS program_name,
            pr.start_date AS start_date,
            pr.end_date AS end_date,
            loc.district AS district,
            CASE
                WHEN p.organization_id IS NULL THEN 'YES'
                ELSE 'NO'
            END AS is_aspirant,
            org.organization_name AS organization_name,
            org.organization_type AS organization_type,
            p.participant_name AS participant_name,
            p.gender AS gender,
            p.category AS category,
            p.disability AS disability,
            p.aadhar_no AS aadhar_no,
            p.mobile_no AS participant_mobile_no,
            p.email AS participant_email,
            p.designation AS participant_designation,
            org.udyam_registration_no AS udyam_registration_no,
            GROUP_CONCAT(DISTINCT sec.sector_name SEPARATOR ', ') AS sectors,
            org.contact_no AS organization_mobile_no,
            org.email AS organization_email,
            org.owner_name AS owner_name,
            org.owner_contact_no AS owner_mobile_no,
            org.owner_email AS owner_email
        FROM participant p
        JOIN program_participant pp ON p.participant_id = pp.participant_id
        JOIN program pr ON pp.program_id = pr.program_id
        LEFT JOIN agency ag ON pr.agency_id = ag.agency_id
        LEFT JOIN location loc ON pr.location_id = loc.location_id
        LEFT JOIN organization org ON p.organization_id = org.organization_id
        LEFT JOIN organization_sector os ON org.organization_id = os.organization_id
        LEFT JOIN sector sec ON os.sector_id = sec.sector_id
        GROUP BY
            p.participant_id,
            ag.agency_name,
            pr.program_title,
            pr.start_date,
            pr.end_date,
            loc.district,
            p.organization_id,
            org.organization_name,
            org.organization_type,
            p.participant_name,
            p.gender,
            p.category,
            p.disability,
            p.aadhar_no,
            p.mobile_no,
            p.email,
            p.designation,
            org.udyam_registration_no,
            org.contact_no,
            org.email,
            org.owner_name,
            org.owner_contact_no,
            org.owner_email
        """;

    public List<ParticipantExcelDto> fetchParticipantExcelData() {
        return jdbcTemplate.query(SQL, new ParticipantExcelRowMapper());
    }

    private static class ParticipantExcelRowMapper implements RowMapper<ParticipantExcelDto> {

        @Override
        public ParticipantExcelDto mapRow(ResultSet rs, int rowNum) throws SQLException {

            ParticipantExcelDto dto = new ParticipantExcelDto();

            dto.setSno(rs.getLong("sno"));
            dto.setAgencyName(rs.getString("agency_name"));
            dto.setProgramName(rs.getString("program_name"));
            dto.setStartDate(rs.getDate("start_date").toLocalDate());
            dto.setEndDate(rs.getDate("end_date").toLocalDate());
            dto.setDistrict(rs.getString("district"));
            dto.setIsAspirant(rs.getString("is_aspirant"));

            dto.setOrganizationName(rs.getString("organization_name"));
            dto.setOrganizationType(rs.getString("organization_type"));

            dto.setParticipantName(rs.getString("participant_name"));
            dto.setGender(rs.getString("gender"));
            dto.setCategory(rs.getString("category"));
            dto.setDisability(rs.getString("disability"));
            dto.setAadharNo(rs.getString("aadhar_no"));
            dto.setParticipantMobileNo(rs.getString("participant_mobile_no"));
            dto.setParticipantEmail(rs.getString("participant_email"));
            dto.setParticipantDesignation(rs.getString("participant_designation"));

            dto.setUdyamRegistrationNo(rs.getString("udyam_registration_no"));
            dto.setSectors(rs.getString("sectors"));

            dto.setOrganizationMobileNo(rs.getString("organization_mobile_no"));
            dto.setOrganizationEmail(rs.getString("organization_email"));
            dto.setOwnerName(rs.getString("owner_name"));
            dto.setOwnerMobileNo(rs.getString("owner_mobile_no"));
            dto.setOwnerEmail(rs.getString("owner_email"));

            return dto;
        }
    }
}
