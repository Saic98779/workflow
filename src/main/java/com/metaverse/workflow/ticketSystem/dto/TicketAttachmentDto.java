package com.metaverse.workflow.ticketSystem.dto;


import com.metaverse.workflow.model.TicketAttachment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketAttachmentDto {

    private Long id;
    private String fileName;
    private String filePath;
    private String contentType;

    public TicketAttachmentDto(TicketAttachment attachment) {
        if (attachment != null) {
            this.id = attachment.getId();
            this.fileName = attachment.getFileName();
            this.filePath = attachment.getFilePath();
            this.contentType = attachment.getContentType();
        }
    }
}

