package com.back_end_TN.project_tn.services.user.impl;

import com.back_end_TN.project_tn.configs.CloudinaryConfig;
import com.back_end_TN.project_tn.configs.ModelMapperConfig;
import com.back_end_TN.project_tn.configs.security.SecurityBeansConfig;
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
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {
    private final JwtService jwtService;
    private final CloudinaryConfig cloudinaryConfig;
    private final UserEntityRepository userRepository;
    private final SecurityBeansConfig securityBeansConfig;
    private final ModelMapperConfig modelMapperConfig;

    @Override
    public ResponseEntity<CommonResponse> updateUser(
            String phoneNumber,
            Gender gender,
            MultipartFile avatar,
            MultipartFile background,
            String token) {
        try {
            String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
            UserEntity userEntity = userRepository.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            // Cập nhật avatar nếu có
            Optional.ofNullable(avatar)
                    .filter(file -> !file.isEmpty())
                    .ifPresent(file -> {
                        try {
                            Map uploadResult = cloudinaryConfig.cloudinary().uploader().upload(
                                    file.getBytes(),
                                    ObjectUtils.asMap("folder", "QUAN_LY_TAI_FILE", "resource_type", "auto")
                            );
                            userEntity.setAvatar((String) uploadResult.get("secure_url"));
                        } catch (IOException e) {
                            throw new RuntimeException("Error uploading avatar", e);
                        }
                    });



            // Cập nhật avatar nếu có
            Optional.ofNullable(background)
                    .filter(file -> !file.isEmpty())
                    .ifPresent(file -> {
                        try {
                            Map uploadResult = cloudinaryConfig.cloudinary().uploader().upload(
                                    file.getBytes(),
                                    ObjectUtils.asMap("folder", "QUAN_LY_TAI_FILE", "resource_type", "auto")
                            );
                            userEntity.setBackground((String) uploadResult.get("secure_url"));
                        } catch (IOException e) {
                            throw new RuntimeException("Error uploading avatar", e);
                        }
                    });

            // Cập nhật giới tính nếu có
            Optional.ofNullable(gender)
                    .filter(g -> !g.getValue().isEmpty())
                    .ifPresent(g -> userEntity.setGender(gender));

            // Cập nhật số điện thoại nếu có
            Optional.ofNullable(phoneNumber)
                    .filter(p -> !p.isEmpty())
                    .ifPresent(userEntity::setPhoneNumber);

            userRepository.save(userEntity);

            return ResponseEntity.ok()
                    .body(CommonResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message("User updated successfully")
                            .build());

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
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
