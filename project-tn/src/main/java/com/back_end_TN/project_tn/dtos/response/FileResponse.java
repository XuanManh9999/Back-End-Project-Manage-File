package com.back_end_TN.project_tn.dtos.response;

import lombok.Data;

import java.util.Date;

@Data
public class FileResponse {
    private Long id;
    private String fileName;
    private String fileType;
    private String publicID;
    private Long fileSize;
    private String fileHash;
    private Date createAt;
    private Date updateAt;
}
