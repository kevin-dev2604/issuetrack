package com.kevinj.portfolio.issuetrack.issue.query;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.Category;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.JpaCategoryRepository;
import com.kevinj.portfolio.issuetrack.admin.application.dto.statistics.IssueDilemmaCategoryOneDepthRatioRecordResponse;
import com.kevinj.portfolio.issuetrack.global.enums.UserRole;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.Issue;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.JpaIssueRepository;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.query.IssueQueryRepository;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.query.IssueStatisticsRepository;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueSearchCommand;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueSearchResponse;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueStatus;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.JpaProcessRepositiry;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.JpaStepRepository;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Process;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Step;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.JpaUserRepository;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class IssueQueryRepositoryTest {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaProcessRepositiry processRepositiry;

    @Autowired
    private JpaStepRepository stepRepository;

    @Autowired
    private JpaIssueRepository issueRepository;

    @Autowired
    private IssueQueryRepository issueQueryRepository;

    @Autowired
    private IssueStatisticsRepository issueStatisticsRepository;

    private void makeTestData() {
        // clear all tables
        issueRepository.deleteAll();
        stepRepository.deleteAll();
        processRepositiry.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        // category db data set-up
        categoryRepository.save(
            new Category(
                null,
                null,
                "Society",
                1,
                YN.Y
            )
        );
        Category society = categoryRepository.findByLabel("Society")
            .orElseThrow(IllegalStateException::new);

        categoryRepository.save(
            new Category(
                null,
                society,
                "Demographics",
                2,
                YN.Y
            )
        );
        Category demographics = categoryRepository.findByLabel("Demographics")
            .orElseThrow(IllegalStateException::new);

        categoryRepository.save(
            new Category(
                null,
                demographics,
                "Low Birth Rates",
                3,
                YN.Y
            )
        );
        Category lowBirthRates = categoryRepository.findByLabel("Low Birth Rates")
            .orElseThrow(IllegalStateException::new);
        categoryRepository.save(
            new Category(
                null,
                demographics,
                "Single-Person Households",
                3,
                YN.Y
            )
        );
        Category singlePersonHouseholds = categoryRepository.findByLabel("Single-Person Households")
            .orElseThrow(IllegalStateException::new);

        categoryRepository.save(
            new Category(
                null,
                society,
                "Public Safety",
                2,
                YN.Y
            )
        );
        Category publicSafety = categoryRepository.findByLabel("Public Safety")
            .orElseThrow(IllegalStateException::new);

        categoryRepository.save(
            new Category(
                null,
                publicSafety,
                "Cybercrime",
                3,
                YN.Y
            )
        );
        Category cybercrime = categoryRepository.findByLabel("Cybercrime")
            .orElseThrow(IllegalStateException::new);
        categoryRepository.save(
            new Category(
                null,
                publicSafety,
                "Public Violence & Security",
                3,
                YN.Y
            )
        );
        Category publicViolenceAndSecurity = categoryRepository.findByLabel("Public Violence & Security")
            .orElseThrow(IllegalStateException::new);

        categoryRepository.save(
            new Category(
                null,
                null,
                "Economy",
                1,
                YN.Y
            )
        );
        Category economy = categoryRepository.findByLabel("Economy")
            .orElseThrow(IllegalStateException::new);

        categoryRepository.save(
            new Category(
                null,
                economy,
                "Real Estate",
                2,
                YN.Y
            )
        );
        Category realEstate = categoryRepository.findByLabel("Real Estate")
            .orElseThrow(IllegalStateException::new);

        categoryRepository.save(
            new Category(
                null,
                realEstate,
                "Rental Fraud Prevention",
                3,
                YN.Y
            )
        );
        Category rentalFraudPrevention = categoryRepository.findByLabel("Rental Fraud Prevention")
            .orElseThrow(IllegalStateException::new);
        categoryRepository.save(
            new Category(
                null,
                realEstate,
                "Housing Affordability",
                3,
                YN.Y
            )
        );
        Category housingAffordability = categoryRepository.findByLabel("Housing Affordability")
            .orElseThrow(IllegalStateException::new);

        // user db data set-up
        userRepository.save(new Users(
            "ohmykevin",
            "qwerty12#",
            UserRole.USER,
            "kevin.j",
            "ohmy@kevinj.com",
            "account for search test cases",
            YN.Y,
            0
        ));
        Users user = userRepository.findByLoginId("ohmykevin")
            .orElseThrow(IllegalStateException::new);

        // process & step db data set-up
        processRepositiry.save(
            new Process(
                null,
                user,
                "Resolution",
                "Cannot be removed. The goal is to improve imbalances in various aspects.",
                YN.Y
            )
        );
        Process resolution = processRepositiry.findByName("Resolution")
            .orElseThrow(IllegalStateException::new);

        stepRepository.save(
            new Step(
                null,
                resolution,
                1,
                "Identify",
                YN.Y
            )
        );

        stepRepository.save(
            new Step(
                null,
                resolution,
                2,
                "Analyze",
                YN.Y
            )
        );

        stepRepository.save(
            new Step(
                null,
                resolution,
                3,
                "Strategy",
                YN.Y
            )
        );

        stepRepository.save(
            new Step(
                null,
                resolution,
                4,
                "Execute",
                YN.Y
            )
        );

        stepRepository.save(
            new Step(
                null,
                resolution,
                5,
                "Review",
                YN.Y
            )
        );

        List<Step> steps = stepRepository.findAllByProcess(resolution);
        steps.sort(Comparator.comparing(Step::getOrder));

        int titleCnt = 1;

        // issue db data set-up
        issueRepository.save(
            new Issue(
                null,
                user,
                housingAffordability,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.HANDLING
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                rentalFraudPrevention,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.PENDING
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                realEstate,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.HIDDEN
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                publicViolenceAndSecurity,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.DILEMMA
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                cybercrime,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.EXIT
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                publicSafety,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.HANDLING
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                singlePersonHouseholds,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.PENDING
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                lowBirthRates,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.HIDDEN
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                demographics,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.DILEMMA
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                housingAffordability,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.EXIT
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                rentalFraudPrevention,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.HANDLING
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                realEstate,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.PENDING
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                publicViolenceAndSecurity,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.HIDDEN
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                cybercrime,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.DILEMMA
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                publicSafety,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.EXIT
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                singlePersonHouseholds,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.HANDLING
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                lowBirthRates,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.PENDING
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                demographics,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.HIDDEN
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                housingAffordability,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.DILEMMA
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                rentalFraudPrevention,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.EXIT
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                realEstate,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.HANDLING
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                publicViolenceAndSecurity,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.PENDING
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                cybercrime,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.HIDDEN
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                publicSafety,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.DILEMMA
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                singlePersonHouseholds,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.EXIT
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                lowBirthRates,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.HANDLING
            )
        );

        issueRepository.save(
            new Issue(
                null,
                user,
                demographics,
                null,
                resolution,
                steps.get(new Random().nextInt(steps.size())),
                String.format("issue search test data #%02d",  titleCnt++),
                "for search and statistics tests",
                IssueStatus.PENDING
            )
        );
    }

    @Test
    void issue_search_test() {
        // given
        makeTestData();
        Users user = userRepository.findByLoginId("ohmykevin")
            .orElseThrow(IllegalStateException::new);
        Category singlePersonHouseholds = categoryRepository.findByLabel("Single-Person Households")
            .orElseThrow(IllegalStateException::new);

        // when
        Page<IssueSearchResponse> searchResponses = issueQueryRepository.searchIssues(
            user,
            new IssueSearchCommand(
                null,
                null,
                null,
                null,
                singlePersonHouseholds.getCategoryId(),
                null,
                null,
                null
            ).toQuery()
        );

        // then
        assertThat(searchResponses.getNumberOfElements()).isEqualTo(3);
        assertThat(searchResponses.getContent())
            .extracting(IssueSearchResponse::categoryLabel)
            .containsOnly("Single-Person Households");
    }

    @Test
    void issue_statistics_test() {
        // given
        makeTestData();

        // when
        List<IssueDilemmaCategoryOneDepthRatioRecordResponse> statisticsList =
            issueStatisticsRepository.countCategoryIssueDilemmaRatio();

        // then
        assertThat(statisticsList.size()).isEqualTo(2);
        assertThat(statisticsList)
            .extracting(IssueDilemmaCategoryOneDepthRatioRecordResponse::dilemmaRatio)
            .doesNotContain(BigDecimal.ZERO);
    }

    @Test
    void empty_search_case_test() {
        // given
        makeTestData();
        Users user = userRepository.findByLoginId("ohmykevin")
            .orElseThrow(IllegalStateException::new);
        Category economy = categoryRepository.findByLabel("Economy")
            .orElseThrow(IllegalStateException::new);

        // when
        Page<IssueSearchResponse> searchResponses = issueQueryRepository.searchIssues(
            user,
            new IssueSearchCommand(
                null,
                null,
                null,
                null,
                economy.getCategoryId(),
                null,
                null,
                null
            ).toQuery()
        );

        // then
        assertThat(searchResponses.getNumberOfElements()).isEqualTo(0);
        assertThat(searchResponses.getContent()).isNullOrEmpty();
    }
}
