package com.back_end_TN.project_tn.services.collection.impl;

import com.back_end_TN.project_tn.configs.CloudinaryConfig;
import com.back_end_TN.project_tn.configs.ModelMapperConfig;
import com.back_end_TN.project_tn.dtos.request.CollectionRequest;
import com.back_end_TN.project_tn.dtos.response.CollectionResponse;
import com.back_end_TN.project_tn.dtos.response.CommonResponse;
import com.back_end_TN.project_tn.entitys.CollectionEntity;
import com.back_end_TN.project_tn.entitys.UserEntity;
import com.back_end_TN.project_tn.enums.Active;
import com.back_end_TN.project_tn.enums.TokenType;
import com.back_end_TN.project_tn.exceptions.customs.DuplicateResourceException;
import com.back_end_TN.project_tn.exceptions.customs.NotFoundException;
import com.back_end_TN.project_tn.repositorys.CollectionRepository;
import com.back_end_TN.project_tn.repositorys.UserEntityRepository;
import com.back_end_TN.project_tn.services.collection.CollectionService;
import com.back_end_TN.project_tn.services.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    private final CloudinaryConfig cloudinaryConfig;
    private final CollectionRepository collectionRepository;
    private final UserEntityRepository userRepository;
    private final JwtService jwtService;
    private final ModelMapperConfig modelMapperConfig;

    private UserEntity getUserEntityByToken(String token) {
        try {
            String username  = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
            Optional<UserEntity> userEntity = userRepository.findByUsername(username);
            if (userEntity.isPresent()) {
                return userEntity.get();
            }else {
                throw new NotFoundException("User not found");
            }
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getCollections(String token) {
        try {
            UserEntity userEntity = getUserEntityByToken(token);

            List<CollectionEntity> collectionEntityList = collectionRepository.findAllByActiveAndUser(Active.HOAT_DONG, userEntity);
            List<CollectionResponse> collectionResponseList = new ArrayList<>();
            for (CollectionEntity collectionEntity : collectionEntityList) {
                CollectionResponse collectionResponse = modelMapperConfig.modelMapper().map(collectionEntity, CollectionResponse.class);
                collectionResponseList.add(collectionResponse);
            }

            return ResponseEntity.ok(CommonResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(collectionResponseList)
                    .message("Get Collections Success")
                    .build());

        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getCollectionById(String token, Long collectionId) {
        try {
            UserEntity userEntity = getUserEntityByToken(token);
            Optional<CollectionEntity> collectionEntity =  collectionRepository.findByUserAndIdAndActive(userEntity, collectionId, Active.HOAT_DONG);
            if (collectionEntity.isPresent()) {
                CollectionResponse collectionResponse = modelMapperConfig.modelMapper().map(collectionEntity.get(), CollectionResponse.class);
                return ResponseEntity.ok(CommonResponse.builder()
                        .status(HttpStatus.OK.value())
                        .data(collectionResponse)
                        .message("Get Collections Success")
                        .build());
            }else {
                throw new  NotFoundException("Collection not found");
            }
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> addCollection(String token, CollectionRequest collectionRequest) {
        try {
            UserEntity entity = getUserEntityByToken(token);

            Optional<CollectionEntity> collection = collectionRepository.findByNameAndActiveAndUser(collectionRequest.getName(), Active.HOAT_DONG, entity);
            if (collection.isPresent()) {
                throw new DuplicateResourceException("Collection already exists");
            }else {
                UserEntity userEntity = getUserEntityByToken(token);
                CollectionEntity collectionEntity = new CollectionEntity();
                collectionEntity.setName(collectionRequest.getName());
                collectionEntity.setDescription(collectionRequest.getDescription());
                collectionEntity.setActive(Active.HOAT_DONG);
                collectionEntity.setUser(userEntity);
                collectionRepository.save(collectionEntity);
                return ResponseEntity.ok(CommonResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Collection Added Success")
                        .build());
            }
        }catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<?> updateCollection(String token, Long collectionID, CollectionRequest collectionRequest) {
        try {
            UserEntity userEntity = getUserEntityByToken(token);
            Optional<CollectionEntity> collectionEntity =  collectionRepository.findByUserAndIdAndActive(userEntity, collectionID, Active.HOAT_DONG);
            if (collectionEntity.isPresent()) {
                CollectionEntity collection = collectionEntity.get();
                collection.setName(collectionRequest.getName());
                collection.setDescription(collectionRequest.getDescription());
                collection.setUser(userEntity);
                collectionRepository.save(collection);
                return ResponseEntity.ok(CommonResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Update Collection Success")
                        .build());
            }else {
                throw new  NotFoundException("Collection not found");
            }
        }catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<?> deleteCollection(String token, Long collectionId) {
        try {
            UserEntity userEntity = getUserEntityByToken(token);
            Optional<CollectionEntity> collectionEntity =  collectionRepository.findByUserAndIdAndActive(userEntity, collectionId, Active.HOAT_DONG);
            if (collectionEntity.isPresent()) {
                collectionEntity.get().setActive(Active.VO_HIEU_HOA);
                collectionRepository.save(collectionEntity.get());
                return ResponseEntity.ok().body(CommonResponse.builder()
                        .status(HttpStatus.NO_CONTENT.value())
                        .message("Collection Deleted Success")
                        .build());
            }else {
                throw new  NotFoundException("Collection not found");
            }
        }catch (Exception ex) {
            throw ex;
        }
    }

}
