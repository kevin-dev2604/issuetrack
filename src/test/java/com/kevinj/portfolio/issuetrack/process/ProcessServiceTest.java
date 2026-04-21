package com.kevinj.portfolio.issuetrack.process;

import com.kevinj.portfolio.issuetrack.global.enums.UserRole;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.issue.FakeIssuePort;
import com.kevinj.portfolio.issuetrack.process.adapter.out.ProcessAndStepMapper;
import com.kevinj.portfolio.issuetrack.process.application.ProcessService;
import com.kevinj.portfolio.issuetrack.process.application.ProcessUseCase;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.*;
import com.kevinj.portfolio.issuetrack.process.exception.process.ProcessAlreadyDeletedException;
import com.kevinj.portfolio.issuetrack.process.exception.process.ProcessInputInvalidException;
import com.kevinj.portfolio.issuetrack.process.exception.process.ProcessNotFoundException;
import com.kevinj.portfolio.issuetrack.user.FakeUserPort;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.*;

public class ProcessServiceTest {

    private final ProcessAndStepMapper mapper = new ProcessAndStepMapper();
    private final FakeUserPort fakeUserPort = new FakeUserPort();
    private final FakeProcessPort fakeProcessPort = new FakeProcessPort(mapper);
    private final FakeIssuePort fakeIssuePort = new FakeIssuePort();
    private final FakeProcessUsageCheckPort fakeProcessUsageCheckPort = new FakeProcessUsageCheckPort(fakeIssuePort);
    private final ProcessUseCase processUseCase = new ProcessService(mapper, fakeProcessPort, fakeProcessUsageCheckPort, fakeUserPort);

    @BeforeEach
    void setUp() {
        User user1 = new User(
                fakeUserPort.newId(),
                "test1",
                "qw12#$",
                UserRole.USER,
                "Test user 1",
                "test1@kevinj.com",
                "",
                YN.Y,
                0
        );
        fakeUserPort.create(user1);

        User user2 = new User(
                fakeUserPort.newId(),
                "test2",
                "qw12#$",
                UserRole.USER,
                "Test user 2",
                "test2@kevinj.com",
                "",
                YN.Y,
                0
        );
        fakeUserPort.create(user2);
    }

    @Test
    void process_single_item_creation_success_verification () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();

        ProcessCreateCommand processCreateCommand = new ProcessCreateCommand("process1", "test process", YN.Y);
        assertThatNoException().isThrownBy(() -> processUseCase.createProcessInfo(user1.getUserId(), processCreateCommand));

