package com.back_end_TN.project_tn.controllers.user;

import com.back_end_TN.project_tn.dtos.response.CommonResponse;
import com.back_end_TN.project_tn.enums.Gender;
import com.back_end_TN.project_tn.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/current")
    public ResponseEntity<CommonResponse> getCurrentUserByToken (
                                                         HttpServletRequest request
    ) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CommonResponse.builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                            .build());
        }

        return userService.getCurrentUser(token);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("")
    public ResponseEntity<CommonResponse> updateUser (
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) MultipartFile avatar,
            @RequestParam(required = false) MultipartFile background,
            HttpServletRequest request ) throws IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CommonResponse.builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                            .build());
        }

        return userService.updateUser(phoneNumber, gender, avatar, background, token);
    }





}
