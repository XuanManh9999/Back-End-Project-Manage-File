package com.back_end_TN.project_tn.services.user;

import com.back_end_TN.project_tn.dtos.request.ChangeInfoUserRequest;
import com.back_end_TN.project_tn.dtos.response.CommonResponse;
import com.back_end_TN.project_tn.enums.Gender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    ResponseEntity<CommonResponse> updateUser(String phoneNumber, Gender gender, MultipartFile avatar, MultipartFile background, String token) throws IOException;
    ResponseEntity<CommonResponse> getCurrentUser(String token);

}
