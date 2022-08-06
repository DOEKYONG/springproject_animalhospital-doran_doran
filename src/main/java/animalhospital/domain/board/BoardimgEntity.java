package animalhospital.domain.board;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "boardimg")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString( exclude="boardEntity" )@Builder
public class BoardimgEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int bimgno;
    private String bimg;

    @ManyToOne
    @JoinColumn( name = "bno")
    private BoardEntity boardEntity;
}