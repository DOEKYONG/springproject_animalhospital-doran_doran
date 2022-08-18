package animalhospital.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalDto {
    private String hname;
    private String hdate;
    private String hcity;
    private String haddress;
    private String htel;
    private String lat;
    private String logt;
}
