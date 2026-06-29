package com.kevinj.portfolio.issuetrack.dilemma.application.port.out;

import com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto.DilemmaCreateCommand;
import com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto.DilemmaSearchQuery;
import com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto.DilemmaSearchResponse;
import com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto.DilemmaUserSearchQuery;
import com.kevinj.portfolio.issuetrack.dilemma.domain.model.DilemmaDomain;
import com.kevinj.portfolio.issuetrack.user.domain.model.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface DilemmaPort {
    Long createDilemma(DilemmaCreateCommand createCommand);
    Optional<DilemmaDomain> getDilemma(User user, Long dilemmaId);
    Optional<DilemmaDomain> getDilemmaUnscoped(Long dilemmaId);
    void saveDilemma(DilemmaDomain dilemmaDomain);
    Page<DilemmaSearchResponse> searchUserDilemma(User user, DilemmaUserSearchQuery userSearchQuery);
    Page<DilemmaSearchResponse> searchFullDilemma(DilemmaSearchQuery searchQuery);

    // Warning: Not supported in test environments (e.g., Fake Ports). Use only for the actual running application.
    Optional<User> getDilemmaUser(Long dilemmaId);
}
