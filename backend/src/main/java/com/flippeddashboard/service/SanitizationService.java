package com.flippeddashboard.service;

import com.modernmt.text.profanity.ProfanityFilter;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Service;

@Service
public class SanitizationService {

    private static final PolicyFactory HTML_POLICY = new HtmlPolicyBuilder().toFactory();
    private static final ProfanityFilter PROFANITY = new ProfanityFilter(0.5f);

    public String sanitizeHtml(String text) {
        if (text == null) return null;
        return HTML_POLICY.sanitize(text);
    }

    public boolean containsProfanity(String text) {
        if (text == null || text.isBlank()) return false;
        return PROFANITY.test("en", text);
    }
}
