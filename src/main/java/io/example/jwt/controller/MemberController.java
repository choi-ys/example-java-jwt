package io.example.jwt.controller;

import io.example.jwt.domain.dto.request.SignupRequest;
import io.example.jwt.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : choi-ys
 * @date : 2021/09/02 9:50 오전
 */
@RestController
@RequestMapping(
        value = "member",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("signup")
    public ResponseEntity signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(memberService.signup(signupRequest));
    }
}
