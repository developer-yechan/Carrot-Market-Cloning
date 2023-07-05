package clone.carrotMarket.web.security;

import clone.carrotMarket.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;

    @Bean
    @Order(0)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용 안함
                .and()
                .formLogin().disable() //Form Login 안함
                .httpBasic().disable() //httpBasic 인증 비활성화
                .addFilter(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration),jwtProperties))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(authenticationConfiguration),memberRepository,jwtProperties))
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll();

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    ApiCustomLoginSuccessHandler apiCustomLoginSuccessHandler(){
        return new ApiCustomLoginSuccessHandler();
    }
}
