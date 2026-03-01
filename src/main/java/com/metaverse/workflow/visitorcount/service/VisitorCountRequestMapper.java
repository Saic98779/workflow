package com.metaverse.workflow.visitorcount.service;

import com.metaverse.workflow.model.VisitorCount;

public class VisitorCountRequestMapper {

    /**
     * Map VisitorCountRequest to VisitorCount entity
     * @param request the request object
     * @return the mapped VisitorCount entity
     */
    public static VisitorCount map(VisitorCountRequest request) {
        if (request == null) {
            return null;
        }

        return VisitorCount.builder()
                .totalCount(request.getTotalCount())
                .build();
    }

    /**
     * Map VisitorCountRequest to VisitorCount entity with existing ID
     * @param request the request object
     * @param existingVisitorCount the existing visitor count entity
     * @return the mapped VisitorCount entity
     */
    public static VisitorCount map(VisitorCountRequest request, VisitorCount existingVisitorCount) {
        if (request == null) {
            return null;
        }

        existingVisitorCount.setTotalCount(request.getTotalCount());
        return existingVisitorCount;
    }
}

