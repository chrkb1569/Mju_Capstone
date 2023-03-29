package mju.capstone.project.repository.board;

import mju.capstone.project.domain.board.Board;
import mju.capstone.project.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findBoardsByCategory(Category category);

    Optional<Board> findBoardBySerialNumber(String serialNumber);
}
