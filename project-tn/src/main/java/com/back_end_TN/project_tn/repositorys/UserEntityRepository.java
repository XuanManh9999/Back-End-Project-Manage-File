package com.back_end_TN.project_tn.repositorys;

import com.back_end_TN.project_tn.entitys.UserEntity;
import com.back_end_TN.project_tn.enums.Active;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsernameAndEmail(String username, String email);
    Optional<UserEntity> findUserEntityByEmailAndActive(String email, Active active);
}
