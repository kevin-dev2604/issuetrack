package com.kevinj.portfolio.issuetrack.issue.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinj.portfolio.issuetrack.WithMockCustomUser;
import com.kevinj.portfolio.issuetrack.global.enums.UserRole;
import com.kevinj.portfolio.issuetrack.global.secutiry.SecurityConfig;
import com.kevinj.portfolio.issuetrack.global.secutiry.SecurityUserDetails;
import com.kevinj.portfolio.issuetrack.global.secutiry.SecurityUserDetailsService;
import com.kevinj.portfolio.issuetrack.issue.application.IssueUseCase;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueCreateCommand;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueModifyCommand;
import com.kevinj.portfolio.issuetrack.issue.exception.DisabledStatusException;
import com.kevinj.portfolio.issuetrack.issue.exception.IssueNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class IssueControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    SecurityUserDetailsService userDetailsService;

    @MockitoBean
    IssueUseCase issueUseCase;

    @BeforeEach
    void setUp() {
        SecurityUserDetails mockUser = new SecurityUserDetails(
            1L, "ohmykevin", "password", UserRole.USER
        );

        given(userDetailsService.loadUserByUsername(anyString())).willReturn(mockUser);
    }

    @Test
    @WithMockCustomUser(id = 100L, loginId = "oh")
    void createIssue_success() throws Exception {
        // given
        IssueCreateCommand request = new IssueCreateCommand(
            1L,
            1L,
            List.of(),
            "mock test title",
            "mock test details"
        );
        // when
        mockMvc.perform(post("/issue/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isOk());

        // then
        verify(issueUseCase).createIssue(any(), any());
    }

    @Test
    @WithMockCustomUser(id = 100L, loginId = "oh")
    void getIssueDetails_success() throws Exception {
        // given
        // when
        mockMvc.perform(get("/issue/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());

        // then
        verify(issueUseCase).getIssueDetails(any(), any());
    }

    @Test
    @WithMockCustomUser(id = 100L, loginId = "oh")
    void changeProcess_fail_when_useCase_throws_disabled_status_exception() throws Exception {
        // given
        doThrow(new DisabledStatusException())
            .when(issueUseCase).changeProcess(any(), any(), any());
        // when
        // then
        mockMvc.perform(post("/issue/1/process/3")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockCustomUser(id = 100L, loginId = "oh")
    void changeIssueInfo_fail_when_useCase_throws_not_found_exception() throws Exception {
        // given
        IssueModifyCommand modifyRequest = new IssueModifyCommand(
            1L,
            3L,
            List.of(),
            "edited title",
            "edited details"
        );
        // when
        doThrow(new IssueNotFoundException())
            .when(issueUseCase).changeIssueInfo(any(), any());
        // then
        mockMvc.perform(post("/issue/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyRequest))
            )
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }
}
