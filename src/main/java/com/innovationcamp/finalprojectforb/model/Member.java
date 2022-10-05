package com.innovationcamp.finalprojectforb.model;

import com.innovationcamp.finalprojectforb.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends Timestamped {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private String provider;

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public Member(String email, String password,String nickname, Authority authority, String provider){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.authority = authority;
        this.provider = provider;
    }

    public Long getId(Long memberId) {
        this.id = memberId;
        return memberId;
    }

}
