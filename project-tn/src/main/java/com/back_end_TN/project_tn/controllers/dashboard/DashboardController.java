package com.back_end_TN.project_tn.controllers.dashboard;

import com.back_end_TN.project_tn.services.report.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/dashboard")
@ConditionalOnBean(ReportService.class)
public class DashboardController {
    private final ReportService reportService;

    @Autowired
    public DashboardController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getDashboard() {
      return reportService.getDashboard();
    }

}
