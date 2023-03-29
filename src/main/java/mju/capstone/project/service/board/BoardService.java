package mju.capstone.project.service.board;

import lombok.RequiredArgsConstructor;
import mju.capstone.project.domain.board.Board;
import mju.capstone.project.domain.category.Category;
import mju.capstone.project.domain.image.Image;
import mju.capstone.project.dto.board.BoardCreateDto;
import mju.capstone.project.dto.board.BoardDetailedDto;
import mju.capstone.project.dto.board.BoardEditRequestDto;
import mju.capstone.project.dto.board.BoardResponseDto;
import mju.capstone.project.dto.board.BoardUpdateResultDto;
import mju.capstone.project.exception.board.BoardNotFoundException;
import mju.capstone.project.exception.board.SerialNumberExistException;
import mju.capstone.project.exception.board.WriterNotMatchException;
import mju.capstone.project.exception.category.CategoryNotFoundException;
import mju.capstone.project.repository.board.BoardRepository;
import mju.capstone.project.repository.category.CategoryRepository;
import mju.capstone.project.service.image.FileService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    // 특정 카테고리에 해당하는 게시글들을 전부 가져옴
    @Transactional(readOnly = true)
    public List<BoardResponseDto> findBoards(Long categoryId) {
        Category findItem = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        List<Board> findList = boardRepository.findBoardsByCategory(findItem);

        if(findList.isEmpty()) throw new BoardNotFoundException();

        return findList.stream()
                .map(board -> new BoardResponseDto().toDto(board))
                .collect(Collectors.toList());
    }

    // 특정 게시글만을 확인
    @Transactional
    public BoardDetailedDto findBoard(Long boardId) {
        Board findItem = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);

        findItem.updateCount();

        return new BoardDetailedDto().toDto(findItem);
    }

    // 시리얼 번호를 통해서 게시글 유무 확인
    @Transactional(readOnly = true)
    public BoardDetailedDto findBoardBySerialNumber(String serialNumber) {
        Board findItem = boardRepository.findBoardBySerialNumber(serialNumber)
                .orElseThrow(BoardNotFoundException::new);

        return new BoardDetailedDto().toDto(findItem);
    }

    // 게시글 생성
    @Transactional
    public void makeBoard(BoardCreateDto createDto, Long categoryId) {
        String writer = SecurityContextHolder.getContext().getAuthentication().getName();
        Category findItem = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);

        List<Image> images = createDto.getFiles().stream()
                .map(file -> new Image(file.getOriginalFilename()))
                .collect(Collectors.toList());

        Board board = new Board(createDto.getTitle(), createDto.getContent(),
                writer, createDto.getItemName(), getSerialNumber(createDto.getSerialNumber()), findItem, images);

        boardRepository.save(board);

        saveImage(createDto.getFiles(), images);
    }

    @Transactional
    public String getQRCode(String serialNumber) {
        if(!serialNumber.startsWith("LOST")) throw new SerialNumberExistException();

        boardRepository.findBoardBySerialNumber(serialNumber).orElseThrow(BoardNotFoundException::new);

        return "http://chart.apis.google.com/chart?cht=qr&chl=http://localhost:8080/api/board/serial/"
                + serialNumber + "&chld=H|2&chs=144";
    }

    // 게시글 수정
    @Transactional
    public void editBoard(BoardEditRequestDto requestDto, Long id) {
        Board findItem = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        if(!checkUserValidation(findItem)) throw new WriterNotMatchException();

        BoardUpdateResultDto updateResult = findItem.updateBoard(requestDto);

        saveImage(updateResult.getFileList(), updateResult.getAddedImages());
        deleteImage(updateResult.getDeletedImages());
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long id) {
        Board findItem = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        if(!checkUserValidation(findItem)) throw new WriterNotMatchException();

        boardRepository.deleteById(id);
    }

    public String getSerialNumber(String serialNumber) {
        if(serialNumber.isEmpty()) return "LOST" + UUID.randomUUID().toString().substring(0, 11);

        return serialNumber;
    }
    public boolean checkUserValidation(Board board) {
        String loginUser = SecurityContextHolder.getContext().getAuthentication().getName();

        String writer = board.getWriter();

        return loginUser.equals(writer);
    }

    public void saveImage(List<MultipartFile> files, List<Image> images) {
        IntStream.range(0, files.size())
                .forEach(image -> fileService.saveImage(files.get(image), images.get(image).getStoredName()));
    }

    public void deleteImage(List<Image> images) {
        images.stream().forEach(i -> fileService.deleteImage(i.getStoredName()));
    }
}
