package esp.irt.Pharmacy_Medical_office_backend.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/signup", "/auth/reset-password").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/roles").permitAll()
                //.requestMatchers(HttpMethod.GET, "/api/stats/**").permitAll()
                .requestMatchers("/auth/**").authenticated()
                //.requestMatchers(HttpMethod.GET, "/medicalOffices/**", "/pharmacies/**").hasAnyRole("ADMIN", "VISITOR")
               // .requestMatchers(HttpMethod.POST, "/medicalOffices/**", "/pharmacies/**").hasRole("ADMIN")
                //.requestMatchers(HttpMethod.PUT, "/medicalOffices/**", "/pharmacies/**").hasRole("ADMIN")
                //.requestMatchers(HttpMethod.DELETE, "/medicalOffices/**", "/pharmacies/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .cors();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
}
