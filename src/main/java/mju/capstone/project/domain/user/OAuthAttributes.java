package mju.capstone.project.domain.user;

import mju.capstone.project.dto.oauth2.UserProfile;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {
    GOOGLE("google", (attribute) -> {
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail((String)attribute.get("email"));
        userProfile.setName((String)attribute.get("name"));

        return userProfile;
    }),

    KAKAO("kakao", (attribute) -> {
        Map<String, Object> account = (Map)attribute.get("kakao_account");
        Map<String, String> profile = (Map)account.get("profile");

        UserProfile userProfile = new UserProfile();
        userProfile.setName(profile.get("nickname"));
        userProfile.setEmail((String)account.get("email"));

        return userProfile;
    });

    private final String registrationId;

    private final Function<Map<String, Object>, UserProfile> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, UserProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(value -> registrationId.equals(value.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
