package animalhospital.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity,Integer> {
    @Query( value = "select * from board where cno = :cno" , nativeQuery = true )
    Page<BoardEntity> findByblist(int cno,  Pageable pageable);

    @Query( value = "select * from board where cno = :cno" , nativeQuery = true )
    List<BoardEntity> findbynoticelist(int cno, Pageable pageable);

    BoardEntity findBybno(int bno );
}