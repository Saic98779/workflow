package com.metaverse.workflow.visitorcount.service;

import com.metaverse.workflow.model.VisitorCount;

public class VisitorCountResponseMapper {

    /**
     * Map VisitorCount entity to VisitorCountResponse
     * @param visitorCount the VisitorCount entity
     * @return the mapped VisitorCountResponse
     */
    public static VisitorCountResponse map(VisitorCount visitorCount) {
        if (visitorCount == null) {
            return null;
        }

        return VisitorCountResponse.builder()
                .id(visitorCount.getId())
                .totalCount(visitorCount.getTotalCount())
                .createdOn(visitorCount.getCreatedTimestamp())
                .lastModified(visitorCount.getLastModified())
                .build();
    }
}

