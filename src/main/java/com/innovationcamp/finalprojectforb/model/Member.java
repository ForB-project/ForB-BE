package com.innovationcamp.finalprojectforb.model;

import com.innovationcamp.finalprojectforb.dto.NicknameResDto;
import com.innovationcamp.finalprojectforb.enums.Authority;
import com.innovationcamp.finalprojectforb.model.chat.ChatMember;
import com.innovationcamp.finalprojectforb.model.chat.ChatMessage;
import com.innovationcamp.finalprojectforb.model.chat.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.List;

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

    @Column
    private String stackType;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column
    private String provider;


    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRoom;


    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMember> chatMember;


    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessage;

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

    public void saveStackType(String stackType) {
        this.stackType = stackType;
    }

    public void updateNickname(NicknameResDto requestDto) {
        this.nickname = requestDto.getNickname();
    }

}
