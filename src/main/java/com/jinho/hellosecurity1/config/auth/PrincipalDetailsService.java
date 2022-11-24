package com.jinho.hellosecurity1.config.auth;

import com.jinho.hellosecurity1.model.Member;
import com.jinho.hellosecurity1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// login 요청이 오면 자동으로 UserDetailService 타입으로 IoC 되어있는 loadUserByUsername이 실행된다!
// form에서 username이라는 이름을 바꾼다면 정상 작동하지 않는다;
// 이걸 해결하고 싶다면 configure에서 usernameParameter("nickname")과 같이 바꾸어 줘야 한다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private MemberRepository memberRepository;

    // 시큐리티 session 객체(내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByUsername(username);
        if (memberEntity != null) {
            return new PrincipalDetails(memberEntity);
        }
        return null;
    }
}
