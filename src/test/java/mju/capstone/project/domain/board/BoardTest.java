package mju.capstone.project.domain.board;

import mju.capstone.project.domain.image.Image;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class BoardTest {

    public static Board board;

    @BeforeEach
    public void initTest() {
        board = new Board(1L, "Test Title", "Test Content", "Test Writer", 0,
                "Test ItemName", "123-456-789", 123.0, 45.6,
                null, null, null, new ArrayList<>());
    }


    @Test
    @DisplayName("updateCount() - 게시글 조회수 증가 테스트")
    public void updateCountTest() {
        //given
        int initialCount = board.getViewCount();

        //when
        board.updateCount();

        //then
        Assertions.assertThat(board.getViewCount()).isEqualTo(initialCount + 1);
    }

    @Test
    @DisplayName("addImages() - 이미지 추가 테스트")
    public void addImagesTest() {
        //given
        List<Image> images = new ArrayList<>();
        Image image1 = new Image("test1.jpeg");
        Image image2 = new Image("test2.jpeg");
        Image image3 = new Image("test3.jpeg");

        images.add(image1);
        images.add(image2);
        images.add(image3);

        //when
        board.addImages(images);

        //then
        Assertions.assertThat(board.getImages().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("deleteImages() - 이미지 삭제 테스트")
    public void deleteImages() {
        //given
        List<Image> addedImages = new ArrayList<>();
        Image image1 = new Image("test1.jpeg");
        Image image2 = new Image("test2.jpeg");
        Image image3 = new Image("test3.jpeg");
        Image image4 = new Image("test4.jpeg");
        Image image5 = new Image("test5.jpeg");
        Image image6 = new Image("test6.jpeg");

        addedImages.add(image1);
        addedImages.add(image2);
        addedImages.add(image3);
        addedImages.add(image4);
        addedImages.add(image5);
        addedImages.add(image6);

        List<Image> deleteImages = new ArrayList<>();
        deleteImages.add(image4);
        deleteImages.add(image5);
        deleteImages.add(image6);

        //when
        board.addImages(addedImages);
        board.deleteImages(deleteImages);

        //then
        Assertions.assertThat(board.getImages().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("longToImage() - 이미지 아이디 -> 이미지 변환 테스트")
    public void longToImageTest() {
        //given
        List<Image> addedImages = new ArrayList<>();
        Image image1 = new Image(1L, "test1.jpeg", "store1.jpeg", "", board);
        Image image2 = new Image(2L, "test2.jpeg", "store2.jpeg", "", board);
        Image image3 = new Image(3L, "test3.jpeg", "store3.jpeg", "", board);

        addedImages.add(image1);
        addedImages.add(image2);
        addedImages.add(image3);

        //when
        board.addImages(addedImages);
        Optional<Image> image = board.longToImage(2L);

        //then
        Assertions.assertThat(image).isPresent();
        Assertions.assertThat(image.get().getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("fileToImage() - MultipartFile -> 이미지 변환 테스트")
    public void fileToImageTest() {
        //given
        MultipartFile file = new MockMultipartFile("origin1", "origin1.jpeg",
                MediaType.IMAGE_JPEG_VALUE, "origin1".getBytes());

        //when
        Image image = board.fileToImage(file);

        //then
        Assertions.assertThat(image.getOriginName()).isEqualTo("origin1.jpeg");
    }
}
