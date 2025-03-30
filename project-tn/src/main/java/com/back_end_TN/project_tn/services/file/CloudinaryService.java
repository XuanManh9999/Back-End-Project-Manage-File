package com.back_end_TN.project_tn.services.file;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    ResponseEntity<?> uploadFiles (MultipartFile[] files, String token, Long collectionId);
    ResponseEntity<?> deleteFile(Long fileId, String token);
}
