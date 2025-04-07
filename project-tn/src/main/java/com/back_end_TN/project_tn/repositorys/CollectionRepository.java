package com.back_end_TN.project_tn.repositorys;

import com.back_end_TN.project_tn.entitys.CollectionEntity;
import com.back_end_TN.project_tn.entitys.UserEntity;
import com.back_end_TN.project_tn.enums.Active;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {
    List<CollectionEntity> findAllByActive(Active active);
    List<CollectionEntity> findAllByActiveAndUser(Active active, UserEntity user);
    Optional<CollectionEntity> findByUserAndIdAndActive(UserEntity user, Long id, Active active);

    Optional<CollectionEntity> findByNameAndActiveAndUser(String name, Active active, UserEntity user);
}
