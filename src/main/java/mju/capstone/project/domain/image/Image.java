package mju.capstone.project.domain.image;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.project.domain.board.Board;
import mju.capstone.project.exception.image.NotSupportedExtensionException;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Arrays;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String storedName;

    @Column(nullable = false)
    private String accessUrl;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    public Image(String originName) {
        this.originName = originName;
        this.storedName = getStoredName(originName);
        this.accessUrl = "";
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    private final static String supportedExtensions[] = {"jpeg", "jpg", "gif", "bmp", "png"};

    // Image Entity를 저장할 때, 의존관계를 맺기 위한 메소드
    public void setBoard(Board board) {
        if(this.board == null) this.board = board;
    }

    // 파일로부터 확장자 추출
    public String extractExtension(String filename) {
        int extensionIndex = filename.lastIndexOf('.');

        String extension = filename.substring(extensionIndex + 1);

        if(validateExtension(extension)) return extension;
        throw new NotSupportedExtensionException(extension + "는 지원하지 않는 확장자입니다.");
    }

    // 기존 파일 이름을 UUID를 사용하여 새로운 이름으로 변경, 해당 이름으로 폴더에 저장됨
    public String getStoredName(String filename) {
        String extension = extractExtension(filename);

        return UUID.randomUUID() + "." + extension;
    }

    // 파일의 확장자가 유효한지 확인
    public boolean validateExtension(String extension) {
        if(Arrays.stream(supportedExtensions).anyMatch(value -> value.equals(extension))) return true;
        return false;
    }
}
