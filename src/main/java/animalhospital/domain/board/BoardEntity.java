package animalhospital.domain.board;

import animalhospital.domain.BaseTime;
import animalhospital.domain.member.MemberEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name= "board")
public class BoardEntity extends BaseTime {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int bno;
    private String btitle;
    private String bcontent;
    private int cno;

    @ManyToOne
    @JoinColumn(name = "mno")
    private MemberEntity memberEntity;

    @OneToMany( mappedBy = "boardEntity" , cascade = CascadeType.ALL )
    private List<BoardimgEntity> boardimgEntities;

    @OneToMany (mappedBy = "boardEntity", cascade = CascadeType.ALL)
    private List<ReplyEntity> replyEntities;
}
