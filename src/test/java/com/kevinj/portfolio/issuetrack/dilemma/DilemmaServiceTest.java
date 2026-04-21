package com.kevinj.portfolio.issuetrack.dilemma;

import com.kevinj.portfolio.issuetrack.admin.FakeCategoryManagePort;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.CategoryMapper;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryCreateCommand;
import com.kevinj.portfolio.issuetrack.admin.domain.CategoryManageInfo;
import com.kevinj.portfolio.issuetrack.dilemma.application.DilemmaService;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.*;
import com.kevinj.portfolio.issuetrack.dilemma.exception.DilemmaClosedException;
import com.kevinj.portfolio.issuetrack.dilemma.exception.DilemmaNotFoundException;
import com.kevinj.portfolio.issuetrack.global.enums.UserRole;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.issue.FakeIssuePort;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueStatus;
import com.kevinj.portfolio.issuetrack.issue.exception.DisabledStatusException;
import com.kevinj.portfolio.issuetrack.process.FakeProcessPort;
import com.kevinj.portfolio.issuetrack.process.FakeStepPort;
import com.kevinj.portfolio.issuetrack.process.adapter.out.ProcessAndStepMapper;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessCreateCommand;
import com.kevinj.portfolio.issuetrack.process.application.dto.step.StepCreateInfo;
import com.kevinj.portfolio.issuetrack.process.domain.ProcessDomain;
import com.kevinj.portfolio.issuetrack.process.domain.StepDomain;
import com.kevinj.portfolio.issuetrack.user.FakeUserPort;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class DilemmaServiceTest {

    private final CategoryMapper categoryMapper = new CategoryMapper();
    private final ProcessAndStepMapper processAndStepMapper = new ProcessAndStepMapper();

    private final FakeUserPort fakeUserPort = new FakeUserPort();
    private final FakeCategoryManagePort fakeCategoryManagePort = new FakeCategoryManagePort(categoryMapper);
    private final FakeProcessPort fakeProcessPort = new FakeProcessPort(processAndStepMapper);
    private final FakeStepPort fakeStepPort = new FakeStepPort(processAndStepMapper);
    private final FakeIssuePort fakeIssuePort = new FakeIssuePort();
    private final FakeDilemmaPort fakeDilemmaPort = new FakeDilemmaPort();
    private final FakeDilemmaDiscussionPort fakeDilemmaDiscussionPort = new FakeDilemmaDiscussionPort();
    private final DilemmaService dilemmaService = new DilemmaService(fakeUserPort, fakeIssuePort, fakeDilemmaPort, fakeDilemmaDiscussionPort);

    @BeforeEach
    void setUp() {
        fakeUserPort.create(
                new User(
                        null,
                        "tester1",
                        "qwe123$",
                        UserRole.USER,
                        "tester 1",
                        "tester1@kevinj.com",
                        "",
                        YN.Y,
                        0
                )
        );

        User user = fakeUserPort.loadLoginUser("tester1").get();

        fakeCategoryManagePort.addCategory(
                new CategoryCreateCommand(
                        null,
                        "Category Level 1",
                        YN.Y
                )
        );

        fakeCategoryManagePort.addCategory(
                new CategoryCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        "Category Level 2",
                        YN.Y
                )
        );

        fakeCategoryManagePort.addCategory(
                new CategoryCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        "Category Level 3",
                        YN.Y
                )
        );

        CategoryManageInfo category = fakeCategoryManagePort.getCategory(fakeCategoryManagePort.lastId()).get();

        fakeProcessPort.createProcess(
                user,
                new ProcessCreateCommand("My process", "", YN.Y)
        );

        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();

        fakeStepPort.createStep(
                user,
                process,
                new StepCreateInfo("ready", 1)
        );

        fakeStepPort.createStep(
                user,
                process,
                new StepCreateInfo("start", 2)
        );

        fakeStepPort.createStep(
                user,
                process,
                new StepCreateInfo("finish", 3)
        );

        StepDomain initStep = fakeStepPort.getInitialStep(user, process).get();

        fakeIssuePort.createIssue(
            new IssueDomain(
                user.getUserId(),
                category.getCategoryId(),
                process.getProcessId(),
                initStep.getStepId(),
                "Test Issue",
                "tests wow"
            )
        );
    }

    @Test
    void dilemma_registration_success_verification() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        Long issueId = fakeIssuePort.lastId();

        assertThatNoException().isThrownBy(() ->
            dilemmaService.openDilemma(
                    user.getUserId(),
                    new DilemmaCreateCommand(
                            issueId,
                            "Test dilemma",
                            "I cannot solve this problem"
                    )
            )
        );
    }

    @Test
    void if_there_is_an_open_dilemma_in_the_issue__dilemma_registration_fails() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        Long issueId = fakeIssuePort.lastId();

        dilemmaService.openDilemma(
                user.getUserId(),
                new DilemmaCreateCommand(
                        issueId,
                        "Test dilemma",
                        "I cannot solve this problem"
                )
        );

        assertThatException().isThrownBy(() ->
                dilemmaService.openDilemma(
                        user.getUserId(),
                        new DilemmaCreateCommand(
                                issueId,
                                "Test dilemma2",
                                "I cannot solve this problem"
                        )
                )
        ).isInstanceOf(DisabledStatusException.class);
    }

    @Test
    void if_issue_status_is_EXIT_dilemma_registration_fails() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        Long issueId = fakeIssuePort.lastId();

        IssueDomain issue = fakeIssuePort.getIssue(user, issueId).get();
        issue.changeStatus(IssueStatus.EXIT);
        fakeIssuePort.saveIssue(issue);

        assertThatException().isThrownBy(() ->
                dilemmaService.openDilemma(
                        user.getUserId(),
                        new DilemmaCreateCommand(
                                issueId,
                                "Test dilemma2",
                                "I cannot solve this problem"
                        )
                )
        ).isInstanceOf(DisabledStatusException.class);
    }

    @Test
    void dilemma_closing_success_verification() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        Long issueId = fakeIssuePort.lastId();

        dilemmaService.openDilemma(
                user.getUserId(),
                new DilemmaCreateCommand(
                        issueId,
                        "Test dilemma",
                        "I cannot solve this problem"
                )
        );
        Long dilemmaId = fakeDilemmaPort.lastId();

        assertThatNoException().isThrownBy(() -> {
            dilemmaService.closeDilemma(dilemmaId);
        });
    }

    @Test
    void closed_dilemma_failed() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        Long issueId = fakeIssuePort.lastId();

        dilemmaService.openDilemma(
                user.getUserId(),
                new DilemmaCreateCommand(
                        issueId,
                        "Test dilemma",
                        "I cannot solve this problem"
                )
        );
        Long dilemmaId = fakeDilemmaPort.lastId();
        dilemmaService.closeDilemma(dilemmaId);

        assertThatException().isThrownBy(() ->
                dilemmaService.editDilemma(
                        user.getUserId(),
                        new DilemmaEditCommand(dilemmaId, "Fail testing", "expected fail")
                )
        ).isInstanceOf(DilemmaClosedException.class);
    }

    private void setupAnotherUserDilemma() {
        // user 1 setting
        User user1 = fakeUserPort.loadLoginUser("tester1").get();
        Long issueId = fakeIssuePort.lastId();

        dilemmaService.openDilemma(
                user1.getUserId(),
                new DilemmaCreateCommand(
                        issueId,
                        "Oh my dilemma 1",
                        "I cannot solve this problem"
                )
        );

        // user 2 setting
        fakeUserPort.create(
                new User(
                        null,
                        "eren",
                        "qwe123$",
                        UserRole.USER,
                        "Eren Jäger",
                        "eren@surveycorps.com",
                        "",
                        YN.Y,
                        0
                )
        );

        User user2 = fakeUserPort.loadLoginUser("eren").get();

        fakeProcessPort.createProcess(
                user2,
                new ProcessCreateCommand("to be an attack on title", "", YN.Y)
        );

        ProcessDomain process2 = fakeProcessPort.getProcess(user2, fakeProcessPort.lastId()).get();

        fakeStepPort.createStep(
                user2,
                process2,
                new StepCreateInfo("Injection", 1)
        );

        fakeStepPort.createStep(
                user2,
                process2,
                new StepCreateInfo("Intellectual Titan", 2)
        );

        fakeStepPort.createStep(
                user2,
                process2,
                new StepCreateInfo("Eating Inheritor", 3)
        );

        fakeStepPort.createStep(
                user2,
                process2,
                new StepCreateInfo("Awakening", 4)
        );

        StepDomain initStep = fakeStepPort.getInitialStep(user2, process2).get();

        CategoryManageInfo category = fakeCategoryManagePort.getCategory(fakeCategoryManagePort.lastId()).get();

        fakeIssuePort.createIssue(
            new IssueDomain(
                user2.getUserId(),
                category.getCategoryId(),
                process2.getProcessId(),
                initStep.getStepId(),
                "no skill without transformation",
                "I wanna get hardening skill"
            )
        );

        Long issue2Id = fakeIssuePort.lastId();

        dilemmaService.openDilemma(
                user2.getUserId(),
                new DilemmaCreateCommand(
                        issue2Id,
                        "All other titans are enemies except me.",
                        "I must subdue them all to obtain their abilities"
                )
        );

        Long dilemma2Id = fakeDilemmaPort.lastId();

        dilemmaService.createDiscussion(
                user1.getUserId(),
                new DilemmaDiscussionCreateCommand(
                        dilemma2Id,
                        "Why are you must subdue them all?"
                )
        );

        dilemmaService.createDiscussion(
                user2.getUserId(),
                new DilemmaDiscussionCreateCommand(
                        dilemma2Id,
                        "To take their abilities, I have to eat them, and there is no way to eat them unless I fight and win."
                )
        );

    }

    @Test
    void user_dilemma_search_success_verification() {
        setupAnotherUserDilemma();

        User user2 = fakeUserPort.loadLoginUser("eren").get();
        DilemmaUserSearchCommand userSearchCommand = new DilemmaUserSearchCommand(
                null,
                null,
                null,
                null,
                "titans",
                null,
                null
        );

        assertThat(dilemmaService.searchMyDilemmaList(user2.getUserId(), userSearchCommand.toQuery()).getNumberOfElements()).isEqualTo(1);
    }

    @Test
    void verification_of_overall_dilemma_search_success() {
        setupAnotherUserDilemma();

        DilemmaSearchCommand searchCommand = new DilemmaSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThat(dilemmaService.searchFullDilemmaList(searchCommand.toQuery()).getNumberOfElements()).isEqualTo(2);
    }

    @Test
    void dilemmaDiscussion_registration_success_verification() {
        setupAnotherUserDilemma();

        User user1 = fakeUserPort.loadLoginUser("tester1").get();
        Long dilemma2Id = fakeDilemmaPort.lastId();

        assertThatNoException().isThrownBy(() ->
                dilemmaService.createDiscussion(
                        user1.getUserId(),
                        new DilemmaDiscussionCreateCommand(
                                dilemma2Id,
                                "Oh my god.. I scared"
                        )
                )
        );
    }

    @Test
    void registering_a_dilemmaDiscussion_on_a_non_existent_dilemma_fails() {
        setupAnotherUserDilemma();

        User user1 = fakeUserPort.loadLoginUser("tester1").get();
        Long dilemma2Id = fakeDilemmaPort.lastId() + 100;

        assertThatException().isThrownBy(() ->
                dilemmaService.createDiscussion(
                        user1.getUserId(),
                        new DilemmaDiscussionCreateCommand(
                                dilemma2Id,
                                "Oh my god.. I scared"
                        )
                )
        ).isInstanceOf(DilemmaNotFoundException.class);
    }

    @Test
    void dilemmaDiscussion_edit_success_verification() {
        setupAnotherUserDilemma();

        User user1 = fakeUserPort.loadLoginUser("tester1").get();
        Long dilemma2Id = fakeDilemmaPort.lastId();

        dilemmaService.createDiscussion(
                user1.getUserId(),
                new DilemmaDiscussionCreateCommand(
                        dilemma2Id,
                        "Oh my god.. I scared"
                )
        );

        assertThatNoException().isThrownBy(() ->
                dilemmaService.editDiscussion(
                        user1.getUserId(),
                        new DilemmaDiscussionEditCommand(
                                fakeDilemmaDiscussionPort.lastId(),
                                dilemma2Id,
                                "It's terrifying, but it's the unavoidable reality. Fight and win."
                        )
                )
        );

    }

    @Test
    void dilemmaDiscussion_deletion_success_verification() {
        setupAnotherUserDilemma();

        User user1 = fakeUserPort.loadLoginUser("tester1").get();
        Long dilemma2Id = fakeDilemmaPort.lastId();

        dilemmaService.createDiscussion(
                user1.getUserId(),
                new DilemmaDiscussionCreateCommand(
                        dilemma2Id,
                        "Hmm.. holy moly..."
                )
        );

        assertThatNoException().isThrownBy(() ->
                dilemmaService.deleteDiscussion(
                        user1.getUserId(),
                        new DilemmaDiscussionDeleteCommand(
                                fakeDilemmaDiscussionPort.lastId(),
                                dilemma2Id
                        )
                )
        );
    }

}
