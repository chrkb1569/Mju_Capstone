package mju.capstone.project.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import mju.capstone.project.dto.token.TokenDto;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider implements InitializingBean {

    public static final String AUTHORIZATION_KEY = "auth";

    private Key key;

    private final String secretKey;

    private final Long tokenValidationTime;

    private final Long refreshTokenValidationTime;

    @Override
    public void afterPropertiesSet() {
        byte[] secret_key = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(secret_key);
    }

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.tokenValidationTime}") Long tokenValidationTime) {
        this.secretKey = secretKey;
        this.tokenValidationTime = tokenValidationTime * 1000;
        this.refreshTokenValidationTime = tokenValidationTime * 1000 * 2;
    }

    public TokenDto createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        Date expirationDate = new Date(now + tokenValidationTime);
        Date refreshTokenExpirationDate = new Date(now + refreshTokenValidationTime);

        String originToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(expirationDate)
                .claim(AUTHORIZATION_KEY, authorities)
                .signWith(this.key, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpirationDate)
                .signWith(this.key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .originToken(originToken)
                .refreshToken(refreshToken)
                .type("Bearer ")
                .validateTime(tokenValidationTime)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<SimpleGrantedAuthority> authorities = Arrays
                        .stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User user = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token);
            return true;
        } catch(MalformedJwtException | SecurityException e) {
            log.info("잘못된 형식의 토큰입니다.");
        } catch(ExpiredJwtException e) {
            log.info("만료된 토큰입니다.");
        } catch(UnsupportedJwtException e) {
            log.info("지원하지 않는 형식의 토큰입니다.");
        } catch(IllegalArgumentException e) {
            log.info("잘못된 토큰입니다.");
        }
        return false;
    }
}
