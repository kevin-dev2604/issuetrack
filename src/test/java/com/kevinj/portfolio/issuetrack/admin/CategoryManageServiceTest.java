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
    void 카테고리_생성_및_단건조회_검증() {
        CategoryCreateCommand createCommand = new CategoryCreateCommand(null, "Test Category", YN.Y);
        assertThatNoException().isThrownBy(() -> categoryManageService.createCategory(createCommand));

        CategoryManageInfoResponse response = categoryManageService.getCategoryManageInfo(fakeCategoryManagePort.lastId());
        assertThat(response.label()).isEqualTo(createCommand.label());
        assertThat(response.isUse()).isEqualTo(createCommand.isUse());
        assertThat(response.depth()).isEqualTo(1);
    }

    @Test
    void 카테고리_검색_검증() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Test Parent Category", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Test Child Category", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(null, "non searchable Category", YN.Y));

        CategorySearchCommand searchCommand = new CategorySearchCommand(null, null, null, null, "Test", null,  YN.Y);
        Page<CategoryManageInfoResponse> categoryList = categoryManageService.searchCategoryManageList(searchCommand.toQuery());

        assertThat(categoryList.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    void 카테고리_수정_검증() {
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
    void 카테고리_삭제_검증() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Category Level 1", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Category Level 2", YN.Y));

        Long lastCategoryId = fakeCategoryManagePort.lastId();

        assertThatNoException().isThrownBy(() -> categoryManageService.deleteCategory(lastCategoryId));
        assertThatException().isThrownBy(() -> categoryManageService.getCategoryManageInfo(lastCategoryId))
                .isInstanceOf(NotFoundCategoryException.class);
    }

    @Test
    void 없는_카테고리_ID로_조회하면_실패한다() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Category Level 1", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Category Level 2", YN.Y));

        assertThatException().isThrownBy(() -> categoryManageService.getCategoryManageInfo(3L))
                .isInstanceOf(NotFoundCategoryException.class);
    }

    @Test
    void 없는_카테고리_ID로_수정하면_실패한다() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Category Level 1", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Category Level 2", YN.Y));

        Long fakeCategoryId = fakeCategoryManagePort.lastId() + 1;

        assertThatException().isThrownBy(() -> categoryManageService.updateCategory(
                new CategoryUpdateCommand(fakeCategoryId, 1L, "Category Level 2-1", YN.Y)))
                .isInstanceOf(NotFoundCategoryException.class);
    }

    @Test
    void 없는_카테고리_ID로_삭제하면_실패한다() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Category Level 1", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Category Level 2", YN.Y));

        assertThatException().isThrownBy(() -> categoryManageService.deleteCategory(3L))
                .isInstanceOf(NotFoundCategoryException.class);
    }

    @Test
    void 부모_카테고리에_중복된_LABEL의_카테고리를_생성하면_실패한다() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Category Level 1", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Category Level 2", YN.Y));

        assertThatException().isThrownBy(() -> categoryManageService.createCategory(
                new CategoryCreateCommand(1L, "Category Level 2", YN.Y)))
                .isInstanceOf(DuplicateCategoryException.class);
    }

    @Test
    void 수정하려는_카테고리의_LABEL이_부모_카테고리_하위에서_중복되면_실패한다() {
        categoryManageService.createCategory(new CategoryCreateCommand(null, "Category Level 1", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(1L, "Category Level 2", YN.Y));
        categoryManageService.createCategory(new CategoryCreateCommand(2L, "Category Level 3", YN.Y));

        assertThatException().isThrownBy(() -> categoryManageService.updateCategory(
                new CategoryUpdateCommand(fakeCategoryManagePort.lastId(), 1L, "Category Level 2", YN.Y)))
                .isInstanceOf(DuplicateCategoryException.class);
    }

    @Test
    void 유효하지_않은_카테고리_생성정보로_생성하면_실패한다() {
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
