package com.back_end_TN.project_tn.controllers.file;

import com.back_end_TN.project_tn.configs.CloudinaryConfig;
import com.back_end_TN.project_tn.dtos.request.UploadFileRequest;
import com.back_end_TN.project_tn.dtos.response.CommonResponse;
import com.back_end_TN.project_tn.services.file.CloudinaryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    @Autowired
    private CloudinaryService cloudinaryService;



    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFiles(
            @RequestParam(name = "collectionID", required = true) Long collectionID,
            @RequestParam("file") MultipartFile[] files,
            HttpServletRequest request) {
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
        if (collectionID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Collection id is required")
                            .build());
        }

        return cloudinaryService.uploadFiles(files, token, collectionID);
    }



    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(
            @PathVariable Long fileId,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Xác thực token (giả sử token được truyền trong header "Authorization")
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CommonResponse.builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message("Unauthorized")
                            .build());
        }

        // Gọi service xóa file
        return cloudinaryService.deleteFile(fileId, token);
    }

}
