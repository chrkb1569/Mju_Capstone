package mju.capstone.project.service.board;

import lombok.RequiredArgsConstructor;
import mju.capstone.project.domain.board.Board;
import mju.capstone.project.domain.category.Category;
import mju.capstone.project.domain.category.LeafCategory;
import mju.capstone.project.domain.category.SubCategory;
import mju.capstone.project.domain.image.Image;
import mju.capstone.project.domain.user.User;
import mju.capstone.project.dto.board.*;
import mju.capstone.project.exception.board.BoardNotFoundException;
import mju.capstone.project.exception.board.SerialNumberExistException;
import mju.capstone.project.exception.board.WriterNotMatchException;
import mju.capstone.project.exception.category.CategoryNotFoundException;
import mju.capstone.project.repository.board.BoardRepository;
import mju.capstone.project.repository.category.CategoryRepository;
import mju.capstone.project.repository.category.LeafCategoryRepository;
import mju.capstone.project.repository.category.SubCategoryRepository;
import mju.capstone.project.service.image.FileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final SubCategoryRepository subCategoryRepository;
    private final LeafCategoryRepository leafCategoryRepository;
    private final FileService fileService;

    // 특정 카테고리에 해당하는 게시글들을 전부 가져옴
    @Transactional(readOnly = true)
    public BoardWithPagingResponseDto findBoards(Long categoryId, int page) {
        Page<Board> boards = makePageBoardsByCategory(categoryId, page);

        List<BoardAndImageResponseDto> result = boards.stream()
                .map(value -> new BoardAndImageResponseDto().toDto(value))
                .collect(Collectors.toList());

        return BoardWithPagingResponseDto.toDto(result, PageInfoDto.toDto(boards));
    }

    // 최근에 생성된 게시글들을 모두 조회. 최대 16개까지만 조회 가능
    @Transactional(readOnly = true)
    public List<BoardAndImageResponseDto> findNewBoards(Pageable pageable) {
        List<BoardAndImageResponseDto> recentBoards = boardRepository.findAll(pageable)
                .stream().map(value -> new BoardAndImageResponseDto().toDto(value))
                .collect(Collectors.toList());

        return recentBoards;
    }

    // 분실물 이름을 통하여 게시글 검색 - 최신순, 조회순
    @Transactional(readOnly = true)
    public BoardWithPagingResponseDto searchBoardByItem(String itemName, int page, Pageable pageable) {
        pageable.withPage(page);
        Page<Board> boards = boardRepository.findAllByItemNameContaining(itemName, pageable);
        if(boards.isEmpty()) throw new BoardNotFoundException();
        List<BoardAndImageResponseDto> result = boards.stream()
                .map(value -> new BoardAndImageResponseDto().toDto(value))
                .collect(Collectors.toList());

        return BoardWithPagingResponseDto.toDto(result, PageInfoDto.toDto(boards));
    }

    // 게시글 이름을 통하여 게시글 검색 - 최신순, 조회순
    @Transactional(readOnly = true)
    public BoardWithPagingResponseDto searchBoardByTitle(String title, int page, Pageable pageable) {
        pageable.withPage(page);
        Page<Board> boards = boardRepository.findAllByTitleContaining(title, pageable);
        if(boards.isEmpty()) throw new BoardNotFoundException();

        List<BoardAndImageResponseDto> result = boards.stream()
                .map(value -> new BoardAndImageResponseDto().toDto(value))
                .collect(Collectors.toList());

        return BoardWithPagingResponseDto.toDto(result, PageInfoDto.toDto(boards));
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
    public BoardResponseDto findBoardBySerialNumber(String serialNumber) {
        Board findItem = boardRepository.findBoardBySerialNumber(serialNumber)
                .orElseThrow(BoardNotFoundException::new);

        return new BoardResponseDto().toDto(findItem);
    }

    // 시리얼 번호를 통하여 본인의 품목인지 확인
    @Transactional(readOnly = true)
    public boolean checkItem(Long id, String serialNumber) {
        Board findBoard = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        return findBoard.getSerialNumber().equals(serialNumber);
    }

    // 게시글 생성
    @Transactional
    public BoardDetailedDto makeBoard(BoardCreateDto createDto, User user, long categoryId, long subCategoryId, long leafCategoryId) {
        String writer = user.getUsername();

        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElseThrow(CategoryNotFoundException::new);
        LeafCategory leafCategory = leafCategoryRepository.findById(leafCategoryId).orElseThrow(CategoryNotFoundException::new);

        List<Image> images = createDto.getFiles().stream()
                .map(file -> new Image(file.getOriginalFilename()))
                .collect(Collectors.toList());

        Board board = new Board(createDto.getTitle(), createDto.getContent(),
                writer, createDto.getItemName(), getSerialNumber(createDto.getSerialNumber()),
                createDto.getLatitude(), createDto.getLongitude(), category, subCategory, leafCategory, images);

        Board savedBoard = boardRepository.save(board);

        saveImage(createDto.getFiles(), images);

        return new BoardDetailedDto().toDto(savedBoard);
    }

    //QR코드 생성
    @Transactional
    public String getQRCode(String serialNumber) {
        if(!serialNumber.startsWith("LOST")) throw new SerialNumberExistException();

        Board findItem = boardRepository.findBoardBySerialNumber(serialNumber).orElseThrow(BoardNotFoundException::new);

        return "http://chart.apis.google.com/chart?cht=qr&chl=http://www.localhost:3000/products/"
                + findItem.getId() + "&chld=H|2&chs=144";
    }

    // 게시글 수정
    @Transactional
    public void editBoard(BoardEditRequestDto requestDto, User user, Long id) {
        Board findItem = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        if(!checkUserValidation(findItem, user)) throw new WriterNotMatchException();

        BoardUpdateResultDto updateResult = findItem.updateBoard(requestDto);

        saveImage(updateResult.getFileList(), updateResult.getAddedImages());
        deleteImage(updateResult.getDeletedImages());
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(User user, Long id) {
        Board findItem = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        if(!checkUserValidation(findItem, user)) throw new WriterNotMatchException();

        boardRepository.deleteById(id);
    }

    // 시리얼 번호가 존재하지 않는 게시글에 부여할 임의의 번호
    public String getSerialNumber(String serialNumber) {
        if(serialNumber.isEmpty()) return "LOST" + UUID.randomUUID().toString().substring(0, 11);

        return serialNumber;
    }

    // 게시글의 작성자와 현재 로그인한 사용자의 일치 확인
    public boolean checkUserValidation(Board board, User user) {
        String loginUser = user.getUsername();
        String writer = board.getWriter();

        return loginUser.equals(writer);
    }

    // 페이징 처리를 위한 객체를 반환
    public Page<Board> makePageBoardsByCategory(long categoryId, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("createdDate").descending());
        Category findItem = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);

        Page<Board> boards = boardRepository.findAllByCategory(findItem, pageRequest);

        if(boards.isEmpty()) throw new BoardNotFoundException();

        return boards;
    }

    // 게시글에 이미지 저장
    public void saveImage(List<MultipartFile> files, List<Image> images) {
        IntStream.range(0, files.size())
                .forEach(image -> {
                    Image imageValue = images.get(image);
                    String url = fileService.saveImage(files.get(image), imageValue.getStoredName());
                    imageValue.setAccessUrl(url);
                });
    }

    // 게시글의 이미지 삭제
    public void deleteImage(List<Image> images) {
        images.stream().forEach(i -> fileService.deleteImage(i.getStoredName()));
    }
}
