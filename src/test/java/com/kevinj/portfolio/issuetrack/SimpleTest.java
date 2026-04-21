package com.kevinj.portfolio.issuetrack;

import com.kevinj.portfolio.issuetrack.global.time.DateTimeFormats;
import com.kevinj.portfolio.issuetrack.global.time.DateTimeUtils;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThatNoException;

public class SimpleTest {

    @Test
    void timezone_test() {
        assertThatNoException().isThrownBy(() -> ZoneId.of("Asia/Seoul"));
    }

    @Test
    void week_parse_test() {
        String isoWeekStr = "2026-W12";
        assertThatNoException().isThrownBy(() -> DateTimeUtils.isParsable(DateTimeFormats.DEFAULT_WEEK, isoWeekStr));
    }
}
