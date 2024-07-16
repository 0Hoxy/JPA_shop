package com.myapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //필수는 아니지만 명시적이고 의도를 명확히 하기 위해서 추가하였음
public class SecurityConfig {

    // WebSecurityConfigurerAdapter는 Spring Security 5.7.0-M2부터 더 이상 사용되지 않게 되었다.
    // WebSecurityConfigurerAdapter을 상속하는 대신 SecurityFilterChain을 @Bean을 정의하여 보안 설정을 구성한다.

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //http 요청에 대한 보안을 설정합니다. 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정을 작성한다.
       return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //비밀번호를 데이터베이스에 그대로 저장했을 경우, 데이터베이스가 해킹당하면 고객의 회원 정보가 그대로 노출 되므로, 이를 해결하기 위해 BcryptPasswordEncoder를 @Bean으로 등록하여 사용한다.
        return new BCryptPasswordEncoder();
    }


}
