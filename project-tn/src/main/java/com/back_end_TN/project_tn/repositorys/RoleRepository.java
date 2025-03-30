package com.back_end_TN.project_tn.repositorys;

import com.back_end_TN.project_tn.entitys.RoleEntity;
import com.back_end_TN.project_tn.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findRoleEntityByName(Role name);
    Optional<RoleEntity> findByName(String name);
}
