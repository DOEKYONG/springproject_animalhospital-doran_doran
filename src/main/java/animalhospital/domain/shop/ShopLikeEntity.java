package animalhospital.domain.shop;


import animalhospital.domain.member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name= "shoplike")
public class ShopLikeEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int shopLikeno; // 좋아하는 상품 식별 번호

    @ManyToOne(fetch = FetchType.LAZY)
    private ShopEntity shoplike;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity memberlike;

    public ShopLikeEntity(ShopEntity shoplike, MemberEntity memberlike) {
        this.shoplike = shoplike;
        this.memberlike = memberlike;
    }

}
