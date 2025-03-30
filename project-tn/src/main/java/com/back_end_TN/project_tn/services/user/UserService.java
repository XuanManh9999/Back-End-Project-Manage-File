package com.back_end_TN.project_tn.services.user;

import com.back_end_TN.project_tn.dtos.request.ChangeInfoUserRequest;
import com.back_end_TN.project_tn.dtos.response.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<CommonResponse> updateUser(ChangeInfoUserRequest request, String token);
    ResponseEntity<CommonResponse> getCurrentUser(String token);

}
