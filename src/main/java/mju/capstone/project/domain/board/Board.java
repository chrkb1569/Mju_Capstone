package mju.capstone.project.domain.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.project.domain.base.BaseEntity;
import mju.capstone.project.domain.category.Category;
import mju.capstone.project.domain.comment.Comment;
import mju.capstone.project.domain.image.Image;
import mju.capstone.project.dto.board.BoardEditRequestDto;
import mju.capstone.project.dto.board.BoardUpdateResultDto;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private String itemName;

    @Column
    private String serialNumber;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Builder
    public Board(String title, String content, String writer, String itemName,
                 String serialNumber, Double latitude, Double longitude, Category category, List<Image> images) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.itemName = itemName;
        this.serialNumber = serialNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.viewCount = 0;
        addImages(images);
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @Column(name = "comment_id", nullable = false)
    private List<Comment> comments = new LinkedList<>();

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @Column(name = "image_id", nullable = false)
    private List<Image> images = new LinkedList<>();

    public void updateCount() {
        this.viewCount = this.viewCount + 1;
    }

    public void addImages(List<Image> images) {
        for(Image image : images) {
            image.setBoard(this);
            this.images.add(image);
        }
    }

    public void deleteImages(List<Image> deleted) {
        deleted.stream()
                .forEach(image -> this.images.remove(image));
    }

    public BoardUpdateResultDto updateBoard(BoardEditRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();

        List<MultipartFile> addedImages = requestDto.getAddImage();
        List<Integer> deletedImages = requestDto.getDeleteImage();

        List<Image> fileImages = addedImages.stream()
                .map(file -> fileToImage(file)).collect(Collectors.toList());

        List<Image> integerImages = deletedImages.stream()
                .map(value -> integerToImage(value))
                .filter(Optional::isPresent)
                .map(value -> value.get())
                .collect(Collectors.toList());

        addImages(fileImages);
        deleteImages(integerImages);

        return new BoardUpdateResultDto(addedImages, fileImages, integerImages);
    }

    public Optional<Image> integerToImage(int intValue) {
        return this.images.stream().filter(value -> value.getId() == intValue).findFirst();
    }

    public Image fileToImage(MultipartFile file) {
        return new Image(file.getOriginalFilename());
    }
}
