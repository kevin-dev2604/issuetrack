package com.kevinj.portfolio.issuetrack.dilemma.application.port;

import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaCreateCommand;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaSearchQuery;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaSearchResponse;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaUserSearchQuery;
import com.kevinj.portfolio.issuetrack.dilemma.domain.DilemmaDomain;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface DilemmaPort {
    void createDilemma(DilemmaCreateCommand createCommand);
    Optional<DilemmaDomain> getDilemma(User user, Long dilemmaId);
    Optional<DilemmaDomain> getDilemmaUnscoped(Long dilemmaId);
    void saveDilemma(DilemmaDomain dilemmaDomain);
    Page<DilemmaSearchResponse> searchUserDilemma(User user, DilemmaUserSearchQuery userSearchQuery);
    Page<DilemmaSearchResponse> searchFullDilemma(DilemmaSearchQuery searchQuery);

    // Warning: Not supported in test environments (e.g., Fake Ports). Use only for the actual running application.
    Optional<User> getDilemmaUser(Long dilemmaId);
}
