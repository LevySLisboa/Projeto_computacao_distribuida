package com.example.Usuario.controller;

import com.example.Usuario.model.Usuario;
import com.example.Usuario.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
        if (request.getEmail() == null || request.getSenha() == null) {
            throw new RuntimeException("Por favor, preencha todos os campos obrigatórios!");
        }

        String token = authService.autenticar(request.getEmail(), request.getSenha());

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        cookie.setSecure(false); // ✅ true em produção (HTTPS)
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);

        LoginResponse resp = new LoginResponse();
        resp.setSuccess(true);
        return resp;
    }
    @PostMapping("/register")
    public Usuario register(@RequestBody Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getSenha() == null || usuario.getNome() == null) {
            throw new RuntimeException("Preencha todos os campos obrigatórios!");
        }
        return authService.registrar(usuario);
    }
    @Data
    public static class LoginRequest {
        private String email;
        private String senha;
    }

    @Data
    public static class LoginResponse {
        private boolean success;
    }
}
