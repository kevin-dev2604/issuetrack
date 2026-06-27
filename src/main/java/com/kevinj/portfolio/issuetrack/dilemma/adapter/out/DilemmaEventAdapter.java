package com.kevinj.portfolio.issuetrack.dilemma.adapter.out;

import com.google.firebase.messaging.*;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.event.DilemmaEventType;
import com.kevinj.portfolio.issuetrack.dilemma.application.port.DilemmaEventProducerPort;
import com.kevinj.portfolio.issuetrack.global.push.AsyncFcmExecutor;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.JpaUserDeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DilemmaEventAdapter implements DilemmaEventProducerPort {

    private final AsyncFcmExecutor asyncFcmExecutor;
    private final JpaUserDeviceTokenRepository jpaUserDeviceTokenRepository;

    @Override
    public void sendDilemmaOpen(List<String> tokens, Long dilemmaId, String dilemmaTitle, Long createdBy) {
        Map<String, String> data = new HashMap<>();
        data.put("eventType", DilemmaEventType.DILEMMA_CREATED.getValue());
        data.put("dilemmaId", String.valueOf(dilemmaId));
        data.put("createdBy", String.valueOf(createdBy));

        for (String token : tokens) {
            try {
                asyncFcmExecutor.push(
                    token,
                    "Dilemma Opened",
                    String.format("Dilemma '%s' opened.", dilemmaTitle),
                    data
                );
            } catch (FirebaseMessagingException e) {
                if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                    // 💡 중요: 구글이 "이 토큰 이제 없는 주소야!"라고 알려준 것입니다.
                    // 우리 DB로 가서 해당 token이 있는 행(Row)을 즉시 삭제(Delete)해야 합니다.
                    // 지우지 않으면 다음 발송 때도 유효하지 않은 요청을 보내게 되어 리소스가 낭비됩니다.
                    jpaUserDeviceTokenRepository.deleteByToken(token);
                }
            }
        }

    }

    @Override
    public void sendDilemmaEdited(List<String> tokens, Long dilemmaId, String dilemmaTitle, Long createdBy) {

        Map<String, String> data = new HashMap<>();
        data.put("eventType", DilemmaEventType.DILEMMA_EDITED.getValue());
        data.put("dilemmaId", String.valueOf(dilemmaId));
        data.put("createdBy", String.valueOf(createdBy));

        for (String token : tokens) {
            try {
                asyncFcmExecutor.push(
                    token,
                    "Dilemma edited",
                    String.format("Dilemma '%s' edited.", dilemmaTitle),
                    data
                );
            } catch (FirebaseMessagingException e) {
                if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                    // 💡 중요: 구글이 "이 토큰 이제 없는 주소야!"라고 알려준 것입니다.
                    // 우리 DB로 가서 해당 token이 있는 행(Row)을 즉시 삭제(Delete)해야 합니다.
                    // 지우지 않으면 다음 발송 때도 유효하지 않은 요청을 보내게 되어 리소스가 낭비됩니다.
                    jpaUserDeviceTokenRepository.deleteByToken(token);
                }
            }
        }

    }

    @Override
    public void sendDilemmaClosed(List<String> tokens, Long dilemmaId, String dilemmaTitle, Long createdBy) {

        Map<String, String> data = new HashMap<>();
        data.put("eventType", DilemmaEventType.DILEMMA_CLOSED.getValue());
        data.put("dilemmaId", String.valueOf(dilemmaId));
        data.put("createdBy", String.valueOf(createdBy));

        for (String token : tokens) {
            try {
                asyncFcmExecutor.push(
                    token,
                    "Dilemma closed",
                    String.format("Dilemma '%s' closed.", dilemmaTitle),
                    data
                );
            } catch (FirebaseMessagingException e) {
                if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                    // 💡 중요: 구글이 "이 토큰 이제 없는 주소야!"라고 알려준 것입니다.
                    // 우리 DB로 가서 해당 token이 있는 행(Row)을 즉시 삭제(Delete)해야 합니다.
                    // 지우지 않으면 다음 발송 때도 유효하지 않은 요청을 보내게 되어 리소스가 낭비됩니다.
                    jpaUserDeviceTokenRepository.deleteByToken(token);
                }
            }
        }

    }

    @Override
    public void sendDiscussionCreated(List<String> tokens, Long dilemmaId, Long dilemmaDiscussionId, String dilemmaTitle, Long createdBy) {

        Map<String, String> data = new HashMap<>();
        data.put("eventType", DilemmaEventType.DISCUSSION_CREATED.getValue());
        data.put("dilemmaId", String.valueOf(dilemmaId));
        data.put("dilemmaDiscussionId", String.valueOf(dilemmaDiscussionId));
        data.put("createdBy", String.valueOf(createdBy));

        for (String token : tokens) {
            try {
                asyncFcmExecutor.push(
                    token,
                    "Discussion created",
                    String.format("Discussion of Dilemma '%s' created.", dilemmaTitle),
                    data
                );
            } catch (FirebaseMessagingException e) {
                if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                    // 💡 중요: 구글이 "이 토큰 이제 없는 주소야!"라고 알려준 것입니다.
                    // 우리 DB로 가서 해당 token이 있는 행(Row)을 즉시 삭제(Delete)해야 합니다.
                    // 지우지 않으면 다음 발송 때도 유효하지 않은 요청을 보내게 되어 리소스가 낭비됩니다.
                    jpaUserDeviceTokenRepository.deleteByToken(token);
                }
            }
        }
    }

}
