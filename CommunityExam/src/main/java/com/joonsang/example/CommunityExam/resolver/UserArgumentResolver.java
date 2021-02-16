package com.joonsang.example.CommunityExam.resolver;

import com.joonsang.example.CommunityExam.annotation.SocialUser;
import com.joonsang.example.CommunityExam.entity.User;
import com.joonsang.example.CommunityExam.ouath.dto.SessionUser;
import com.joonsang.example.CommunityExam.user.SocialType;
import com.joonsang.example.CommunityExam.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import static com.joonsang.example.CommunityExam.user.SocialType.*;

/**
 * 조건에 맞는 경우(=supportsParameter)의 메소드가 있다면, 해당 메소드의 파라미터로 리졸브 !!
 * HandlerMethodArgumentResolver 는 항상 WebMvcConfigurer 의 addArgumentResolvers() 를 통해 추가.
 */
@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserRepository userRepository;

    /**
     * 지원 여부 판단
     * - 파라미터에 어노테이션 @SocialUser 인 경우 true
     * - 파라미터 클래스 타입이 User.class 인 경우 true
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(SocialUser.class) != null;
        boolean isUserClass = User.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;
    }

    /**
     * 유저 정보를 가져와 파라미터에 리졸브
     * 1. 세션에서 가져옴
     * 2. 없으면 SecurityContextHolder
     * 3. 없으면 DB 조회 후, User 객체 반환 (질문: save 를 할 필요가 있는건가 ?)
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // 세션 객체를 가져옴
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();

        // 세션에서 유저 정보를 가져옴
        SessionUser sessionUser = (SessionUser) session.getAttribute("user");
        User user = new User(sessionUser.getName(), sessionUser.getEmail(), sessionUser.getPicture());

        // User 타입의 객체 생성
        return getUser(user, session);
    }

    /**
     * 유저 정보를 가져옴
     * 1. Session
     * 2. SecurityContextHolder
     */
    private User getUser(User user, HttpSession session) {
        // 세션에 정보가 없을 경우...
        if (user == null) {
            try {
                // SecurityContextHolder 에서 OAuth2AuthenticationToken 을 가져옴
                //
                OAuth2AuthenticationToken authentication
                        = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

                // SecurityContextHolder 에서 가져온 토큰 개인정보를 Map 에 담음
                Map<String, Object> map = authentication.getPrincipal().getAttributes();

                // SecurityContextHolder 에서 가져온 토큰 정보로 getAuthorizedClientRegistrationId() 을 통해, 인증 된 소셜 미디어를 알 수 있음
                User convertUser = convertUser(authentication.getAuthorizedClientRegistrationId(), map);

                // SecurityContextHolder 에서 가져온 토큰 정보를 User 객체로 변환 후, DB 에서 조회
                user = userRepository.findByEmail(convertUser.getEmail());

                // SecurityContextHolder 에서 찾은 정보가 DB 에 없다면, 저장 !!
                if (user == null) {
                    user = userRepository.save(convertUser);
                }


                setRoleIfNotSame(user, authentication, map);
                session.setAttribute("user", user);
            } catch (ClassCastException e) {
                return user;
            }
        }
        return user;
    }

    private User convertUser(String authority, Map<String, Object> map) {
        if(FACEBOOK.equals(authority))
            return getModernUser(FACEBOOK, map);
        else if("google".equals(authority))
            return getModernUser(GOOGLE, map);
        else if(KAKAO.equals(authority))
            return getKaKaoUser(map);
        else
            return null;
    }

    private User getModernUser(SocialType socialType, Map<String, Object> map) {
        return User.builder()
                .name(String.valueOf(map.get("name")))
                .email(String.valueOf(map.get("email")))
                .pincipal(String.valueOf(map.get("id")))
                .socialType(socialType)
                .build();
    }

    private User getKaKaoUser(Map<String, Object> map) {
        Map<String, String> propertyMap = (HashMap<String, String>) map.get("properties");
        return User.builder()
                .name(propertyMap.get("nickname"))
                .email(String.valueOf(map.get("kaccount_email")))
                .pincipal(String.valueOf(map.get("id")))
                .socialType(KAKAO)
                .build();
    }

    /**
     *
     * @param user              DB 유저 정보
     * @param authentication    SecurityContextHolder 유저 정보
     * @param map               유저 정보
     */
    private void setRoleIfNotSame(User user, OAuth2AuthenticationToken authentication, Map<String, Object> map) {
        if (!authentication.getAuthorities()
                        .contains(new SimpleGrantedAuthority(user.getSocialType().getRoleType()))) {
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(map, "N/A", AuthorityUtils.createAuthorityList(user.getSocialType().getRoleType())));
        }
    }
}
