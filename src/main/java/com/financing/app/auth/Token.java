package com.financing.app.auth;


import com.financing.app.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "token", schema = "public")
@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    public Long id;

    @Column(unique = true, name = "token")
    public String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    public TokenType tokenType = TokenType.BEARER;

    @Column(name = "revoked")
    public boolean revoked;

    @Column(name = "expired")
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    public Token(User user, String jwtToken, TokenType tokenType, boolean revoked, boolean expired) {
        this.user = user;
        this.token = jwtToken;
        this.tokenType = tokenType;
        this.revoked = revoked;
        this.expired = expired;
    }
}
