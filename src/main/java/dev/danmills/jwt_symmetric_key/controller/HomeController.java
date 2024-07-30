package dev.danmills.jwt_symmetric_key.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class HomeController {


      @GetMapping("/")
      public String home(Authentication authentication) {
         return "Hello" + authentication.getName();
      }
      
}

