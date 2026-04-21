package com.kevinj.portfolio.issuetrack.admin;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.AttributesMapper;
import com.kevinj.portfolio.issuetrack.admin.application.AttributesManageService;
import com.kevinj.portfolio.issuetrack.admin.application.dto.AttributesCreateCommand;
import com.kevinj.portfolio.issuetrack.admin.application.dto.AttributesManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.application.dto.AttributesSearchCommand;
import com.kevinj.portfolio.issuetrack.admin.application.dto.AttributesUpdateCommand;
import com.kevinj.portfolio.issuetrack.admin.exception.DuplicateAttributesException;
import com.kevinj.portfolio.issuetrack.admin.exception.NotFoundAttributesException;
import com.kevinj.portfolio.issuetrack.admin.exception.WrongParametersInputException;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.issue.FakeIssuePort;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.*;

public class AttributesManageServiceTest {

    private final AttributesMapper attributesMapper = new AttributesMapper();
    private final FakeIssuePort fakeIssuePort = new FakeIssuePort();
    private final FakeAttributeManagePort fakeAttributeManagePort = new FakeAttributeManagePort(attributesMapper);
    private final FakeAdminUsageCheckPort fakeAdminUsageCheckPort = new FakeAdminUsageCheckPort(fakeIssuePort);
    private final AttributesManageService attributesManageService = new AttributesManageService(fakeAttributeManagePort, fakeAdminUsageCheckPort, attributesMapper);

    @Test
    void 어트리뷰트_생성_및_단건조회_검증() {
        AttributesCreateCommand createCommand = new AttributesCreateCommand("attribute 1", YN.Y);
        assertThatNoException().isThrownBy(() -> attributesManageService.createAttributeManage(createCommand));

        AttributesManageInfoResponse attributesResponse = attributesManageService.getAttributesManageInfo(fakeAttributeManagePort.lastId());
        assertThat(attributesResponse.label()).isEqualTo(createCommand.label());
        assertThat(attributesResponse.isUse()).isEqualTo(createCommand.isUse());
    }

    @Test
    void 어트리뷰트_검색_검증() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 2", YN.Y));
        attributesManageService.createAttributeManage(new AttributesCreateCommand("invalid", YN.N));

        AttributesSearchCommand searchCommand = new AttributesSearchCommand(null, null, null, null, "attribute", YN.Y);
        Page<AttributesManageInfoResponse> responseList = attributesManageService.searchAttributeManageInfo(searchCommand.toQuery());
        assertThat(responseList.getNumberOfElements()).isEqualTo(2);

        searchCommand = new AttributesSearchCommand(null, null, null, null, null, YN.N);
        responseList = attributesManageService.searchAttributeManageInfo(searchCommand.toQuery());
        assertThat(responseList.getNumberOfElements()).isEqualTo(1);

    }

    @Test
    void 어트리뷰트_수정_검증() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));
        AttributesManageInfoResponse original = attributesManageService.getAttributesManageInfo(fakeAttributeManagePort.lastId());
        AttributesUpdateCommand command = new AttributesUpdateCommand(original.attributesId(), "new label", YN.N);

        assertThatNoException().isThrownBy(() -> attributesManageService.updateAttributeManage(command));

        AttributesManageInfoResponse response = attributesManageService.getAttributesManageInfo(command.attributesId());
        assertThat(response.label()).isEqualTo(command.label());
        assertThat(response.isUse()).isEqualTo(command.isUse());
    }

    @Test
    void 어트리뷰트_삭제_검증() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));
        Long lastAttributeId = fakeAttributeManagePort.lastId();

        assertThatNoException().isThrownBy(() -> attributesManageService.deleteAttributeManage(lastAttributeId));
        assertThatException().isThrownBy(() -> attributesManageService.getAttributesManageInfo(lastAttributeId))
                .isInstanceOf(NotFoundAttributesException.class);
    }

    @Test
    void 없는_어트리뷰트_ID로_조회하면_실패한다() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));
        Long lastAttributeId = fakeAttributeManagePort.lastId() + 1;

        assertThatException().isThrownBy(() -> attributesManageService.getAttributesManageInfo(lastAttributeId))
                .isInstanceOf(NotFoundAttributesException.class);
    }

    @Test
    void 없는_어트리뷰트_ID로_수정하면_실패한다() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));
        Long lastAttributeId = fakeAttributeManagePort.lastId() + 1;

        assertThatException().isThrownBy(() ->
                        attributesManageService.updateAttributeManage(new AttributesUpdateCommand(lastAttributeId, "new label", YN.N)))
                .isInstanceOf(NotFoundAttributesException.class);
    }

    @Test
    void 없는_어트리뷰트_ID로_삭제하면_실패한다() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));
        Long lastAttributeId = fakeAttributeManagePort.lastId() + 1;

        assertThatException().isThrownBy(() -> attributesManageService.deleteAttributeManage(lastAttributeId))
                .isInstanceOf(NotFoundAttributesException.class);

    }

    @Test
    void 중복된_LABEL의_어트리뷰트를_생성하면_실패한다() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));

        assertThatException().isThrownBy(() ->
                        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y)))
                .isInstanceOf(DuplicateAttributesException.class);
    }

    @Test
    void 수정하려는_어트리뷰트의_LABEL이_중복되면_실패한다() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 2", YN.Y));

        assertThatException().isThrownBy(() ->
                        attributesManageService.updateAttributeManage(new AttributesUpdateCommand(fakeAttributeManagePort.lastId(), "attribute 1", YN.N)))
                .isInstanceOf(DuplicateAttributesException.class);

    }

    @Test
    void 유효하지_않은_어트리뷰트_생성정보로_생성하면_실패한다() {
        AttributesCreateCommand command1 = new AttributesCreateCommand(" ", YN.Y);
        assertThatException().isThrownBy(() -> attributesManageService.createAttributeManage(command1))
                .isInstanceOf(WrongParametersInputException.class);

        AttributesCreateCommand command2 = new AttributesCreateCommand("invalid", null);
        assertThatException().isThrownBy(() -> attributesManageService.createAttributeManage(command2))
                .isInstanceOf(WrongParametersInputException.class);

    }
}
