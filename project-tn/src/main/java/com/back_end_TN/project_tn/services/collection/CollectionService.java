package com.back_end_TN.project_tn.services.collection;

import com.back_end_TN.project_tn.dtos.request.CollectionRequest;
import org.springframework.http.ResponseEntity;

public interface CollectionService {
    ResponseEntity<?> getCollections(String token);
    ResponseEntity<?> getCollectionById(String token, Long collectionId);
    ResponseEntity<?> addCollection(String token, CollectionRequest collectionRequest);
    ResponseEntity<?> updateCollection(String token, Long collectionID, CollectionRequest collectionRequest);
    ResponseEntity<?> deleteCollection(String token, Long collectionId);
}
