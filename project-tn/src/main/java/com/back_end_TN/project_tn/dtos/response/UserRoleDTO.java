package com.back_end_TN.project_tn.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDTO {
    private Long id;
    private RoleResponseDTO role;
    private UserResponseDTO user;
}
