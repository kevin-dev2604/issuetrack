package com.kevinj.portfolio.issuetrack.admin;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.CategoryMapper;
import com.kevinj.portfolio.issuetrack.admin.application.CategoryManageService;
import com.kevinj.portfolio.issuetrack.admin.application.CategoryManageUseCase;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryCreateCommand;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategorySearchCommand;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryUpdateCommand;
import com.kevinj.portfolio.issuetrack.admin.exception.DuplicateCategoryException;
import com.kevinj.portfolio.issuetrack.admin.exception.NotFoundCategoryException;
import com.kevinj.portfolio.issuetrack.admin.exception.WrongParametersInputException;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.issue.FakeIssuePort;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.*;

public class CategoryManageServiceTest {

    private final CategoryMapper categoryMapper = new CategoryMapper();
    private final FakeIssuePort fakeIssuePort = new FakeIssuePort();
    private final FakeCategoryManagePort fakeCategoryManagePort = new FakeCategoryManagePort(categoryMapper);
    private final FakeAdminUsageCheckPort fakeAdminUsageCheckPort = new FakeAdminUsageCheckPort(fakeIssuePort);
    private final CategoryManageUseCase categoryManageService = new CategoryManageService(fakeCategoryManagePort, fakeAdminUsageCheckPort, categoryMapper);

    @Test
    void category_creation_and_single_retrieval_verification() {
        CategoryCreateCommand createCommand = new CategoryCreateCommand(null, "Test Category", YN.Y);
        assertThatNoException().isThrownBy(() -> categoryManageService.createCategory(createCommand));

        CategoryManageInfoResponse response = categoryManageService.getCategoryManageInfo(fakeCategoryManagePort.lastId());
        assertThat(response.label()).isEqualTo(createCommand.label());
        assertThat(response.isUse()).isEqualTo(createCommand.isUse());
        assertThat(response.depth()).isEqualTo(1);
    }

    @Test
    void category_search_verification() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Test Parent Category", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Test Child Category", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(null, "non searchable Category", YN.Y));

        CategorySearchCommand searchCommand = new CategorySearchCommand(null, null, null, null, "Test", null,  YN.Y);
        Page<CategoryManageInfoResponse> categoryList = categoryManageService.searchCategoryManageList(searchCommand.toQuery());

        assertThat(categoryList.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    void category_edit_verification() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Category Level 1", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Category Level 2", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(2L, "Category Level 3", YN.Y));

        CategoryManageInfoResponse original = categoryManageService.getCategoryManageInfo(fakeCategoryManagePort.lastId());
        CategoryUpdateCommand updateCommand = new CategoryUpdateCommand(original.categoryId(), 1L, "Category Level 2-1", original.isUse());
        assertThatNoException().isThrownBy(() -> categoryManageService.updateCategory(updateCommand));

        CategoryManageInfoResponse response = categoryManageService.getCategoryManageInfo(fakeCategoryManagePort.lastId());
        assertThat(response.parentCategoryId()).isEqualTo(updateCommand.parentCategoryId());
        assertThat(response.label()).isEqualTo(updateCommand.label());
        assertThat(response.isUse()).isEqualTo(updateCommand.isUse());
    }

    @Test
    void category_deletion_verification() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Category Level 1", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Category Level 2", YN.Y));

        Long lastCategoryId = fakeCategoryManagePort.lastId();

        assertThatNoException().isThrownBy(() -> categoryManageService.deleteCategory(lastCategoryId));
        assertThatException().isThrownBy(() -> categoryManageService.getCategoryManageInfo(lastCategoryId))
                .isInstanceOf(NotFoundCategoryException.class);
    }

    @Test
    void searching_with_non_existent_category_id_fails() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Category Level 1", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Category Level 2", YN.Y));

        assertThatException().isThrownBy(() -> categoryManageService.getCategoryManageInfo(3L))
                .isInstanceOf(NotFoundCategoryException.class);
    }

    @Test
    void editing_with_a_non_existent_category_id_fails() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Category Level 1", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Category Level 2", YN.Y));

        Long fakeCategoryId = fakeCategoryManagePort.lastId() + 1;

        assertThatException().isThrownBy(() -> categoryManageService.updateCategory(
                new CategoryUpdateCommand(fakeCategoryId, 1L, "Category Level 2-1", YN.Y)))
                .isInstanceOf(NotFoundCategoryException.class);
    }

    @Test
    void deleting_with_a_non_existent_category_id_fails() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Category Level 1", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Category Level 2", YN.Y));

        assertThatException().isThrownBy(() -> categoryManageService.deleteCategory(3L))
                .isInstanceOf(NotFoundCategoryException.class);
    }

    @Test
    void creating_a_category_for_a_label_that_is_duplicated_in_the_parent_category_fails() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Category Level 1", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Category Level 2", YN.Y));

        assertThatException().isThrownBy(() -> categoryManageService.createCategory(
                new CategoryCreateCommand(1L, "Category Level 2", YN.Y)))
                .isInstanceOf(DuplicateCategoryException.class);
    }

    @Test
    void fails_if_the_label_of_the_category_to_be_modified_is_duplicated_under_the_parent_category() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Category Level 1", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Category Level 2", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(2L, "Category Level 3", YN.Y));

        assertThatException().isThrownBy(() -> categoryManageService.updateCategory(
                new CategoryUpdateCommand(fakeCategoryManagePort.lastId(), 1L, "Category Level 2", YN.Y)))
                .isInstanceOf(DuplicateCategoryException.class);
    }

    @Test
    void creation_fails_with_invalid_category_creation_information() {
        CategoryCreateCommand createCommand1 = new CategoryCreateCommand(-1L, "invalid", YN.Y);
        assertThatException().isThrownBy(() -> categoryManageService.createCategory(createCommand1))
                .isInstanceOf(WrongParametersInputException.class);

        CategoryCreateCommand createCommand2 = new CategoryCreateCommand(null, " ", YN.Y);
        assertThatException().isThrownBy(() -> categoryManageService.createCategory(createCommand2))
                .isInstanceOf(WrongParametersInputException.class);

        CategoryCreateCommand createCommand3 = new CategoryCreateCommand(null, "invalid", null);
        assertThatException().isThrownBy(() -> categoryManageService.createCategory(createCommand3))
                .isInstanceOf(WrongParametersInputException.class);
    }
}
