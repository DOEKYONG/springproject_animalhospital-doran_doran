package animalhospital.dto;


import animalhospital.domain.member.Role;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private int mno;
    private String mid;
    private String mpassword;
    private String mname;
    private String memail;
    private String oauth;   // 일반회원=인증  /oauth 구분용

    @Enumerated( EnumType.STRING ) // 열거형 이름
    private Role role;

    public String getrolekey() {  // 시큐리티에서 인증허가 된 리스트에 보관하기 위해서
        return role.getKey();
    }
}
