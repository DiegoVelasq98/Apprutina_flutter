package com.usuarios.ms_usuarios.Service;

import com.usuarios.ms_usuarios.Entity.Usuario;
import com.usuarios.ms_usuarios.Repository.UsuarioRepository;
import com.usuarios.ms_usuarios.Security.JwtUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository repository, JwtUtils jwtUtils) {
        this.repository = repository;
        this.jwtUtils = jwtUtils;
    }

    public Usuario registrar(Usuario u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return repository.save(u);
    }

    public String login(String email, String password) {
        Optional<Usuario> userOpt = repository.findByEmail(email);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return jwtUtils.generateToken(email);
        }
        throw new RuntimeException("Credenciales inválidas");
    }

    public Usuario obtenerPorEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public JwtUtils getJwtUtils() {
        return jwtUtils;
    }
}