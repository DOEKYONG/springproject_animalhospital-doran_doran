package animalhospital.domain;

import animalhospital.domain.board.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity,Integer> {
    @Query( value = "select * from review where hname =:hname and hdate =:hdate" , nativeQuery = true )
    List<ReviewEntity> findBylist(@Param("hname") String hname, @Param("hdate") String hdate);
    @Query( value = "select * from review where hname =:hname and hdate =:hdate" , nativeQuery = true )
    Page<ReviewEntity> findByrlist(@Param("hname") String hname, @Param("hdate") String hdate, Pageable pageable);

}