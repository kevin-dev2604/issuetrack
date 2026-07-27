package com.kevinj.portfolio.issuetrack.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Slf4j
@Configuration
public class FirebaseConfig {

    // 값이 없을 경우 default로 처리
    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @PostConstruct
    public void init() throws IOException {
        InputStream serviceAccount;

        if (activeProfile.equals("dev") || activeProfile.equals("prod")) {
            String base64Key = System.getenv("FIREBASE_SERVICE_ACCOUNT_BASE64");
            byte[] decodedKey = Base64.getDecoder().decode(base64Key);
            serviceAccount = new ByteArrayInputStream(decodedKey);
        } else {
            // resources 폴더에 둔 JSON 키 파일 읽기
            serviceAccount = new ClassPathResource("firebase-service-account.json").getInputStream();
        }

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

        // FirebaseApp이 아직 초기화되지 않은 경우에만 초기화 진행
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
