package com.back_end_TN.project_tn.dtos.request;

import lombok.Data;

import java.util.List;

@Data
public class DeleteFilesRequest {
    private List<Long> fileIds;
}
