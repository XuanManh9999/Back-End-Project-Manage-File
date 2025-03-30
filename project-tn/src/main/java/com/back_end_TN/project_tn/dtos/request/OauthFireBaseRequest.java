package com.back_end_TN.project_tn.dtos.request;

import lombok.Data;

@Data
public class OauthFireBaseRequest {
    private String id_token;
    private String email;
    private String provider;
    private String username;
    private String avatar;
}
