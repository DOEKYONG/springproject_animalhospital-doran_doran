package animalhospital.domain.board;

import animalhospital.domain.BaseTime;
import animalhospital.domain.member.MemberEntity;
import lombok.*;

import javax.persistence.*;
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name= "reply")
public class ReplyEntity extends BaseTime {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int rno;
    private int rindex;
    private String rcontent;

    @ManyToOne
    @JoinColumn(name = "mno")
    @ToString.Exclude private MemberEntity memberEntity;

    @ManyToOne
    @JoinColumn(name = "bno")
    @ToString.Exclude private BoardEntity boardEntity;
}