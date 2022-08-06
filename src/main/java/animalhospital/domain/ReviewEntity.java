package animalhospital.domain;

import animalhospital.domain.member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor@AllArgsConstructor
@Builder
@Entity
@Table(name="review")
public class ReviewEntity extends BaseTime{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    private int rno;///리뷰평번호

    private String rcontent;//리뷰내용
    private String rimg1;//후기이미지
    private String rimg2; //영수증이미지
    private String hname;//병원이름
    private String hdate;///병원인허가일자
    private int rkind;
    private int rfac;
    private int rprice;

    @ManyToOne
    @JoinColumn(name = "mno")
    private MemberEntity memberEntity;
}