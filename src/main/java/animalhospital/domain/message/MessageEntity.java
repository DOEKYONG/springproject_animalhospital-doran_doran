package animalhospital.domain.message;

import animalhospital.domain.BaseTime;
import animalhospital.domain.member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="message")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int msgno;

    private String msg;
    private boolean isread;
    // 읽음 여부
    private int msgtype;
    // 메시지 타입 : 1번 (병원과 회원 간의 메시지)  2번 (회원과 회원 간의 메시지)

    @ManyToOne // 메시지 받는 사람
    @JoinColumn(name = "tomno")
    MemberEntity toentity;

    @ManyToOne // 병원 관리자
    @JoinColumn(name="hospital")
    MemberEntity hospitalEntity;


    @ManyToOne // 메시지 보내는 사람
    @JoinColumn(name = "frommno")
    MemberEntity fromentity;

    // 보내는 사람은 운영자로 고정되어 있으므로 굳이 컬럼으로 잡지 않음

}
