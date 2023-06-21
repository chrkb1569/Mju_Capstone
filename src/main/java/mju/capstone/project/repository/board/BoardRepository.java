package mju.capstone.project.repository.board;

import mju.capstone.project.domain.board.Board;
import mju.capstone.project.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByCategory(Category category, Pageable pageable);
    Page<Board> findAll(Pageable pageable);
    Page<Board> findAllByItemNameContaining(String itemName, Pageable pageable);
    Page<Board> findAllByTitleContaining(String title, Pageable pageable);

    Optional<Board> findBoardBySerialNumber(String serialNumber);
}
