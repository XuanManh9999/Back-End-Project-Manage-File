package com.back_end_TN.project_tn.repositorys;

import com.back_end_TN.project_tn.entitys.RoleEntity;
import com.back_end_TN.project_tn.entitys.UserEntity;
import com.back_end_TN.project_tn.entitys.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM UserRoleEntity ur WHERE ur.userId.id = :userId")
    void deleteUserRolesByUserId(Long userId);
    Optional<UserRoleEntity> findUserRoleEntitiesByUserIdAndRoleId(UserEntity user, RoleEntity role);
}
