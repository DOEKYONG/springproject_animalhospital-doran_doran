package animalhospital.domain.member;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="request")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private int hno;
    private String hospital;
    private String bin;
    private boolean active;
    private int mno;
    private String binimg;

}