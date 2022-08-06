package animalhospital.domain.shop;

import animalhospital.domain.board.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<ShopEntity, Integer> {

    @Query( value = "select * from shop where itemstatus = :itemstatus" , nativeQuery = true )
    Page<ShopEntity> findByblist(int itemstatus ,Pageable pageable);

    @Query( value = "select * from shop where sno = :sno" , nativeQuery = true )
    List<ShopEntity> findbynoticelist(int sno, Pageable pageable);

    @Query( value = "select * from shop where mnoitem = :mnoitem and sno = :sno" , nativeQuery = true )
    Optional<ShopEntity> findbymnoitem(int mnoitem, @Param("sno") int sno);


}
