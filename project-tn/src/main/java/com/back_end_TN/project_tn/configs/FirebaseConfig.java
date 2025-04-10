package com.back_end_TN.project_tn.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    @Bean
    public FirebaseAuth firebaseAuth() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                // Đọc file từ classpath thay vì đường dẫn cứng
                InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase.json");

                if (serviceAccount == null) {
                    throw new IOException("Không tìm thấy tệp cấu hình Firebase.");
                }

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
            }
            return FirebaseAuth.getInstance();
        } catch (IOException e) {
            throw new RuntimeException("Không thể khởi tạo Firebase", e);
        }
    }
}