package com.back_end_TN.project_tn.services.common.impl;

import com.back_end_TN.project_tn.configs.security.SecurityBeansConfig;
import com.back_end_TN.project_tn.dtos.request.ChangePasswordRequest;
import com.back_end_TN.project_tn.dtos.response.CommonResponse;
import com.back_end_TN.project_tn.entitys.UserEntity;
import com.back_end_TN.project_tn.enums.TokenType;
import com.back_end_TN.project_tn.exceptions.customs.NotFoundException;
import com.back_end_TN.project_tn.exceptions.customs.ServerException;
import com.back_end_TN.project_tn.repositorys.UserEntityRepository;
import com.back_end_TN.project_tn.services.common.CommonService;
import com.back_end_TN.project_tn.services.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
    private final JwtService jwtService;
    private final UserEntityRepository userRepository;
    private final SecurityBeansConfig securityBeansConfig;

    @Override
    public ResponseEntity<?> handleChangePassword(ChangePasswordRequest changePasswordRequest, String token) {
        try {
            String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
            Optional<UserEntity> user  = userRepository.findByUsername(username);
            if (user.isPresent()) {
                UserEntity userEntity = user.get();
                // so sanh mat khau
                Boolean isPassword = securityBeansConfig.passwordEncoder().matches(changePasswordRequest.getOldPassword(), userEntity.getPassword());
                if (isPassword) {
                    String newPassword = securityBeansConfig.passwordEncoder().encode(changePasswordRequest.getNewPassword());
                    userEntity.setPassword(newPassword);
                    userRepository.save(userEntity);
                    return ResponseEntity.ok().body(CommonResponse.builder().
                            status(HttpStatus.OK.value()).
                             message("Password changed successfully")
                            .build());
                }else {
                    throw new ServerException("Password does not match");
                }
            }else {
                throw new NotFoundException("User not found");
            }
        }catch (Exception e) {
            throw e;
        }
    }
}
