package com.myapp.config;

import com.myapp.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity //필수는 아니지만 명시적이고 의도를 명확히 하기 위해서 추가하였음
public class SecurityConfig {

    // WebSecurityConfigurerAdapter는 Spring Security 5.7.0-M2부터 더 이상 사용되지 않게 되었다.
    // WebSecurityConfigurerAdapter을 상속하는 대신 SecurityFilterChain을 @Bean을 정의하여 보안 설정을 구성한다.

    @Autowired
    MemberService memberService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //http 요청에 대한 보안을 설정합니다. 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정을 작성한다.
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/members/login", "/members/new").permitAll()
                        .anyRequest().authenticated()
                )
                // 로그인 설정
                .formLogin(form -> form
                        .loginPage("/members/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("email")
                        .failureUrl("/members/login/error")
                        .permitAll()
                )
                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //비밀번호를 데이터베이스에 그대로 저장했을 경우, 데이터베이스가 해킹당하면 고객의 회원 정보가 그대로 노출 되므로, 이를 해결하기 위해 BcryptPasswordEncoder를 @Bean으로 등록하여 사용한다.
        return new BCryptPasswordEncoder();
    }


    @Bean
    //AuthenticationConfiguration을 통해 자동 구성된 AuthenticationManager를 가져옵니다.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    /* 주요 차이점과 장점:
    자동 구성 활용: 새로운 방식은 Spring Boot의 자동 구성 기능을 최대한 활용합니다. UserDetailsService와 PasswordEncoder가 빈으로 등록되어 있다면 자동으로 인식되어 사용됩니다.
    분리된 관심사: 인증 로직의 구성(UserDetailsService, PasswordEncoder 설정 등)과 AuthenticationManager 빈 생성이 분리되어, 코드의 관심사가 더 명확해집니다.
    유연성: 필요한 경우 AuthenticationManagerBuilder를 직접 주입받아 추가적인 설정을 할 수 있지만, 대부분의 경우 자동 구성으로 충분합니다.
    일관성: WebSecurityConfigurerAdapter를 사용하지 않는 새로운 방식으로 전환함으로써, Spring Security 구성 방식의 일관성을 유지할 수 있습니다.
        ** 결론적으로, 두 코드는 같은 목적(인증 관리자 설정)을 달성하지만, 새로운 방식은 Spring Security의 최신 철학과 Spring Boot의 자동 구성 기능을 더 잘 반영하고 있습니다. 이 방식을 사용하면 코드가 더 간결해지고, Spring의 자동 구성 기능을 최대한 활용할 수 있습니다. */

}
