package animalhospital.config;

import animalhospital.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;




@EnableWebSecurity
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig {

    private final MemberService memberService;



    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/member/info").hasRole("MEMBER")
                .antMatchers("/board/write").hasRole("MEMBER")
                .antMatchers("/**").permitAll()
                .antMatchers("member/login").permitAll()
                .antMatchers("board/blist").permitAll()
                .antMatchers("board/getboard").permitAll()
                .antMatchers("board/getreply").permitAll()
                .antMatchers("board/getrereply").permitAll()
                .and()
                .formLogin() // 로그인페이지 보안 설정
                .loginPage("/member/login") // 아이디 / 비밀번호를 입력받을 페이지 URL
                .loginProcessingUrl("/member/adminlogincontroller") // 로그일 처리할 URL 정의 -> loadUserByUsername
                .defaultSuccessUrl("/")// 로그인 성공시 이동할 URL
                .usernameParameter("mid") // 로그인시 아이디로 입력받을 변수명 [ 기본값 : user -> mid ]
                .passwordParameter("mpassword")// 로그인시 비밀번호로 입력받을 변수명[ 기본값 : password -> mpassword ]
                .failureUrl("/member/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(("/member/logout") ) )
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and()
                .csrf()
                .ignoringAntMatchers("/member/login")
                .ignoringAntMatchers("/member/adminlogincontroller")
                .ignoringAntMatchers("/member/delete")
                .ignoringAntMatchers("/board/write")
                .ignoringAntMatchers("/board/blist")
                .ignoringAntMatchers("/board/bdelete")
                .ignoringAntMatchers("/board/getnotice")
                .ignoringAntMatchers("/admin/noticesave")
                .ignoringAntMatchers("/admin/updatenotice")
                .ignoringAntMatchers("/admin/deletenotice")
                .ignoringAntMatchers("/special/specialanimal")
                .ignoringAntMatchers("/map/info")
                .ignoringAntMatchers("/map/addreview")
                .ignoringAntMatchers("/map/getreviewlist")
                .ignoringAntMatchers("/videosearch")
                .ignoringAntMatchers("/board/replysave")
                .ignoringAntMatchers("/board/reupdate")
                .ignoringAntMatchers("/board/rereply")
                .ignoringAntMatchers("/board/getboard")
                .ignoringAntMatchers("/board/getreply")
                .ignoringAntMatchers("/board/getrereply")
                .ignoringAntMatchers("/board/btiplist")
                .ignoringAntMatchers("/board/tipwrite")
                .ignoringAntMatchers("/board/tipupdate")
                .ignoringAntMatchers("/board/gettipboard")
                .ignoringAntMatchers("/map/rdelete")
                .ignoringAntMatchers("/map/getreview")
                .ignoringAntMatchers("/map/updatereview")
                .ignoringAntMatchers("/map/getreviewstarlist")
                .ignoringAntMatchers("/member/getitemlist")
                .ignoringAntMatchers("/member/itemsave")
                .ignoringAntMatchers("/member/itemupdate")
                .ignoringAntMatchers("/member/deleteitem")
                .ignoringAntMatchers("/member/getitem")
                .ignoringAntMatchers("/member/idcheck")
                .ignoringAntMatchers("/member/itemview{sno}")
                .ignoringAntMatchers("/board/mypetupdate")
                .ignoringAntMatchers("/member/requestsave")
                .ignoringAntMatchers("/member/msgdelete")
                .ignoringAntMatchers("/member/gettomsglist")
                .ignoringAntMatchers("/")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/error")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(memberService);

        return http.build();
    }





}