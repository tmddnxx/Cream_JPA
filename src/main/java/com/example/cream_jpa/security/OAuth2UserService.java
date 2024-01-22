package com.example.cream_jpa.security;

import com.example.cream_jpa.member.dto.MemberDTO;
import com.example.cream_jpa.member.entity.Member;
import com.example.cream_jpa.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 부모 클래스의 loadUser 메서드를 호출하여 OAuth2User 정보를 가져온다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 사용자의 역할을 나타내는 권한 정보를 생성한다.
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("user");

        // 사용자 정보에서 사용자 이름의 속성 이름을 가져온다.
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        log.info("소셜 오소 "+authorities);
        log.info("소셜 유저네임어트리뷰트 " + userNameAttributeName);
        log.info("소셜 리퀘스트 " + userRequest);
        log.info("소셜 오소투유저 " + oAuth2User);

        // OAuth2User에서 속성을 가져와 로그에 출력한다.
        Map<String , Object> attributes = oAuth2User.getAttributes();

        attributes.forEach((s, o) -> {
            log.info("s : " + s + " : " + "o : " + o);
        });
        // Kakao에서 이메일을 가져오는 메서드 호출
        String email = getKakaoEmail(attributes);

        // MemberDTO 생성 메서드 호출하여 반환
        return generatedDTO(email, userRequest.getClientRegistration().getClientName(), attributes);
    }
    
    //로그인 시 memberDTO를 반환함. 시큐리티세션에 담기도록
    private MemberDTO generatedDTO(String email, String clientName, Map<String, Object> params){
        // email 식별자 아이디 , clientName = kakao or google, params=소셜로그인 정보가 담김(아이디,연결일자,닉네임,이메일 등등)
        Optional<Member> member = memberRepository.findByMemberId(email);

        if (member.isEmpty()){
            Member newMember = Member.builder()
                    .memberId(email)
                    .passwd(passwordEncoder.encode("1111"))
                    .email(email)
                    .role("user")
                    .build();

            MemberDTO memberDTO = newMember.toDTO();

            return memberDTO;
        }else {
            MemberDTO memberDTO = member.get().toDTO();

            return memberDTO;
        }
    }


    private String getKakaoEmail(Map<String, Object> paramMap){
        // Kakao 계정 정보를 담고 있는 "kakao_account" 속성을 가져온다.
        Object value = paramMap.get("kakao_account");
        log.info(value);

        // "kakao_account" 속성을 맵 형태로 변환한다.
        LinkedHashMap accountMap = (LinkedHashMap) value;

        // 이메일 값을 가져와 반환한다.
        String email = (String) accountMap.get("email");

        log.info("email..." + email);
        return email;
    }
}
