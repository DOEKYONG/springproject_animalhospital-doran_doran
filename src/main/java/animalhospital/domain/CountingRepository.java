package animalhospital.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CountingRepository extends JpaRepository<CountEntity,String>  {

    @Query( value = "SELECT * FROM animalhospital.count order by createdate ", nativeQuery = true )
    List<CountEntity>getcount();
    // dto

}