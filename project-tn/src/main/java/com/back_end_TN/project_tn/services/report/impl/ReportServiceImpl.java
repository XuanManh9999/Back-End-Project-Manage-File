package com.back_end_TN.project_tn.services.report.impl;

import com.back_end_TN.project_tn.dtos.response.DashboardResponse;
import com.back_end_TN.project_tn.repositorys.FileRepository;
import com.back_end_TN.project_tn.repositorys.UserEntityRepository;
import com.back_end_TN.project_tn.services.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final  UserEntityRepository userRepository;
    private final  FileRepository fileRepository;

    @Override
    public ResponseEntity<?> getDashboard() {
        return ResponseEntity.ok()
                .body(DashboardResponse.builder()
                        .quantity_file(fileRepository.count())
                        .quantity_users(userRepository.count())
                        .build());
    }
}

