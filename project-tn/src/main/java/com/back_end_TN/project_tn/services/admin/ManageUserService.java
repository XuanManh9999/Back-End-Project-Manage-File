package com.back_end_TN.project_tn.services.admin;

import com.back_end_TN.project_tn.dtos.request.UserRequest;
import com.back_end_TN.project_tn.dtos.response.CommonResponse;
import com.back_end_TN.project_tn.entitys.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface ManageUserService {
    UserDetailsService userDetailsService();
    Optional<UserEntity> findUserByUsername(String username);
    ResponseEntity<CommonResponse> getAllUsers(Integer limit, Integer offset);
    ResponseEntity<CommonResponse> getUserById(Long userId);
    ResponseEntity<CommonResponse> addUser(UserRequest userRequest);
    ResponseEntity<CommonResponse> updateUser(UserRequest userRequest, Long userId);
    ResponseEntity<CommonResponse> deleteUser(Long userId);
}
