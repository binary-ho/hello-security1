package com.jinho.hellosecurity1.config.oauth;

import com.jinho.hellosecurity1.config.auth.PrincipalDetails;
import com.jinho.hellosecurity1.config.oauth.provider.FacebookUserInfo;
import com.jinho.hellosecurity1.config.oauth.provider.GoogleUserInfo;
import com.jinho.hellosecurity1.config.oauth.provider.OAuth2UserInfo;
import com.jinho.hellosecurity1.model.Member;
import com.jinho.hellosecurity1.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public PrincipalOauth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /* 구글로 부터 받은
        userRequest 데어터에 대한 후처리를 여기에 해준다. */
    /* loadUser 메서드가 종료되면서 @AuthenticationPrincipal 어노테이션이 만들어 져서
    * 다른 곳에서 받아볼 수 있게 되는 것이다. */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest = " + userRequest);
        System.out.println("userRequest.getClientRegistration() = " + userRequest.getClientRegistration());
        System.out.println("userRequest.getAccessToken() = " + userRequest.getAccessToken());
        System.out.println("super.loadUser(userRequest).getAttributes() = " + super.loadUser(userRequest).getAttributes());

        // 유저 객체
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo;

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("미지원 소셜 로그인입니다.");
            oAuth2UserInfo = null;
        }


        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = "password";
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        Member memberEntity = memberRepository.findByUsername(username);

        if (memberEntity == null) {
            memberEntity = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(role)
                .provider(provider)
                .providerId(providerId)
                .build();

            memberRepository.save(memberEntity);
            System.out.println("회원가입해주셔서 감사합니다");
        }

        return new PrincipalDetails(memberEntity, oAuth2User.getAttributes());
    }
}
