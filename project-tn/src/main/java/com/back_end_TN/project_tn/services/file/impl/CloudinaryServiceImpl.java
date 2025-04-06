package com.back_end_TN.project_tn.services.file.impl;

import com.back_end_TN.project_tn.configs.CloudinaryConfig;
import com.back_end_TN.project_tn.dtos.request.DeleteFilesRequest;
import com.back_end_TN.project_tn.dtos.request.FileUpdateRequest;
import com.back_end_TN.project_tn.dtos.response.CommonResponse;
import com.back_end_TN.project_tn.entitys.CollectionEntity;
import com.back_end_TN.project_tn.entitys.FileEntity;
import com.back_end_TN.project_tn.entitys.UserEntity;
import com.back_end_TN.project_tn.enums.Active;
import com.back_end_TN.project_tn.enums.TokenType;
import com.back_end_TN.project_tn.exceptions.customs.NotFoundException;
import com.back_end_TN.project_tn.repositorys.CollectionRepository;
import com.back_end_TN.project_tn.repositorys.FileRepository;
import com.back_end_TN.project_tn.repositorys.UserEntityRepository;
import com.back_end_TN.project_tn.services.file.CloudinaryService;
import com.back_end_TN.project_tn.services.security.JwtService;
import com.back_end_TN.project_tn.utils.FileUtils;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CloudinaryServiceImpl implements CloudinaryService {
    private final CloudinaryConfig cloudinaryConfig;
    private final FileRepository fileRepository;
    private final JwtService jwtService;
    private final UserEntityRepository userEntityRepository;
    private final CollectionRepository collectionRepository;

    public ResponseEntity<?> uploadFile(MultipartFile file, UserEntity user, CollectionEntity collection) {
        try {
            String fileHash = FileUtils.getFileHash(file);

            // Kiểm tra trong database xem file đã tồn tại chưa
            Optional<FileEntity> existingFile = fileRepository.findByFileHashAndUser(fileHash, user);
            if (existingFile.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("File " + file.getOriginalFilename() + " đã tồn tại!");
            }

            // Upload file lên Cloudinary với folder và resource_type tự động
            Map uploadResult = cloudinaryConfig.cloudinary().uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "QUAN_LY_TAI_FILE", "resource_type", "auto")
            );
            String fileUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            // Lưu thông tin file vào database
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileHash(fileHash);

            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            }
            fileEntity.setFileName(FileUtils.randomFileName(fileExtension));
            fileEntity.setFileSize(file.getSize());
            fileEntity.setFileType(file.getContentType());
            fileEntity.setCloudinaryUrl(fileUrl);
            fileEntity.setCollection(collection);
            fileEntity.setUser(user);
            fileEntity.setPublicID(publicId);
            fileEntity.setActive(Active.HOAT_DONG);
            fileRepository.save(fileEntity);

            return ResponseEntity.ok("Upload thành công! File URL: " + fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi khi upload file: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<?> uploadFiles(MultipartFile[] files, String token, Long collectionId) {
        String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
        Optional<UserEntity> user = userEntityRepository.findByUsername(username);
        Optional<CollectionEntity> collectionEntity = collectionRepository.findById(collectionId);
        if (!user.isPresent() || !collectionEntity.isPresent()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    CommonResponse
                            .builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Đã xảy ra lỗi bất ngờ từ Server")
                            .build()
            );
        }

        List<String> resultMessages = new ArrayList<>();
        for (MultipartFile file : files) {
            // Gọi uploadFile và lấy kết quả trả về của từng file
            ResponseEntity<?> response = uploadFile(file, user.get(), collectionEntity.get());
            if (response.getStatusCode() == HttpStatus.CONFLICT) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        CommonResponse.builder()
                                .status(HttpStatus.CONFLICT.value())
                                .message("files config!")
                                .data(response.getBody().toString())
                                .build()
                );
            }
            resultMessages.add(response.getBody().toString());
        }
        return ResponseEntity.ok().body(
                CommonResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Upload files success!")
                        .data(resultMessages)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> deleteFile(Long fileId, String token) {
        try {
            // Giả sử bạn đã có phương thức kiểm tra và lấy user từ token
            String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
            Optional<UserEntity> user = userEntityRepository.findByUsername(username);
            if (!user.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(CommonResponse.builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("User not found!")
                                .build());
            }

            // Tìm file trong database theo fileId
            Optional<FileEntity> fileEntityOpt = fileRepository.findByUserAndId(user.get(), fileId);
            if (!fileEntityOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CommonResponse.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .message("File not found!")
                                .build());
            }

            FileEntity fileEntity = fileEntityOpt.get();

            // Lấy public_id của file (đã lưu khi upload)
            String publicId = fileEntity.getPublicID();
            if (publicId == null || publicId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(CommonResponse.builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .message("Public ID is missing!")
                                .build());
            }

            // Xóa file trên Cloudinary
            Map destroyResult = cloudinaryConfig.cloudinary().uploader().destroy(publicId, ObjectUtils.emptyMap());
            // Kiểm tra destroyResult nếu cần (ví dụ: status, result == "ok")

            // Xóa record file khỏi database
            fileRepository.delete(fileEntity);

            return ResponseEntity.ok(CommonResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("File deleted successfully!")
                    .build());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<?> updateFile(Long fileId, FileUpdateRequest request) {
        try {
            Optional<FileEntity> file = fileRepository.findById(fileId);
            if (file.isPresent()) {
                FileEntity fileEntity = file.get();
                fileEntity.setFileName(request.getFileName());
                fileRepository.save(fileEntity);

                return ResponseEntity.ok().body(
                  CommonResponse.builder()
                          .status(HttpStatus.OK.value())
                          .message("File updated successfully!")
                          .build()
                );
            }else {
                throw new NotFoundException("File not found!");
            }
        }catch (Exception ex) {
            throw  ex;
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> deleteFiles(DeleteFilesRequest request, String token) {
        try {
            String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
            Optional<UserEntity> user = userEntityRepository.findByUsername(username);
            if (!user.isPresent()) {
                throw new NotFoundException("User not found!");
            }
            fileRepository.deleteAllByIdAndUser(request.getFileIds(), user.get());
            return ResponseEntity.ok().body(
                    CommonResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message("File deleted successfully!")
                            .build()
            );
        }catch (Exception ex) {
            throw  ex;
        }

    }


}
