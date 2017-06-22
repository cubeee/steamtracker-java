package com.x7ff.steam.shared.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.common.base.CharMatcher;
import com.x7ff.steam.shared.domain.steam.SteamVanityResolve;
import com.x7ff.steam.shared.service.steam.SteamVanityResolveService;
import org.springframework.stereotype.Component;

@Component
public final class SteamUtils {
    private static final Pattern STEAM_CUSTOM_NAME_PATH = Pattern.compile("http[s]*://steamcommunity.com/id/(\\w+)[/]*");
    private static final Pattern STEAM_PROFILE_ID_PATH = Pattern.compile("http[s]*://steamcommunity.com/profiles/(\\w+)[/]*");
    private static final String EXAMPLE_STEAM_ID = "76561198055235921";

    private static InstancedSteamUtils instancedSteamUtils;

    @Inject
    private InstancedSteamUtils steamUtils;

    @Component
    private static class InstancedSteamUtils {

        private final SteamVanityResolveService steamVanityResolveService;

        @Inject
        public InstancedSteamUtils(SteamVanityResolveService steamVanityResolveService) {
            this.steamVanityResolveService = steamVanityResolveService;
        }

        private Optional<String> getIdForCustomName(String customName) {
            try {
                SteamVanityResolve resolve = steamVanityResolveService.fetch(customName);
                if (resolve == null || resolve.getResponse() == null) {
                    return Optional.empty();
                }
                SteamVanityResolve.SteamVanityResponse response = resolve.getResponse();
                if (!response.isSuccessful()) {
                    return Optional.empty();
                }
                return Optional.of(response.getSteamId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Optional.empty();
        }

    }

    private SteamUtils() {

    }

    @PostConstruct
    private void init() {
        SteamUtils.instancedSteamUtils = steamUtils;
    }

    /**
     * Attempts to get an user's id from given input which can be a profile url, id or custom name.<br/>
     * Input may not be <i>null</i> or empty
     *
     * @param input Text to be resolved into an user's id
     * @return User's resolved id or <i>empty</i> if no id has been found
     */
    public static Optional<String> resolveId(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        boolean isNumeric = CharMatcher.javaDigit().matchesAllOf(input);
        if (input.length() == EXAMPLE_STEAM_ID.length() && input.startsWith("7656119") && isNumeric) {
            // Input is a numeric steam id
            return Optional.of(input);
        }

        URLMatcherResult matcherResult = isValidPatternPath(STEAM_PROFILE_ID_PATH, input);
        if (matcherResult.isValid()) {
            // input is a default steam profile url
            return Optional.of(matcherResult.getMatcher().group(1));
        }

        // assume input is a custom steam profile name url
        matcherResult = isValidPatternPath(STEAM_CUSTOM_NAME_PATH, input);
        String customName = input;
        if (matcherResult.isValid()) {
            customName = matcherResult.getMatcher().group(1);
        }
        return getIdForCustomName(customName);
    }

    public static Optional<String> getIdForCustomName(String customName) {
        return instancedSteamUtils.getIdForCustomName(customName);
    }

    private static URLMatcherResult isValidPatternPath(Pattern pattern, String url) {
        Matcher matcher = pattern.matcher(url);
        return new URLMatcherResult(matcher.find(), matcher);
    }

    static class URLMatcherResult {

        private final boolean valid;

        private final Matcher matcher;

        public URLMatcherResult(boolean valid, Matcher matcher) {
            this.valid = valid;
            this.matcher = matcher;
        }

        public boolean isValid() {
            return valid;
        }

        public Matcher getMatcher() {
            return matcher;
        }

    }

}