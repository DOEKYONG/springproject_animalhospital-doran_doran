package animalhospital.dto;

import animalhospital.domain.ReviewEntity;
import animalhospital.domain.board.BoardEntity;
import animalhospital.domain.member.MemberEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {

    private int rno;///리뷰평번호
    private String rcontent;//리뷰내용
    private MultipartFile rimg1;//후기이미지
    private MultipartFile rimg2; //영수증이미지
    private String hname;//병원이름
    private String hdate;///병원인허가일자
    private int rkind;
    private int rfac;
    private int rprice;

    public ReviewEntity toentity(){
        return ReviewEntity.builder()
                .rno(this.rno)
                .rcontent(this.rcontent)
                .hname(this.hname)
                .hdate(this.hdate)
                .rkind(this.rkind)
                .rfac(this.rfac)
                .rprice(this.rprice)
                .build();
    }

}