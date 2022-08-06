package animalhospital.domain.shop;

import animalhospital.domain.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import animalhospital.domain.shop.ShopEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShopLikeRepository extends JpaRepository<ShopLikeEntity, Integer> {
    @Query(value = "select * from shoplike where memberlike_mno = :mno and shoplike_sno = :sno" ,nativeQuery = true)
    Optional<ShopLikeEntity> existbyMemberandShop(int mno, int sno);
}
