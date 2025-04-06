package com.back_end_TN.project_tn.repositorys;

import com.back_end_TN.project_tn.entitys.FileEntity;
import com.back_end_TN.project_tn.entitys.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    boolean existsByFileHash(String fileHash);
    Optional<FileEntity> findByFileHashAndUser(String fileHash, UserEntity user);
    Optional<FileEntity> findByUserAndId(UserEntity userEntity, Long id);

    // Sử dụng void nếu không cần trả về kết quả
    @Modifying
    @Transactional
    @Query("DELETE FROM FileEntity f WHERE f.id IN :fileIds AND f.user = :user")
    void deleteAllByIdAndUser(@Param("fileIds") List<Long> fileIds, @Param("user") UserEntity user);
}
