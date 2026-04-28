package com.flippeddashboard.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SanitizationServiceTest {

    private final SanitizationService sanitizationService = new SanitizationService();

    @Test
    void sanitizeHtml_stripsScriptTag() {
        assertThat(sanitizationService.sanitizeHtml("<script>alert('xss')</script>hello")).isEqualTo("hello");
    }

    @Test
    void sanitizeHtml_stripsAnchorTag() {
        assertThat(sanitizationService.sanitizeHtml("<a href=\"evil\">click</a>")).isEqualTo("click");
    }

    @Test
    void sanitizeHtml_nullInput_returnsNull() {
        assertThat(sanitizationService.sanitizeHtml(null)).isNull();
    }

    @Test
    void sanitizeHtml_plainText_unchanged() {
        assertThat(sanitizationService.sanitizeHtml("hello world")).isEqualTo("hello world");
    }

    @Test
    void containsProfanity_cleanText_returnsFalse() {
        assertThat(sanitizationService.containsProfanity("This is a great lesson")).isFalse();
    }

    @Test
    void containsProfanity_null_returnsFalse() {
        assertThat(sanitizationService.containsProfanity(null)).isFalse();
    }

    @Test
    void containsProfanity_profaneText_returnsTrue() {
        assertThat(sanitizationService.containsProfanity("this is fucking stupid")).isTrue();
    }
}
