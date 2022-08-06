package animalhospital.domain.shop;

import animalhospital.domain.board.BoardEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "shopimg")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShopImgEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int simgno;
    private String simg;

    @ManyToOne
    @JoinColumn( name = "sno")
    private ShopEntity shopEntity;
}
