package com.kevinj.portfolio.issuetrack.issue;

import com.kevinj.portfolio.issuetrack.admin.FakeAttributeManagePort;
import com.kevinj.portfolio.issuetrack.admin.FakeCategoryManagePort;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.AttributesMapper;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.CategoryMapper;
import com.kevinj.portfolio.issuetrack.admin.application.dto.AttributesCreateCommand;
import com.kevinj.portfolio.issuetrack.admin.application.dto.AttributesSearchCommand;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryCreateCommand;
import com.kevinj.portfolio.issuetrack.global.enums.UserRole;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.issue.application.IssueService;
import com.kevinj.portfolio.issuetrack.issue.application.dto.*;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueStatus;
import com.kevinj.portfolio.issuetrack.issue.exception.CannotProceedNextStepException;
import com.kevinj.portfolio.issuetrack.issue.exception.DisabledStatusException;
import com.kevinj.portfolio.issuetrack.issue.exception.EmptyProcessException;
import com.kevinj.portfolio.issuetrack.issue.exception.InvalidInputException;
import com.kevinj.portfolio.issuetrack.process.FakeProcessPort;
import com.kevinj.portfolio.issuetrack.process.FakeStepPort;
import com.kevinj.portfolio.issuetrack.process.adapter.out.ProcessAndStepMapper;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessCreateCommand;
import com.kevinj.portfolio.issuetrack.process.application.dto.step.StepCreateInfo;
import com.kevinj.portfolio.issuetrack.process.domain.ProcessDomain;
import com.kevinj.portfolio.issuetrack.process.exception.process.ProcessNotFoundException;
import com.kevinj.portfolio.issuetrack.user.FakeUserPort;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class IssueServiceTest {

    private static final Logger log = LoggerFactory.getLogger(IssueServiceTest.class);
    private final CategoryMapper categoryMapper = new CategoryMapper();
    private final AttributesMapper attributesMapper = new AttributesMapper();
    private final ProcessAndStepMapper processAndStepMapper = new ProcessAndStepMapper();

    private final FakeUserPort fakeUserPort = new FakeUserPort();
    private final FakeCategoryManagePort fakeCategoryManagePort = new FakeCategoryManagePort(categoryMapper);
    private final FakeAttributeManagePort fakeAttributeManagePort = new FakeAttributeManagePort(attributesMapper);
    private final FakeProcessPort fakeProcessPort = new FakeProcessPort(processAndStepMapper);
    private final FakeStepPort fakeStepPort = new FakeStepPort(processAndStepMapper);

    private final FakeIssuePort fakeIssuePort = new FakeIssuePort();
    private final IssueService issueService = new IssueService(
            fakeUserPort,
            fakeCategoryManagePort,
            fakeAttributeManagePort,
            fakeProcessPort,
            fakeStepPort,
            fakeIssuePort
    );

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

        fakeAttributeManagePort.addAttributes(
                new AttributesCreateCommand("Country", YN.Y)
        );

        fakeAttributeManagePort.addAttributes(
                new AttributesCreateCommand("Location", YN.Y)
        );

        fakeProcessPort.createProcess(
                user,
                new ProcessCreateCommand("My process", "", YN.Y)
        );

    }

    private void createBasicSteps(User user, ProcessDomain process) {

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
                new StepCreateInfo("proceed", 3)
        );

        fakeStepPort.createStep(
                user,
                process,
                new StepCreateInfo("imminent", 4)
        );

        fakeStepPort.createStep(
                user,
                process,
                new StepCreateInfo("finish", 5)
        );
    }

    @Test
    void issue_creation_success_verification() {

        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        assertThatNoException().isThrownBy(() -> issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Test case management issue",
                        "I consider about organizing practical test cases."
                )
        ));

    }

    @Test
    void creating_an_issue_with_a_non_existent_category_fails() {

        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        Long categoryId = fakeCategoryManagePort.lastId() + 1;

        assertThatException().isThrownBy(() -> issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        categoryId,
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Test case management issue",
                        "I consider about organizing practical test cases."
                )
            )
        ).isInstanceOf(InvalidInputException.class);

    }

    @Test
    void creating_an_issue_with_a_non_existent_attribute_fails() {

        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        List<IssueAttributesBasicInfo> attributesCreateInfoList = Lists.newArrayList();
        attributesCreateInfoList.add(new IssueAttributesBasicInfo(fakeCategoryManagePort.lastId() + 1, "test values"));
        attributesCreateInfoList.add(new IssueAttributesBasicInfo(fakeCategoryManagePort.lastId() + 2, "test values"));

        assertThatException().isThrownBy(() -> issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Test case management issue",
                        "I consider about organizing practical test cases."
                )
            )
        ).isInstanceOf(InvalidInputException.class);

    }

    @Test
    void creating_an_issue_for_a_non_existent_process_fails() {

        User user = fakeUserPort.loadLoginUser("tester1").get();
        Long processId = fakeProcessPort.lastId() + 1;

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        assertThatException().isThrownBy(() -> issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        processId,
                        attributesCreateInfoList,
                        "Test case management issue",
                        "I consider about organizing practical test cases."
                )
            )
        ).isInstanceOf(ProcessNotFoundException.class);

    }

    @Test
    void if_you_create_an_issue_without_registering_a_step_to_the_process__it_fails() {

        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        assertThatException().isThrownBy(() -> issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Test case management issue",
                        "I consider about organizing practical test cases."
                )
            )
        ).isInstanceOf(EmptyProcessException.class);

    }

    @Test
    void issue_fix_success_verification() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        IssueCreateCommand issueCreateCommand = new IssueCreateCommand(
                fakeCategoryManagePort.lastId(),
                process.getProcessId(),
                attributesCreateInfoList,
                "Test case management issue",
                "I consider about organizing practical test cases."
        );

        issueService.createIssue(
                user.getUserId(),
                issueCreateCommand
        );

        Long issueId = fakeIssuePort.lastId();

        Long categoryId = fakeCategoryManagePort.lastId() - 1;
        List<IssueAttributesModifyInfo> attributesModifyInfoList = fakeIssuePort.getIssueAttributesList(user, issueId)
                .stream()
                .map(issueAttributes ->
                        new IssueAttributesModifyInfo(
                                issueAttributes.getId(),
                                issueAttributes.getAttributesId(),
                                "modified test values"
                        )
                )
                .toList();

        IssueModifyCommand issueModifyCommand = new IssueModifyCommand(
                fakeIssuePort.lastId(),
                categoryId,
                attributesModifyInfoList,
                "Modified Test case management issue",
                "I consider about organizing practical test cases. Additionally i want best cases."
        );

        assertThatNoException().isThrownBy(() -> issueService.changeIssueInfo(user.getUserId(), issueModifyCommand));

        IssueDomain issueDomain2 = fakeIssuePort.getIssue(user, issueId).get();

        assertThat(issueCreateCommand.title()).isNotEqualTo(issueDomain2.getTitle());
        assertThat(issueCreateCommand.details()).isNotEqualTo(issueDomain2.getDetails());
        assertThat(issueCreateCommand.categoryId()).isNotEqualTo(issueDomain2.getCategoryId());

        assertThat(issueModifyCommand.title()).isEqualTo(issueDomain2.getTitle());
        assertThat(issueModifyCommand.details()).isEqualTo(issueDomain2.getDetails());
        assertThat(issueModifyCommand.categoryId()).isEqualTo(issueDomain2.getCategoryId ());

    }

    @Test
    void modifying_an_issue_with_invalid_information_fails() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        IssueCreateCommand issueCreateCommand = new IssueCreateCommand(
                fakeCategoryManagePort.lastId(),
                process.getProcessId(),
                attributesCreateInfoList,
                "Test case management issue",
                "I consider about organizing practical test cases."
        );

        issueService.createIssue(
                user.getUserId(),
                issueCreateCommand
        );

        Long issueId = fakeIssuePort.lastId();

        invalidCase1(user, issueId);
        invalidCase2(user, issueId);
        invalidCase3(user, issueId);
    }

    private void invalidCase1(User user, Long issueId) {
        // case #1 : invalid category
        Long categoryId = fakeCategoryManagePort.lastId() + 3;
        List<IssueAttributesModifyInfo> attributesModifyInfoList = fakeIssuePort.getIssueAttributesList(user, issueId)
                .stream()
                .map(issueAttributes ->
                        new IssueAttributesModifyInfo(
                                issueAttributes.getId(),
                                issueAttributes.getAttributesId(),
                                "modified test values"
                        )
                )
                .toList();

        IssueModifyCommand issueModifyCommand = new IssueModifyCommand(
                fakeIssuePort.lastId(),
                categoryId,
                attributesModifyInfoList,
                "Modified Test case management issue",
                "I consider about organizing practical test cases. Additionally i want best cases."
        );

        assertThatException().isThrownBy(() -> issueService.changeIssueInfo(user.getUserId(), issueModifyCommand))
                .isInstanceOf(InvalidInputException.class);
    }

    private void invalidCase2(User user, Long issueId) {
        // case #2 : invalid attributes
        Long categoryId = fakeCategoryManagePort.lastId();
        List<IssueAttributesModifyInfo> attributesModifyInfoList = fakeIssuePort.getIssueAttributesList(user, issueId)
                .stream()
                .map(issueAttributes ->
                        new IssueAttributesModifyInfo(
                                issueAttributes.getId(),
                                issueAttributes.getAttributesId() + 100,
                                "modified test values"
                        )
                )
                .toList();

        IssueModifyCommand issueModifyCommand = new IssueModifyCommand(
                fakeIssuePort.lastId(),
                categoryId,
                attributesModifyInfoList,
                "Modified Test case management issue",
                "I consider about organizing practical test cases. Additionally i want best cases."
        );

        assertThatException().isThrownBy(() -> issueService.changeIssueInfo(user.getUserId(), issueModifyCommand))
                .isInstanceOf(InvalidInputException.class);
    }

    private void invalidCase3(User user, Long issueId) {
        // case #3 : invalid title
        Long categoryId = fakeCategoryManagePort.lastId();
        List<IssueAttributesModifyInfo> attributesModifyInfoList = fakeIssuePort.getIssueAttributesList(user, issueId)
                .stream()
                .map(issueAttributes ->
                        new IssueAttributesModifyInfo(
                                issueAttributes.getId(),
                                issueAttributes.getAttributesId(),
                                "modified test values"
                        )
                )
                .toList();

        IssueModifyCommand issueModifyCommand = new IssueModifyCommand(
                fakeIssuePort.lastId(),
                categoryId,
                attributesModifyInfoList,
                " ",
                "I consider about organizing practical test cases. Additionally i want best cases."
        );

        assertThatException().isThrownBy(() -> issueService.changeIssueInfo(user.getUserId(), issueModifyCommand))
                .isInstanceOf(InvalidInputException.class);
    }

    @Test
    void verification_of_issue_s_process_success() {

        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Test case management issue",
                        "I consider about organizing practical test cases."
                )
        );

        Long issueId = fakeIssuePort.lastId();

        assertThatNoException().isThrownBy(() -> issueService.proceedIssue(user.getUserId(), issueId));
    }

    @Test
    void if_the_issue_s_process_s_current_step_is_the_last__the_process_fails() {

        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Test case management issue",
                        "I consider about organizing practical test cases."
                )
        );

        Long issueId = fakeIssuePort.lastId();

        // order 1 -> 2
        issueService.proceedIssue(user.getUserId(), issueId);
        // order 2 -> 3
        issueService.proceedIssue(user.getUserId(), issueId);
        // order 3 -> 4
        issueService.proceedIssue(user.getUserId(), issueId);
        // order 4 -> 5 (last)
        issueService.proceedIssue(user.getUserId(), issueId);

        assertThatException().isThrownBy(() -> issueService.proceedIssue(user.getUserId(), issueId))
                .isInstanceOf(CannotProceedNextStepException.class);
    }

    @Test
    void issue_status_change_success_verification() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Test case management issue",
                        "I consider about organizing practical test cases."
                )
        );

        Long issueId = fakeIssuePort.lastId();

        assertThatNoException().isThrownBy(() -> issueService.changeStatus(user.getUserId(), issueId, IssueStatus.HIDDEN));
        assertThatNoException().isThrownBy(() -> issueService.changeStatus(user.getUserId(), issueId, IssueStatus.HANDLING));
    }

    @Test
    void if_you_change_the_issue_status_to_EXIT__the_modification_fails() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Test case management issue",
                        "I consider about organizing practical test cases."
                )
        );

        Long issueId = fakeIssuePort.lastId();
        issueService.changeStatus(user.getUserId(), issueId, IssueStatus.EXIT);

        Long categoryId = fakeCategoryManagePort.lastId() - 1;
        List<IssueAttributesModifyInfo> attributesModifyInfoList = fakeIssuePort.getIssueAttributesList(user, issueId)
                .stream()
                .map(issueAttributes ->
                        new IssueAttributesModifyInfo(
                                issueAttributes.getId(),
                                issueAttributes.getAttributesId(),
                                "modified test values"
                        )
                )
                .toList();

        IssueModifyCommand issueModifyCommand = new IssueModifyCommand(
                fakeIssuePort.lastId(),
                categoryId,
                attributesModifyInfoList,
                "Modified Test case management issue",
                "I consider about organizing practical test cases. Additionally i want best cases."
        );

        assertThatException().isThrownBy(() -> issueService.changeIssueInfo(user.getUserId(), issueModifyCommand))
                .isInstanceOf(DisabledStatusException.class);
    }

    @Test
    void if_you_change_the_issue_status_to_PENDING__the_process_fails() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Test case management issue",
                        "I consider about organizing practical test cases."
                )
        );

        Long issueId = fakeIssuePort.lastId();
        issueService.changeStatus(user.getUserId(), issueId, IssueStatus.PENDING);

        assertThatException().isThrownBy(() -> issueService.proceedIssue(user.getUserId(), issueId))
                .isInstanceOf(DisabledStatusException.class);

        issueService.changeStatus(user.getUserId(), issueId, IssueStatus.HANDLING);
        assertThatNoException().isThrownBy(() -> issueService.proceedIssue(user.getUserId(), issueId));
    }

    @Test
    void verification_of_successful_issue_process_change() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Test case management issue",
                        "I consider about organizing practical test cases."
                )
        );

        Long issueId = fakeIssuePort.lastId();

        fakeProcessPort.createProcess(
                user,
                new ProcessCreateCommand("My second process", "", YN.Y)
        );

        ProcessDomain process2 = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();

        fakeStepPort.createStep(user, process2, new StepCreateInfo("begin", 1));
        fakeStepPort.createStep(user, process2, new StepCreateInfo("end", 2));

        assertThatNoException().isThrownBy(() -> issueService.changeProcess(user.getUserId(), issueId, process2.getProcessId()));
    }

    @Test
    void if_you_change_to_a_process_that_has_not_registered_a_step_in_the_issue__it_fails() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Test case management issue",
                        "I consider about organizing practical test cases."
                )
        );

        Long issueId = fakeIssuePort.lastId();

        fakeProcessPort.createProcess(
                user,
                new ProcessCreateCommand("My second process", "", YN.Y)
        );

        assertThatException().isThrownBy(() -> issueService.changeProcess(user.getUserId(), issueId, fakeProcessPort.lastId()))
                .isInstanceOf(EmptyProcessException.class);
    }

    @Test
    void if_you_change_the_issue_status_to_EXIT__the_process_change_fails() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Test case management issue",
                        "I consider about organizing practical test cases."
                )
        );

        Long issueId = fakeIssuePort.lastId();
        issueService.changeStatus(user.getUserId(), issueId, IssueStatus.EXIT);

        fakeProcessPort.createProcess(
                user,
                new ProcessCreateCommand("My second process", "", YN.Y)
        );

        ProcessDomain process2 = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();

        assertThatException().isThrownBy(() -> issueService.changeProcess(user.getUserId(), issueId, process2.getProcessId()))
                .isInstanceOf(DisabledStatusException.class);
    }

    @Test
    void issue_search_success_verification() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Issue domain development",
                        "test case issues"
                )
        );

        issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Dilemma domain design",
                        "considering about use case and business logic"
                )
        );

        issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Statistics Implementation",
                        "test case issues about jooq library using"
                )
        );

        IssueSearchCommand searchCommand = new IssueSearchCommand(null, null, null, null, null, null, "domain", null);
        Page<IssueSearchResponse> response = issueService.searchIssues(user.getUserId(), searchCommand.toQuery());

        assertThat(response.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    void issue_detail_view_success_verification() {
        User user = fakeUserPort.loadLoginUser("tester1").get();
        ProcessDomain process = fakeProcessPort.getProcess(user, fakeProcessPort.lastId()).get();
        createBasicSteps(user, process);

        AttributesSearchCommand attributesSearchCommand = new AttributesSearchCommand(
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<IssueAttributesBasicInfo> attributesCreateInfoList = fakeAttributeManagePort
                .searchList(attributesSearchCommand.toQuery())
                .getContent()
                .stream()
                .map(attributesManageInfoResponse ->
                        new IssueAttributesBasicInfo(attributesManageInfoResponse.attributesId(), "test values")
                )
                .toList();

        issueService.createIssue(
                user.getUserId(),
                new IssueCreateCommand(
                        fakeCategoryManagePort.lastId(),
                        process.getProcessId(),
                        attributesCreateInfoList,
                        "Dilemma domain design",
                        "considering about use case and business logic"
                )
        );

        Long issueId = fakeIssuePort.lastId();
        IssueDetailResponse detailResponse = issueService.getIssueDetails(user.getUserId(), issueId);

        assertThat(detailResponse).isNotNull();
        assertThat(detailResponse.categoryId()).isEqualTo(fakeCategoryManagePort.lastId());
        assertThat(detailResponse.processId()).isEqualTo(process.getProcessId());
        assertThat(detailResponse.title()).isEqualTo("Dilemma domain design");
        assertThat(detailResponse.details()).isEqualTo("considering about use case and business logic");
    }

}
