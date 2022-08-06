package animalhospital.dto;

import animalhospital.domain.member.RequestEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestDto {

    private int hno; //병원 pk
    private String hname;   //병원 식별 - 병원 이름
    private String hdate;   //병원 식별 - 인허가일자
    private String hospital; //병원 식별 - 병원이름+인허가일자
    private String bin; //사업자 번호
    private boolean active; //신청상황
    private String mid;  //회원번호
    private MultipartFile binimg; //사업자등록증 사진경로

    public RequestEntity toentity() {
        return RequestEntity.builder()
                .hno( this.hno )
                .hospital(this.hospital)
                .bin(this.bin)
                .active(this.active)
                .build();
    }

}