package com.back_end_TN.project_tn.entitys;

import com.back_end_TN.project_tn.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "role")
public class RoleEntity extends BaseEntity<Integer> {
    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true, nullable = false)
    private Role name;
    @Column(name = "desc_role")
    private String descRole;
    @OneToMany(mappedBy = "roleId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoleEntity> userRoles;

    @OneToMany(mappedBy = "role")
    private Set<RoleHasPermission> roleHasPermissions = new HashSet<>();
}
