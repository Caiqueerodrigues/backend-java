package webb_lanches.webb_lanches.Infra;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webb_lanches.webb_lanches.Commons.Services.TokenService;
import webb_lanches.webb_lanches.Usuarios.UsuarioRepository;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        if(tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT); //pegar o subject do token
            var usuario = usuarioRepository.findByUser(subject); //buscar o usuario pelo login para dizer que esta logado

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); 

            SecurityContextHolder.getContext().setAuthentication(authentication); //setar o usuario como autenticado
        }
        
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorization = request.getHeader("Authorization"); //pegar o authorization do header
        
        if(authorization != null) {
            return authorization.replace("Bearer ", "");
        }
        
        return null;
    }
}
