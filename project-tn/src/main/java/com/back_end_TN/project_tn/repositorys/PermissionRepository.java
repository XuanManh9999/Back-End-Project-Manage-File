package com.back_end_TN.project_tn.repositorys;

import com.back_end_TN.project_tn.entitys.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}
