package animalhospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@EnableJpaAuditing // JPA 매핑된 엔티티(테이블)의 변화감지 [ 웹 시작시 JPA 감지 기능 ]
@SpringBootApplication // 스프링부트의 자동 설정 , 스프링 빈(클래스 메모리) 읽기기와 생성을 모두 자동 설정
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}