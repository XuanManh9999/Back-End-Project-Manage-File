package com.back_end_TN.project_tn.services.user.impl;

import com.back_end_TN.project_tn.repositorys.FileRepository;
import com.back_end_TN.project_tn.services.user.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FireServiceImpl implements FileService {
    private final FileRepository fileRepository;
    @Override
    public boolean isDuplicateFile(String hash) {
        return fileRepository.existsByFileHash(hash);
    }
}
