package com.back_end_TN.project_tn.repositorys;

import com.back_end_TN.project_tn.entitys.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    boolean existsByFileHash(String fileHash);
}
