package com.back_end_TN.project_tn.services.user.impl;

import com.back_end_TN.project_tn.configs.ModelMapperConfig;
import com.back_end_TN.project_tn.configs.security.SecurityBeansConfig;
import com.back_end_TN.project_tn.dtos.request.ChangeInfoUserRequest;
import com.back_end_TN.project_tn.dtos.response.CommonResponse;
import com.back_end_TN.project_tn.dtos.response.RoleResponseDTO;
import com.back_end_TN.project_tn.dtos.response.UserResponseDTO;
import com.back_end_TN.project_tn.entitys.RoleEntity;
import com.back_end_TN.project_tn.entitys.UserEntity;
import com.back_end_TN.project_tn.entitys.UserRoleEntity;
import com.back_end_TN.project_tn.enums.Gender;
import com.back_end_TN.project_tn.enums.TokenType;
import com.back_end_TN.project_tn.exceptions.customs.NotFoundException;
import com.back_end_TN.project_tn.repositorys.UserEntityRepository;
import com.back_end_TN.project_tn.services.security.JwtService;
import com.back_end_TN.project_tn.services.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {
    private final JwtService jwtService;
    private final UserEntityRepository userRepository;
    private final SecurityBeansConfig securityBeansConfig;
    private final ModelMapperConfig modelMapperConfig;

    @Transactional
    @Override
    public ResponseEntity<CommonResponse> updateUser(ChangeInfoUserRequest request, String token) {
        try {
            String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
            Optional<UserEntity> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                UserEntity userEntity = user.get();
                if (!request.getAvatar().equals("")) {
                    userEntity.setAvatar(request.getAvatar());
                }
//                if (!request.getBirthday().equals("")) {
//                    userEntity.setBirthday(request.getBirthday());
//                }
                if (!request.getGender().equals("")) {
                    userEntity.setGender(Gender.fromValue(request.getGender()));
                }
                if (!request.getPhone_number().equals("")) {
                    userEntity.setPhoneNumber(request.getPhone_number());
                }
                userRepository.save(userEntity);

                UserResponseDTO userResponseDTO = modelMapperConfig.modelMapper().map(userEntity, UserResponseDTO.class);
                return ResponseEntity.ok()
                        .body(CommonResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("User updated successfully")
                                .data(userResponseDTO)
                                .build());
            }else {
                throw new NotFoundException("User not found");
            }
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity<CommonResponse> getCurrentUser(String token) {
        try {
            String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
            Optional<UserEntity> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                UserResponseDTO userResponseDTO = modelMapperConfig.modelMapper().map(user.get(), UserResponseDTO.class);
                userResponseDTO.setCreatedAt(user.get().getCreateAt());
                userResponseDTO.setUpdatedAt(user.get().getUpdateAt());
                List<UserRoleEntity> userRoleEntitys = user.get().getUserRoles();
                List<RoleResponseDTO> RoleResponseDTOs = new ArrayList<>();
                for (UserRoleEntity userRoleEntity : userRoleEntitys) {
                    RoleEntity roleEntity = userRoleEntity.getRoleId();
                    RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
                    roleResponseDTO.setId(roleEntity.getId());
                    roleResponseDTO.setName(roleEntity.getName());
                    roleResponseDTO.setDescRole(roleEntity.getDescRole());
                    roleResponseDTO.setCreatedAt(roleEntity.getCreateAt());
                    roleResponseDTO.setUpdatedAt(roleEntity.getUpdateAt());
                    RoleResponseDTOs.add(roleResponseDTO);
                }

                userResponseDTO.setRoles(RoleResponseDTOs);
                return ResponseEntity.ok().body(CommonResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Get current user Done")
                        .data(userResponseDTO)
                        .build());
            }else {
                throw new NotFoundException("User not found");
            }
        }catch (Exception e) {
            throw e;
        }
    }
}
