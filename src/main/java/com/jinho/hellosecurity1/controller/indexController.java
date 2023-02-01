package com.jinho.hellosecurity1.controller;

import com.jinho.hellosecurity1.config.auth.PrincipalDetails;
import com.jinho.hellosecurity1.model.Member;
import com.jinho.hellosecurity1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class indexController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(
        Authentication authentication,
        @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("/test/login ===============");

        /* Authentication에서 꺼내어 OAuth2User로 다운 캐스팅해서 접근할 수도 있고,
        * @AuthenticationPrincipal 어노테이션을 통해서도 접근할 수 있다. */
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oAuth2User.getAttributes = " + oAuth2User.getAttributes());
        System.out.println("oauth2User: " + oauth.getAttributes());

        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(
        Authentication authentication,
        @AuthenticationPrincipal PrincipalDetails userDetails) {
        System.out.println("/test/login ===============");

        /* PrincipalDetails는 UserDetails를 implements 했다. */
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principalDetails getMember() = " + principalDetails.getMember());
        System.out.println("userDetails = " + userDetails.getMember());
        return "세션 정보 확인하기";
    }

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

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody
    String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN')")
//    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody
    String data() {
        return "데이터";
    }
}
