package com.back_end_TN.project_tn.components;

import com.back_end_TN.project_tn.configs.security.AppConfig;
import com.back_end_TN.project_tn.configs.security.SecurityBeansConfig;
import com.back_end_TN.project_tn.entitys.Permission;
import com.back_end_TN.project_tn.entitys.RoleEntity;
import com.back_end_TN.project_tn.entitys.UserEntity;
import com.back_end_TN.project_tn.entitys.UserRoleEntity;
import com.back_end_TN.project_tn.enums.Active;
import com.back_end_TN.project_tn.enums.Role;
import com.back_end_TN.project_tn.repositorys.PermissionRepository;
import com.back_end_TN.project_tn.repositorys.RoleRepository;
import com.back_end_TN.project_tn.repositorys.UserEntityRepository;
import com.back_end_TN.project_tn.repositorys.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@ConditionalOnBean(AppConfig.class)
public class DataSeeder implements CommandLineRunner  {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final SecurityBeansConfig securityBeansConfig;
    private final UserEntityRepository userEntityRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public void run(String... args)  {
        seederPermission();
        seederRole();
        createAccountAdmin();
    }

    public void seederPermission () {
        if (permissionRepository.count() == 0) {
            permissionRepository.save(Permission.builder().name("CREATE").build());
            permissionRepository.save(Permission.builder().name("UPDATE").build());
            permissionRepository.save(Permission.builder().name("READ").build());
            permissionRepository.save(Permission.builder().name("DELETE").build());
            permissionRepository.save(Permission.builder().name("FULL_ACCESS").build());
        }
    }

    public void seederRole () {
        if (roleRepository.count() == 0) {
            var roleAdmin = new  RoleEntity();
            var roleClient = new RoleEntity();
            var roleLecturer= new RoleEntity();
            roleAdmin.setName(Role.ROLE_ADMIN);
            roleAdmin.setActive(Active.HOAT_DONG);
            roleClient.setName(Role.ROLE_USER);
            roleClient.setActive(Active.HOAT_DONG);
            roleLecturer.setName(Role.ROLE_LECTURER);
            roleLecturer.setActive(Active.HOAT_DONG);
            roleRepository.save(roleAdmin);
            roleRepository.save(roleClient);
            roleRepository.save(roleLecturer);
        }
    }

    public void createAccountAdmin() {
        Optional<UserEntity> admin = userEntityRepository.findByUsername("admin");
        if (!admin.isPresent()) {
            UserEntity user = new UserEntity();
            user.setUsername("admin");
            String passwordEncrypted = "$2a$10$115OmNgmZtyyIQS01oV12eMqcj019L3f3BXLaFGv7hvATjskPqOJW";
            user.setPassword(passwordEncrypted); // Mã hóa mật khẩu
            user.setActive(Active.HOAT_DONG);
            userEntityRepository.save(user);

            Optional<RoleEntity> roleEntity = roleRepository.findRoleEntityByName(Role.ROLE_ADMIN);
            if (roleEntity.isPresent()) {
                RoleEntity role = roleEntity.get();
                UserRoleEntity userRole = new UserRoleEntity();
                userRole.setRoleId(role);
                userRole.setUserId(user);
                userRoleRepository.save(userRole);
            }
        }
    }




}
