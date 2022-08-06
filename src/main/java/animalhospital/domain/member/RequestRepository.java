package animalhospital.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<RequestEntity, Integer> {

    @Query( value = "select * from request where active = false" , nativeQuery = true )
    List<RequestEntity> findBybinlist();
    RequestEntity findBymno (int mno);
    @Query( value = "select mno from request where hospital = :to" , nativeQuery = true )
    String findByhospital(String to);

    @Query( value = "select * from request where mno = :mno" , nativeQuery = true )
    Optional< RequestEntity > findbymno ( int mno );

    @Query( value = "select * from request where hospital = :hospital" , nativeQuery = true )
    Optional< RequestEntity > findbyhospital ( String hospital );

}
