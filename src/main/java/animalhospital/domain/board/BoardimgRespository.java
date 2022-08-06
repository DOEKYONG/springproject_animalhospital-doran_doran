package animalhospital.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardimgRespository extends JpaRepository<BoardimgEntity,Integer> {

    @Query( value = "select * from boardimg where bno = :bno" , nativeQuery = true )
    List<BoardimgEntity> getboardimgEntities(int bno);
}
