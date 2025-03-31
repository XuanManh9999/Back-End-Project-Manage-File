package com.back_end_TN.project_tn.dtos.request;

import com.back_end_TN.project_tn.enums.Gender;
import lombok.Data;

@Data
public class ChangeInfoUserRequest {
    private String phone_number;
    private String gender;
    private String avatar;

}
