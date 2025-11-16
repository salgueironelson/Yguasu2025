package bo.gob.yguasu.modules.auth.service;

import bo.gob.yguasu.modules.auth.domain.Usuario;
import bo.gob.yguasu.modules.auth.dto.LoginRequest;
import bo.gob.yguasu.modules.auth.dto.LoginResponse;
import bo.gob.yguasu.modules.auth.repository.UsuarioRepository;
import bo.gob.yguasu.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsuario(),
                        request.getClave()
                )
        );

        Usuario usuario = usuarioRepository.findByUsuarioAndEstado(request.getUsuario(), 'A')
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsuario());

        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .tipo("Bearer")
                .idUsuario(usuario.getIdUsuario())
                .usuario(usuario.getUsuario())
                .tipoUsuario(usuario.getTipoUsuario())
                .build();
    }
}
