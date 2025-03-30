package com.back_end_TN.project_tn.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class PermissionService {

    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public static boolean isUser() {
        return hasRole("ROLE_USER");
    }

    public static boolean isLecturer() {
        return hasRole("ROLE_LECTURER");
    }

    private static boolean hasRole(String role) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(authRole -> authRole.equals(role));
        }
        return false;
    }
}
