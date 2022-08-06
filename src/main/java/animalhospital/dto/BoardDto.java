package animalhospital.dto;

import animalhospital.domain.board.BoardEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto {

    private int bno;
    private String btitle;
    private String bcontent;
    private int cno;

    private List<MultipartFile> bimg;

    public BoardEntity toentity(){
        return BoardEntity.builder()
                .bno(this.bno)
                .btitle(this.btitle)
                .bcontent(this.bcontent)
                .cno(this.cno)
                .boardimgEntities( new ArrayList<>( ) )
                .build();
    }

}