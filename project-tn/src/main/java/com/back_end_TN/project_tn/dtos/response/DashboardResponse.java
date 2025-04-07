package com.back_end_TN.project_tn.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {
    private Long quantity_users;
    private Long quantity_file;
}
