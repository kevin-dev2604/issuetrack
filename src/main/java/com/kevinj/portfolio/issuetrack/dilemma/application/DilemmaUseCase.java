package com.kevinj.portfolio.issuetrack.dilemma.application;

import com.kevinj.portfolio.issuetrack.dilemma.application.dto.*;
import org.springframework.data.domain.Page;

public interface DilemmaUseCase {
    // USER use case
    void openDilemma(Long userId, DilemmaCreateCommand createCommand);
    void editDilemma(Long userId, DilemmaEditCommand editCommand);
    Page<DilemmaSearchResponse> searchMyDilemmaList(Long userId, DilemmaUserSearchQuery userSearchQuery);
    void createDiscussion(Long userId, DilemmaDiscussionCreateCommand createCommand);
    void editDiscussion(Long userId, DilemmaDiscussionEditCommand editCommand);
    void deleteDiscussion(Long userId, DilemmaDiscussionDeleteCommand deleteCommand);

    // DILEMMA use case
    void closeDilemma(Long dilemmaId);
    Page<DilemmaSearchResponse> searchFullDilemmaList(DilemmaSearchQuery searchQuery);

    // for everyone
    DilemmaDetailResponse getDilemmaDetailInfo(Long dilemmaId);

}
