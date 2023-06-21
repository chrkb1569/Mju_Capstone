package mju.capstone.project.domain.image;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ImageTest {
    @Test
    @DisplayName(value = "extractExtension() - 확장자 추출 테스트")
    public void extractExtensionTest() {
        //given
        Image image = new Image();

        //when
        String extension = image.extractExtension("test.jpeg");

        //then
        Assertions.assertThat(extension).isEqualTo("jpeg");
    }

    @Test
    @DisplayName(value = "getStoredName() - 저장할 이름 생성 테스트")
    public void getStoredName() {
        //given
        Image image = new Image();

        //when
        String storedName = image.getStoredName("test.jpeg");

        //then
        Assertions.assertThat(storedName).isNotEqualTo("test.jpeg");
    }

    @Test
    @DisplayName(value = "validateExtension() - 확장자 유효성 테스트")
    public void validateExtensionTest() {
        //given
        Image image = new Image();

        //when
        boolean test1 = image.validateExtension("jpeg");
        boolean test2 = image.validateExtension("txt");

        //then
        Assertions.assertThat(test1).isTrue();
        Assertions.assertThat(test2).isFalse();
    }
}
