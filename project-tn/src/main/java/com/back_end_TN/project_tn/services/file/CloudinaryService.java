package com.back_end_TN.project_tn.services.file;

import com.back_end_TN.project_tn.dtos.request.DeleteFilesRequest;
import com.back_end_TN.project_tn.dtos.request.FileUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    ResponseEntity<?> uploadFiles (MultipartFile[] files, String token, Long collectionId);
    ResponseEntity<?> deleteFile(Long fileId, String token);
    ResponseEntity<?> updateFile(Long fileId, FileUpdateRequest request);
    ResponseEntity<?> deleteFiles(DeleteFilesRequest request, String token);
}
