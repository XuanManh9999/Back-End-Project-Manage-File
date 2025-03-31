package com.back_end_TN.project_tn.controllers.collection;

import com.back_end_TN.project_tn.dtos.request.CollectionRequest;
import com.back_end_TN.project_tn.dtos.response.CommonResponse;
import com.back_end_TN.project_tn.services.collection.CollectionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/collection")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;


    @GetMapping("/collections")
    public ResponseEntity<?> getAllCollectionsByUser(HttpServletRequest request) {
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

        return collectionService.getCollections(token);
    }


    @GetMapping("/{collectionID}")
    public ResponseEntity<?> getCollectionByID(HttpServletRequest request, @PathVariable Long collectionID) {
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
                            .message("collectionId are required")
                            .build());
        }

        return collectionService.getCollectionById(token, collectionID);
    }



    @PostMapping("")
    public ResponseEntity<?> addCollection(HttpServletRequest request, @RequestBody CollectionRequest collectionRequest) {
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

        if (collectionRequest.getName().isEmpty() ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("name and description are required")
                            .build());
        }

        return collectionService.addCollection(token, collectionRequest);
    }


    @PutMapping("/{collectionId}")
    public ResponseEntity<?> updateCollection(HttpServletRequest request, @RequestBody CollectionRequest collectionRequest, @PathVariable Long collectionId) {
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

        if (collectionRequest.getName().isEmpty() || collectionId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("name/collectionId are required")
                            .build());
        }
        return collectionService.updateCollection(token, collectionId, collectionRequest);
    }


    @DeleteMapping("/{collectionId}")
    public ResponseEntity<?> deleteCollection(HttpServletRequest request, @PathVariable Long collectionId) {
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
        if (collectionId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("collectionId are required")
                            .build());
        }
        return collectionService.deleteCollection(token, collectionId);
    }

}
