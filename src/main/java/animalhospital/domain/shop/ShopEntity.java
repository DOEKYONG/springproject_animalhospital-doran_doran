package animalhospital.domain.shop;

import animalhospital.domain.BaseTime;
import animalhospital.domain.board.BoardimgEntity;
import animalhospital.domain.member.MemberEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name= "shop")
public class ShopEntity extends BaseTime {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int sno; // 상품 번호
    private String stitle; // 상품 제목
    private String scontent; // 상품 내용
    private int price; // 상품 가격
    private boolean itemstatus; // 상품 상태

    @OneToMany( mappedBy = "shopEntity" , cascade = CascadeType.ALL )
    private List<ShopImgEntity> shopimgEntities;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "mnoitem" )
    private MemberEntity member;

    @Builder.Default
    @OneToMany(mappedBy = "shoplike", cascade = CascadeType.ALL)
    List<ShopLikeEntity> shopLikeEntityList = new ArrayList<>();


}
