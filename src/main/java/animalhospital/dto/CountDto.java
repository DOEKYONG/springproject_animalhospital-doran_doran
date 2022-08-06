package animalhospital.dto;

import animalhospital.domain.CountEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountDto {

    String cnum;

    int count ;

    private LocalDate createdate;


    public CountEntity toentity(){

        return CountEntity.builder()
                .cnum(this.cnum)
                .count(this.count)
                .createdate(this.createdate)
                .build();
    }
}