package com.back_end_TN.project_tn.repositorys;

import com.back_end_TN.project_tn.entitys.FileEntity;
import com.back_end_TN.project_tn.entitys.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    boolean existsByFileHash(String fileHash);
    Optional<FileEntity> findByFileHash(String fileHash);
    Optional<FileEntity> findByUserAndId(UserEntity userEntity, Long id);

}
