package com.jinho.hellosecurity1.controller;

import com.jinho.hellosecurity1.model.Member;
import com.jinho.hellosecurity1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class indexController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody
    String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody
    String admin() {
        System.out.println("admin");
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody
    String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(Member member) {
        System.out.println("member = " + member);
        member.setRole("ROLE_USER");
        String rawPassword = member.getPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
        member.setPassword(encodedPassword);

        // 회원가입은 잘 되지만 시큐리티로 로그인 할 수는 없음.. 패스워드가 암호화 되어 있지 않기 때문
        // => 인코딩 과정이 필요하다.
        memberRepository.save(member);
        return "redirect:/loginForm";
    }
}