        ProcessInfoResponse processInfoResponse = processUseCase.getProcessInfo(user1.getUserId(), fakeProcessPort.lastId());
        assertThat(processInfoResponse).isNotNull();
        assertThat(processInfoResponse.name()).isEqualTo(processCreateCommand.name());
        assertThat(processInfoResponse.note()).isEqualTo(processCreateCommand.note());
        assertThat(processInfoResponse.isActive()).isEqualTo(processCreateCommand.isActive());
    }

    @Test
    void process_modification_success_verification () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        processUseCase.createProcessInfo(user1.getUserId(), new  ProcessCreateCommand("process1", "test process", YN.Y));

        Long processId = fakeProcessPort.lastId();
        ProcessInfoResponse processInfoResponse1 = processUseCase.getProcessInfo(user1.getUserId(), processId);
        ProcessUpdateCommand processUpdateCommand = new ProcessUpdateCommand(processId, "modified process", "modify test",  YN.N);

        assertThatNoException().isThrownBy(() -> processUseCase.updateProcessInfo(user1.getUserId(), processUpdateCommand));

        ProcessInfoResponse processInfoResponse2 = processUseCase.getProcessInfo(user1.getUserId(), processId);

        assertThat(processInfoResponse2).isNotNull();
        assertThat(processInfoResponse2.name()).isNotEqualTo(processInfoResponse1.name());
        assertThat(processInfoResponse2.note()).isNotEqualTo(processInfoResponse1.note());
        assertThat(processInfoResponse2.isActive()).isNotEqualTo(processInfoResponse1.isActive());
    }

    @Test
    void process_deletion_success_verification () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        processUseCase.createProcessInfo(user1.getUserId(), new  ProcessCreateCommand("process1", "test process", YN.Y));

        Long processId = fakeProcessPort.lastId();
        ProcessDeleteResponse processDeleteResponse = processUseCase.deleteProcessInfo(user1.getUserId(), processId);

        assertThat(processDeleteResponse).isEqualTo(ProcessDeleteResponse.deleted());

        assertThatException().isThrownBy(() -> processUseCase.getProcessInfo(user1.getUserId(), processId))
                .isInstanceOf(ProcessNotFoundException.class);
    }

    @Test
    void process_search_success_verification () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        processUseCase.createProcessInfo(user1.getUserId(), new  ProcessCreateCommand("Sprints", "Pure Speed / Power", YN.Y));
        processUseCase.createProcessInfo(user1.getUserId(), new  ProcessCreateCommand("Middle:Heat", "preliminary rounds", YN.Y));
        processUseCase.createProcessInfo(user1.getUserId(), new  ProcessCreateCommand("Middle:Semi-finals", "Semi final rounds", YN.Y));
        processUseCase.createProcessInfo(user1.getUserId(), new  ProcessCreateCommand("Middle:Final", "Final round", YN.Y));
        processUseCase.createProcessInfo(user1.getUserId(), new  ProcessCreateCommand("Long", "Aerobic Capacity / Strategy", YN.Y));
        processUseCase.createProcessInfo(user1.getUserId(), new  ProcessCreateCommand("Road-Race", "Marathon", YN.Y));

        Page<ProcessInfoResponse> pageResults = processUseCase.searchProcesses(
                new ProcessSearchCommand(null, null, null, null, "Middle", YN.Y).toQuery(user1.getUserId())
        );

        assertThat(pageResults).isNotNull();
        assertThat(pageResults.getNumberOfElements()).isEqualTo(3);
    }

    @Test
    void if_parameters_are_invalid_during_process_creation__it_fails () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();

        ProcessCreateCommand processCreateCommand1 = new ProcessCreateCommand(" ", null, YN.Y);
        assertThatException().isThrownBy(() -> processUseCase.createProcessInfo(user1.getUserId(), processCreateCommand1))
                .isInstanceOf(ProcessInputInvalidException.class);

        ProcessCreateCommand processCreateCommand2 = new ProcessCreateCommand("error-process", null, null);
        assertThatException().isThrownBy(() -> processUseCase.createProcessInfo(user1.getUserId(), processCreateCommand2))
                .isInstanceOf(ProcessInputInvalidException.class);
    }

    @Test
    void if_the_parameter_is_invalid_when_modifying_the_process__it_fails () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        processUseCase.createProcessInfo(user1.getUserId(), new ProcessCreateCommand("tests", "test case", YN.Y));

        Long processId = fakeProcessPort.lastId();

        ProcessUpdateCommand processUpdateCommand1 = new ProcessUpdateCommand(processId, " ", null, YN.Y);
        assertThatException().isThrownBy(() -> processUseCase.updateProcessInfo(processId, processUpdateCommand1))
                .isInstanceOf(ProcessInputInvalidException.class);

        ProcessUpdateCommand processUpdateCommand2 = new ProcessUpdateCommand(processId, "error-process", null, null);
        assertThatException().isThrownBy(() -> processUseCase.updateProcessInfo(processId, processUpdateCommand2))
                .isInstanceOf(ProcessInputInvalidException.class);
    }

    @Test
    void deleting_duplicate_processes_fails () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        processUseCase.createProcessInfo(user1.getUserId(), new ProcessCreateCommand("tests", "test case", YN.Y));

        Long processId = fakeProcessPort.lastId();

        processUseCase.deleteProcessInfo(user1.getUserId(), processId);
        assertThatException().isThrownBy(() -> processUseCase.deleteProcessInfo(user1.getUserId(), processId))
                .isInstanceOf(ProcessAlreadyDeletedException.class);
    }

    @Test
    void querying_another_user_s_process_fails () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        processUseCase.createProcessInfo(user1.getUserId(), new ProcessCreateCommand("tests", "test case", YN.Y));

        Long processId = fakeProcessPort.lastId();
        User user2 = fakeUserPort.loadLoginUser("test2").get();

        assertThatException().isThrownBy(() -> processUseCase.getProcessInfo(user2.getUserId(), processId))
                .isInstanceOf(ProcessNotFoundException.class);
    }
}
