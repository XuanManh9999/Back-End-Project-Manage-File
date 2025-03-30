package com.back_end_TN.project_tn.services.common;

import com.back_end_TN.project_tn.dtos.request.ChangePasswordRequest;
import org.springframework.http.ResponseEntity;

public interface CommonService {
    ResponseEntity<?> handleChangePassword(ChangePasswordRequest changePasswordRequest, String token);
}
