package com.kevinj.portfolio.issuetrack;

import com.kevinj.portfolio.issuetrack.global.enums.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String loginId() default "test";
    UserRole role() default UserRole.USER;
    long id() default 1L; // Additional PK values, etc. required to prevent NPEs
}