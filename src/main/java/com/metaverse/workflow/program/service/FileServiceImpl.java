package com.metaverse.workflow.program.service;

import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.FileType;
import com.metaverse.workflow.model.ProgramFilePathInfo;
import com.metaverse.workflow.model.ProgramFileResponse;
import com.metaverse.workflow.model.User;
import com.metaverse.workflow.program.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ProgramService programService;
    private final LoginRepository loginRepository;
    private final ProgramRepository programRepository;

    private static final String BASE_PREFIX = "/home/metaverseedu/public_html/";
    private static final String URL_PREFIX = "https://metaverseedu.in/";

    @Override
    public List<ProgramFileResponse> getAllProgramFilePathsByStatus(FileType fileType, String userId) throws DataException {
        if (fileType == null) {
            return null;
        }

        List<ProgramFilePathInfo> paths = programService.getProgramFileByType(fileType);
        if (paths == null || paths.isEmpty()) {
            return List.of();
        }

        User byUserId = loginRepository.findByUserId(userId)
                .orElseThrow(() -> new DataException("Admin user not found", "ADMIN_NOT_FOUND", 400));

        if (byUserId.getAgency() != null && byUserId.getAgency().getAgencyId() != null) {
            Long agencyId = byUserId.getAgency().getAgencyId();
            List<Long> byAgencyAgencyId = programRepository.findByAgencyAgencyId(agencyId)
                    .stream()
                    .map(info -> info.getProgramId())
                    .toList();

            return paths.stream()
                    .filter(info -> byAgencyAgencyId.contains(info.getProgramId()))
                    .map(info -> new ProgramFileResponse(info.getProgramId(), info.getFileId(), toUrl(info.getFilePath().toAbsolutePath().toString())))
                    .toList();
        }

        return paths.stream()
                .map(info -> new ProgramFileResponse(info.getProgramId(), info.getFileId(), toUrl(info.getFilePath().toAbsolutePath().toString())))
                .toList();
    }

    private String toUrl(String fullPath) {
        return fullPath.startsWith(BASE_PREFIX)
                ? URL_PREFIX + fullPath.substring(BASE_PREFIX.length())
                : fullPath;
    }
}

