package com.back_end_TN.project_tn.repositorys;

import com.back_end_TN.project_tn.entitys.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findOtpEntitiesByEmailAndOtpCode(String email, String otpCode);
}
