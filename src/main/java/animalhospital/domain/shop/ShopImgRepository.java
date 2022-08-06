package animalhospital.domain.shop;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ShopImgRepository extends JpaRepository<ShopImgEntity, Integer> {


    @Query( value = "delete from shopimg where simgno = :simgno" , nativeQuery = true )
    List<ShopImgEntity> deleteimglist(int simgno);
}
