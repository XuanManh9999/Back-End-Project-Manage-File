package com.back_end_TN.project_tn.controllers.admin;

import com.back_end_TN.project_tn.dtos.request.UserRequest;
import com.back_end_TN.project_tn.dtos.response.CommonResponse;
import com.back_end_TN.project_tn.services.admin.ManageUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ManageUserController {
    ManageUserService manageUserService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<CommonResponse> getAllUsers (
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset) {
        if (limit < 0 || offset < 0 || limit > 100) {
            limit = 10;
            offset = 0;
        }
        return manageUserService.getAllUsers(limit, offset);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/roles")
    public ResponseEntity<?> getAllRoles () {
        return manageUserService.getAllRoles();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/by-name")
    public ResponseEntity<?> getAllUsersByName (@RequestParam String name) {
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    CommonResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("name is required")
                            .build()
            );
        }
        return manageUserService.getUserByName(name);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/by-id/{userId}")
    public ResponseEntity<CommonResponse> getUserById(@PathVariable Long userId) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return manageUserService.getUserById(userId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<CommonResponse> createUser(@RequestBody UserRequest userRequest) {
        return  manageUserService.addUser(userRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<CommonResponse> updateUser(@RequestBody UserRequest userRequest, @PathVariable Long userId) {
        return manageUserService.updateUser(userRequest, userId);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/by-id/{userId}")
    public ResponseEntity<CommonResponse> deleteUserById(@PathVariable Long userId) {
        return manageUserService.deleteUser(userId);
    }



}
