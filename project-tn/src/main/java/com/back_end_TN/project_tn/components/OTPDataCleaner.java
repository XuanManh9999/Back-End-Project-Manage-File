package com.back_end_TN.project_tn.components;

import com.back_end_TN.project_tn.repositorys.OtpRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ConditionalOnBean(OtpRepository.class)
public class OTPDataCleaner {
    OtpRepository otpRepository;
    @Scheduled(cron = "0 0 0 1 * ?")
    public void cleanOldOTP() {
        otpRepository.deleteAll();
    }
}
