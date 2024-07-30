package dev.danmills.jwt_symmetric_key.config;

import javax.crypto.spec.SecretKeySpec;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   // Pull in environment variable from application.properties
   @Value("${jwt.key}")
   private String jwtKey;


    @Bean
    UserDetailsService UserDetailsService() {
      return new InMemoryUserDetailsManager(
         User.withUsername("dmills")
            .password("{noop}password")
            .authorities("READ", "ROLE_USER")
            .build());
      
   }

   @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                     .requestMatchers("/api/auth/token").hasRole("USER")
                     .anyRequest().hasAuthority("SCOPE_READ")
                     )

            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // updated to use customizer with defaults setting due to deprecation
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .httpBasic(withDefaults())
            .build();
   }

   // Encodes JWT keys
   @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes()));
    }

    // Decodes JWT keys
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] bytes = jwtKey.getBytes();
        SecretKeySpec originalKey = new SecretKeySpec(bytes, 0, bytes.length,"RSA");
        return NimbusJwtDecoder.withSecretKey(originalKey).macAlgorithm(MacAlgorithm.HS512).build();
    }

}
