package io.example.jwt.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author : choi-ys
 * @date : 2021/09/01 7:20 오후
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Token {

    private String accessToken;
    private String refreshToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss", timezone = "Asia/Seoul")
    private Date accessExpired;

    public Token(String accessToken, String refreshToken, Date accessExpired) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessExpired = accessExpired;
    }
}
