package com.back_end_TN.project_tn.dtos.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CollectionResponse {
    private Long id;
    private String name;
    private String description;
    private List<FileResponse> files;
    private Date createAt;
    private Date updateAt;
}
