package animalhospital.dto;

import animalhospital.domain.shop.ShopEntity;

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
public class ShopDto {

    private int sno; // 상품 번호
    private String stitle; // 상품 제목
    private String scontent; // 상품 내용
    private int price; // 상품 가격
    private boolean itemstatus; // 상품 상태
    private List<MultipartFile> simg;


    public ShopEntity toentity(){
        return ShopEntity.builder()
                .sno(this.sno)
                .scontent(this.scontent)
                .stitle(this.stitle)
                .price(this.price)
                .itemstatus(this.itemstatus)
                .shopimgEntities(new ArrayList<>())
                .build();
    }



}
