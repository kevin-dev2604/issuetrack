package com.kevinj.portfolio.issuetrack.process;

import com.kevinj.portfolio.issuetrack.global.enums.UserRole;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.process.adapter.out.ProcessAndStepMapper;
import com.kevinj.portfolio.issuetrack.process.application.StepService;
import com.kevinj.portfolio.issuetrack.process.application.StepUseCase;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessCreateCommand;
import com.kevinj.portfolio.issuetrack.process.application.dto.step.*;
import com.kevinj.portfolio.issuetrack.process.domain.ProcessDomain;
import com.kevinj.portfolio.issuetrack.process.exception.step.StepNotFoundException;
import com.kevinj.portfolio.issuetrack.process.exception.step.StepParameterInvalidException;
import com.kevinj.portfolio.issuetrack.user.FakeUserPort;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class StepServiceTest {

    private final ProcessAndStepMapper mapper = new ProcessAndStepMapper();
    private final FakeUserPort fakeUserPort = new FakeUserPort();
    private final FakeProcessPort fakeProcessPort = new FakeProcessPort(mapper);
    private final FakeStepPort fakeStepPort = new FakeStepPort(mapper);
    private final StepUseCase stepUseCase = new StepService(fakeUserPort, fakeProcessPort, fakeStepPort, mapper);

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

        fakeProcessPort.createProcess(
                user1,
                new ProcessCreateCommand("Test Process", "Only for use case tests", YN.Y)
        );
    }

    @Test
    void step_단일생성_성공_검증 () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user1, fakeProcessPort.lastId()).get();

        StepCreateCommand stepCreateCommand = new StepCreateCommand(process.getProcessId(), "Step 1", 1);

        assertThatNoException().isThrownBy(() -> stepUseCase.createStepInfo(user1.getUserId(), stepCreateCommand));
    }

    @Test
    void step_일괄생성_성공_검증 () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user1, fakeProcessPort.lastId()).get();

        List<StepCreateInfo> createInfoList = Lists.newArrayList();
        createInfoList.add(new StepCreateInfo("System Design", 2));
        createInfoList.add(new StepCreateInfo("Requirement Analysis", 1));
        createInfoList.add(new StepCreateInfo("Testing", 4));
        createInfoList.add(new StepCreateInfo("Maintenance", 6));
        createInfoList.add(new StepCreateInfo("Implementation (Coding)", 3));
        createInfoList.add(new StepCreateInfo("Deployment", 5));

        StepListCreateCommand listCreateCommand = new StepListCreateCommand(process.getProcessId(), createInfoList);
        assertThatNoException().isThrownBy(() -> stepUseCase.createStepList(user1.getUserId(), listCreateCommand));

        List<StepInfoResponse> stepInfoResponse = stepUseCase.getAllStepList(user1.getUserId(), process.getProcessId());

        assertThat(stepInfoResponse.size()).isEqualTo(6);
        assertThat(stepInfoResponse.stream().findFirst().get().order()).isEqualTo(1);
    }

    @Test
    void step_수정_성공_검증 () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user1, fakeProcessPort.lastId()).get();
        StepCreateCommand stepCreateCommand = new StepCreateCommand(process.getProcessId(), "Step 1", 1);
        stepUseCase.createStepInfo(user1.getUserId(), stepCreateCommand);

        Long stepId = fakeStepPort.lastId();
        StepCommand command = new StepCommand(stepId, process.getProcessId());
        StepInfoResponse response1 = stepUseCase.getStepInfo(user1.getUserId(), command);

        StepUpdateCommand stepUpdateCommand = new StepUpdateCommand(
                stepId,
                process.getProcessId(),
                "Updated step 1",
                2,
                YN.N
        );
        stepUseCase.updateStepInfo(user1.getUserId(), stepUpdateCommand);

        StepInfoResponse response2 = stepUseCase.getStepInfo(user1.getUserId(), command);

        assertThat(response2.name()).isNotEqualTo(response1.name());
        assertThat(response2.order()).isNotEqualTo(response1.order());
        assertThat(response2.isActive()).isNotEqualTo(response1.isActive());
    }

    @Test
    void step_삭제_성공_검증 () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user1, fakeProcessPort.lastId()).get();
        stepUseCase.createStepInfo(user1.getUserId(), new StepCreateCommand(process.getProcessId(), "Step 1", 1));

        assertThatNoException().isThrownBy(() ->
                stepUseCase.deleteStepInfo(user1.getUserId(), new StepCommand(fakeStepPort.lastId(), process.getProcessId())));
    }

    @Test
    void step_생성시_파라미터가_유효하지_않으면_실패한다 () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user1, fakeProcessPort.lastId()).get();

        StepCreateCommand stepCreateCommand1 = new StepCreateCommand(process.getProcessId(), " ", 1);
        assertThatException().isThrownBy(() -> stepUseCase.createStepInfo(user1.getUserId(), stepCreateCommand1))
                .isInstanceOf(StepParameterInvalidException.class);

        StepCreateCommand stepCreateCommand2 = new StepCreateCommand(process.getProcessId(), "invalid", -1);
        assertThatException().isThrownBy(() -> stepUseCase.createStepInfo(user1.getUserId(), stepCreateCommand2))
                .isInstanceOf(StepParameterInvalidException.class);
    }

    @Test
    void step_수정시_파라미터가_유효하지_않으면_실패한다 () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user1, fakeProcessPort.lastId()).get();
        StepCreateCommand stepCreateCommand = new StepCreateCommand(process.getProcessId(), "Step 1", 1);
        stepUseCase.createStepInfo(user1.getUserId(), stepCreateCommand);

        Long stepId = fakeStepPort.lastId();

        StepUpdateCommand stepUpdateCommand1 = new StepUpdateCommand(
                stepId,
                process.getProcessId(),
                " ",
                2,
                YN.N
        );
        assertThatException().isThrownBy(() -> stepUseCase.updateStepInfo(user1.getUserId(), stepUpdateCommand1))
                .isInstanceOf(StepParameterInvalidException.class);

        StepUpdateCommand stepUpdateCommand2 = new StepUpdateCommand(
                stepId,
                process.getProcessId(),
                "invalid",
                0,
                YN.N
        );
        assertThatException().isThrownBy(() -> stepUseCase.updateStepInfo(user1.getUserId(), stepUpdateCommand2))
                .isInstanceOf(StepParameterInvalidException.class);

        StepUpdateCommand stepUpdateCommand3 = new StepUpdateCommand(
                stepId,
                process.getProcessId(),
                "invalid",
                2,
                null
        );
        assertThatException().isThrownBy(() -> stepUseCase.updateStepInfo(user1.getUserId(), stepUpdateCommand3))
                .isInstanceOf(StepParameterInvalidException.class);
    }

    @Test
    void step을_중복해서_삭제하면_실패한다 () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user1, fakeProcessPort.lastId()).get();
        StepCreateCommand stepCreateCommand = new StepCreateCommand(process.getProcessId(), "Step 1", 1);
        stepUseCase.createStepInfo(user1.getUserId(), stepCreateCommand);

        Long stepId = fakeStepPort.lastId();
        StepCommand command = new StepCommand(stepId, process.getProcessId());

        stepUseCase.deleteStepInfo(user1.getUserId(), command);
        assertThatException().isThrownBy(() -> stepUseCase.deleteStepInfo(user1.getUserId(), command))
                .isInstanceOf(StepNotFoundException.class);
    }

    @Test
    void 다른_process의_step을_조회하면_실패한다 () {
        User user1 = fakeUserPort.loadLoginUser("test1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user1, fakeProcessPort.lastId()).get();
        StepCreateCommand stepCreateCommand = new StepCreateCommand(process.getProcessId(), "Step 1", 1);
        stepUseCase.createStepInfo(user1.getUserId(), stepCreateCommand);

        Long stepId = fakeStepPort.lastId();

        fakeProcessPort.createProcess(
                user1,
                new ProcessCreateCommand("Test Process 2", "Only for use case tests", YN.Y)
        );

        assertThatException().isThrownBy(() ->
                        stepUseCase.getStepInfo(user1.getUserId(), new StepCommand(stepId, fakeProcessPort.lastId()))
                )
                .isInstanceOf(StepNotFoundException.class);
    }
}
