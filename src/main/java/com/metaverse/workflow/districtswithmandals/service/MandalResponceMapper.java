package com.metaverse.workflow.districtswithmandals.service;

import com.metaverse.workflow.model.Mandal;
import org.springframework.stereotype.Component;

@Component
public class MandalResponceMapper {

    public static MandalResponse map(Mandal mandal)
    {
        MandalResponse response =  MandalResponse.builder()
                .mandalId(mandal.getMandalId())
                .mandalName(mandal.getMandalName())
                .build();

        return response;
    }

    public static MandalResponse mapMandals(Mandal mandal)
    {
        MandalResponse response =  MandalResponse.builder()
                .mandalId(mandal.getMandalId())
                .mandalName(mandal.getMandalName())
                .build();

        return response;
    }
}
