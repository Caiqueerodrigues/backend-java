package webb_lanches.webb_lanches.Commons.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import webb_lanches.webb_lanches.Usuarios.UsuarioRepository;

@Service //Indica ao Spring que essa classe é um serviço
public class AuthService implements UserDetailsService {
    //UserDetailsService classe do spring própria para autenticacao
    //complete com sugestão de correção da IDE

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUser(username);
    }
    

}
