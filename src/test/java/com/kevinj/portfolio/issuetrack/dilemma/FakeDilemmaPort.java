package com.kevinj.portfolio.issuetrack.dilemma;

import com.kevinj.portfolio.issuetrack.FakePort;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaCreateCommand;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaSearchQuery;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaSearchResponse;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaUserSearchQuery;
import com.kevinj.portfolio.issuetrack.dilemma.application.port.DilemmaPort;
import com.kevinj.portfolio.issuetrack.dilemma.domain.DilemmaDomain;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import org.springframework.data.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeDilemmaPort implements DilemmaPort, FakePort {

    private final Map<Long, DilemmaDomain> dilemmaList = new HashMap<>();

    @Override
    public void createDilemma(DilemmaCreateCommand createCommand) {
        Long dilemmaId = newId();
        dilemmaList.put(
                dilemmaId,
                new DilemmaDomain(
                        dilemmaId,
                        createCommand.issueId(),
                        createCommand.title(),
                        createCommand.details(),
                        YN.Y,
                        null,
                        null
                )
        );
    }

    @Override
    public Optional<DilemmaDomain> getDilemma(User user, Long dilemmaId) {
        return getDilemmaUnscoped(dilemmaId);
    }

    @Override
    public Optional<DilemmaDomain> getDilemmaUnscoped(Long dilemmaId) {
        return Optional.ofNullable(dilemmaList.get(dilemmaId));
    }

    @Override
    public void saveDilemma(DilemmaDomain dilemmaDomain) {
        Long dilemmaId = dilemmaDomain.getDilemmaId();
        dilemmaList.put(dilemmaId, dilemmaDomain);
    }

    @Override
    public Page<DilemmaSearchResponse> searchUserDilemma(User user, DilemmaUserSearchQuery query) {
        Pageable pageable = PageRequest.of(
                query.page() - 1,
                query.size(),
                Sort.by(Sort.Direction.fromString(query.direction().toUpperCase()), query.sortBy())
        );

        List<DilemmaSearchResponse> content = dilemmaList.values()
                .stream()
                .filter(dilemma -> {
                    Boolean result = true;
                    if (query.keyword() != null && !query.keyword().isBlank()) {
                        result &= dilemma.getTitle().contains(query.keyword()) || dilemma.getDetails().contains(query.keyword());
                    }
                    if (query.fromDate() != null) {
                        result &= query.fromDate().isBefore(dilemma.getCreatedAt()) || query.fromDate().isEqual(dilemma.getCreatedAt());
                    }
                    if (query.toDate() != null) {
                        result &= query.toDate().isAfter(dilemma.getCreatedAt()) || query.toDate().isEqual(dilemma.getCreatedAt());
                    }
                    return result;
                })
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .map(dilemma -> new DilemmaSearchResponse(
                        dilemma.getDilemmaId(),
                        dilemma.getTitle(),
                        dilemma.getIssueId(),
                        null,
                        dilemma.getIsOpen(),
                        dilemma.getCreatedAt(),
                        dilemma.getUpdatedAt()
                ))
                .toList();

        Long total = dilemmaList.values()
                .stream()
                .filter(dilemma -> {
                    Boolean result = true;
                    if (query.keyword() != null && !query.keyword().isBlank()) {
                        result &= dilemma.getTitle().contains(query.keyword()) || dilemma.getDetails().contains(query.keyword());
                    }
                    if (query.fromDate() != null) {
                        result &= query.fromDate().isBefore(dilemma.getCreatedAt()) || query.fromDate().isEqual(dilemma.getCreatedAt());
                    }
                    if (query.toDate() != null) {
                        result &= query.toDate().isAfter(dilemma.getCreatedAt()) || query.toDate().isEqual(dilemma.getCreatedAt());
                    }
                    return result;
                })
                .count();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<DilemmaSearchResponse> searchFullDilemma(DilemmaSearchQuery query) {
        Pageable pageable = PageRequest.of(
                query.page() - 1,
                query.size(),
                Sort.by(Sort.Direction.fromString(query.direction().toUpperCase()), query.sortBy())
        );

        List<DilemmaSearchResponse> content = dilemmaList.values()
                .stream()
                .filter(dilemma -> {
                    Boolean result = true;
                    if (query.title() != null && !query.title().isBlank()) {
                        result &= dilemma.getTitle().contains(query.title());
                    }
                    if (query.details() != null && !query.details().isBlank()) {
                        result &= dilemma.getDetails().contains(query.details());
                    }
                    if (query.createFrom() != null) {
                        result &= query.createFrom().isBefore(dilemma.getCreatedAt()) || query.createFrom().isEqual(dilemma.getCreatedAt());
                    }
                    if (query.createTo() != null) {
                        result &= query.createTo().isAfter(dilemma.getCreatedAt()) || query.createTo().isEqual(dilemma.getCreatedAt());
                    }
                    if (query.updateFrom() != null) {
                        result &= query.updateFrom().isBefore(dilemma.getUpdatedAt()) || query.updateFrom().isEqual(dilemma.getUpdatedAt());
                    }
                    if (query.updateTo() != null) {
                        result &= query.updateTo().isAfter(dilemma.getUpdatedAt()) || query.updateTo().isEqual(dilemma.getUpdatedAt());
                    }
                    return result;
                })
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .map(dilemma -> new DilemmaSearchResponse(
                        dilemma.getDilemmaId(),
                        dilemma.getTitle(),
                        dilemma.getIssueId(),
                        null,
                        dilemma.getIsOpen(),
                        dilemma.getCreatedAt(),
                        dilemma.getUpdatedAt()
                ))
                .toList();

        Long total = dilemmaList.values()
                .stream()
                .filter(dilemma -> {
                    Boolean result = true;
                    if (query.title() != null && !query.title().isBlank()) {
                        result &= dilemma.getTitle().contains(query.title());
                    }
                    if (query.details() != null && !query.details().isBlank()) {
                        result &= dilemma.getDetails().contains(query.details());
                    }
                    if (query.createFrom() != null) {
                        result &= query.createFrom().isBefore(dilemma.getCreatedAt()) || query.createFrom().isEqual(dilemma.getCreatedAt());
                    }
                    if (query.createTo() != null) {
                        result &= query.createTo().isAfter(dilemma.getCreatedAt()) || query.createTo().isEqual(dilemma.getCreatedAt());
                    }
                    if (query.updateFrom() != null) {
                        result &= query.updateFrom().isBefore(dilemma.getUpdatedAt()) || query.updateFrom().isEqual(dilemma.getUpdatedAt());
                    }
                    if (query.updateTo() != null) {
                        result &= query.updateTo().isAfter(dilemma.getUpdatedAt()) || query.updateTo().isEqual(dilemma.getUpdatedAt());
                    }
                    return result;
                })
                .count();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<User> getDilemmaUser(Long dilemmaId) {
        throw new UnsupportedOperationException("Warning: Not supported in test environments (e.g., Fake Ports). Use only for the actual running application.");
    }

    @Override
    public Long newId() {
        return (long) dilemmaList.size() + 1;
    }

    @Override
    public Long lastId() {
        return dilemmaList.keySet()
                .stream()
                .reduce(Long::max)
                .orElse(null);
    }
}
