package com.kevinj.portfolio.issuetrack.global.push;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class AsyncFcmExecutor {

    @Async
    public void push(String token, String title, String body, Map<String, String> data) throws FirebaseMessagingException {
        // 1. 알림창에 보일 시각적 요소 빌드
        Notification notification = Notification.builder()
            .setTitle(title)
            .setBody(body)
            .build();

        // 2. FCM 메시지 객체 생성 (토큰, 알림 내용, 커스텀 데이터 세팅)
        Message message = Message.builder()
            .setToken(token) // 수신 주소 입력
            .setNotification(notification) // 기본 상단 배너 문구
            .putAllData(data) // 프론트엔드가 읽어서 화면을 전환할 딥링크 데이터
            .build();

        // 3. Firebase Admin SDK를 통해 구글 서버로 발송 위임 (비동기 처리 원할 시 sendAsync 사용 가능)
        String response = FirebaseMessaging.getInstance().send(message);
        log.info("FCM 푸시 전송 성공, Message ID: " + response);
    }
}
