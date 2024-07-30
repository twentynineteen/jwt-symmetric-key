package dev.danmills.jwt_symmetric_key.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import dev.danmills.jwt_symmetric_key.service.TokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

   private final TokenService tokenService;

   public AuthController(TokenService tokenService) {
      this.tokenService = tokenService;
   }

      @PostMapping("/token")
      public String token(Authentication authentication) {
         return tokenService.generateToken(authentication);
      }
}
