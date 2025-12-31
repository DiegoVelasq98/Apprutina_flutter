package com.usuarios.ms_usuarios.Controller;

import com.usuarios.ms_usuarios.Entity.Usuario;
import com.usuarios.ms_usuarios.Service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public Usuario registrar(@RequestBody Usuario usuario) {
        return service.registrar(usuario);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> datos) {
        String token = service.login(datos.get("email"), datos.get("password"));
        return Map.of("token", token);
    }

    @GetMapping("/profile")
    public Usuario perfil(@RequestHeader("Authorization") String header) {
        String token = header.replace("Bearer ", "");
        String email = service.getJwtUtils().extractEmail(token);
        return service.obtenerPorEmail(email);
    }
}