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
    void attribute_creation_and_single_retrieval_verification() {
        AttributesCreateCommand createCommand = new AttributesCreateCommand("attribute 1", YN.Y);
        assertThatNoException().isThrownBy(() -> attributesManageService.createAttributeManage(createCommand));

        AttributesManageInfoResponse attributesResponse = attributesManageService.getAttributesManageInfo(fakeAttributeManagePort.lastId());
        assertThat(attributesResponse.label()).isEqualTo(createCommand.label());
        assertThat(attributesResponse.isUse()).isEqualTo(createCommand.isUse());
    }

    @Test
    void attribute_search_verification() {
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
    void attribute_modification_verification() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));
        AttributesManageInfoResponse original = attributesManageService.getAttributesManageInfo(fakeAttributeManagePort.lastId());
        AttributesUpdateCommand command = new AttributesUpdateCommand(original.attributesId(), "new label", YN.N);

        assertThatNoException().isThrownBy(() -> attributesManageService.updateAttributeManage(command));

        AttributesManageInfoResponse response = attributesManageService.getAttributesManageInfo(command.attributesId());
        assertThat(response.label()).isEqualTo(command.label());
        assertThat(response.isUse()).isEqualTo(command.isUse());
    }

    @Test
    void attribute_deletion_verification() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));
        Long lastAttributeId = fakeAttributeManagePort.lastId();

        assertThatNoException().isThrownBy(() -> attributesManageService.deleteAttributeManage(lastAttributeId));
        assertThatException().isThrownBy(() -> attributesManageService.getAttributesManageInfo(lastAttributeId))
                .isInstanceOf(NotFoundAttributesException.class);
    }

    @Test
    void querying_with_non_existent_attribute_id_fails() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));
        Long lastAttributeId = fakeAttributeManagePort.lastId() + 1;

        assertThatException().isThrownBy(() -> attributesManageService.getAttributesManageInfo(lastAttributeId))
                .isInstanceOf(NotFoundAttributesException.class);
    }

    @Test
    void modifying_with_a_non_existent_attribute_id_fails() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));
        Long lastAttributeId = fakeAttributeManagePort.lastId() + 1;

        assertThatException().isThrownBy(() ->
                        attributesManageService.updateAttributeManage(new AttributesUpdateCommand(lastAttributeId, "new label", YN.N)))
                .isInstanceOf(NotFoundAttributesException.class);
    }

    @Test
    void deleting_with_a_non_existent_attribute_id_fails() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));
        Long lastAttributeId = fakeAttributeManagePort.lastId() + 1;

        assertThatException().isThrownBy(() -> attributesManageService.deleteAttributeManage(lastAttributeId))
                .isInstanceOf(NotFoundAttributesException.class);

    }

    @Test
    void creating_duplicate_label_attributes_fails() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));

        assertThatException().isThrownBy(() ->
                        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y)))
                .isInstanceOf(DuplicateAttributesException.class);
    }

    @Test
    void fails_if_the_label_of_the_attribute_to_be_modified_is_duplicated() {
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 1", YN.Y));
        attributesManageService.createAttributeManage(new AttributesCreateCommand("attribute 2", YN.Y));

        assertThatException().isThrownBy(() ->
                        attributesManageService.updateAttributeManage(new AttributesUpdateCommand(fakeAttributeManagePort.lastId(), "attribute 1", YN.N)))
                .isInstanceOf(DuplicateAttributesException.class);

    }

    @Test
    void creation_fails_with_invalid_attribute_creation_information() {
        AttributesCreateCommand command1 = new AttributesCreateCommand(" ", YN.Y);
        assertThatException().isThrownBy(() -> attributesManageService.createAttributeManage(command1))
                .isInstanceOf(WrongParametersInputException.class);

        AttributesCreateCommand command2 = new AttributesCreateCommand("invalid", null);
        assertThatException().isThrownBy(() -> attributesManageService.createAttributeManage(command2))
                .isInstanceOf(WrongParametersInputException.class);

    }
}
