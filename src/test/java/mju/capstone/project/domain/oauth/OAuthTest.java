package mju.capstone.project.domain.oauth;

import mju.capstone.project.domain.user.OAuthAttributes;
import mju.capstone.project.dto.oauth2.UserProfile;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ExtendWith(SpringExtension.class)
public class OAuthTest {

    @Test
    @DisplayName(value = "extract() - 사용자 정보 추출 테스트")
    public void extractTest() {
        //given
        String registrationId = "google";
        Map<String, Object> attributeMap = new ConcurrentHashMap<>();
        attributeMap.put("email", "test@test.com");
        attributeMap.put("name", "test");

        //when
        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributeMap);

        //then
        Assertions.assertThat(userProfile.getName()).isEqualTo("test");
        Assertions.assertThat(userProfile.getEmail()).isEqualTo("test@test.com");
    }
}
