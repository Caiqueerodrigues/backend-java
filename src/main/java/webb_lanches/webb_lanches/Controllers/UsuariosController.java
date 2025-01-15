package webb_lanches.webb_lanches.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.security.auth.message.AuthException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import webb_lanches.webb_lanches.Commons.DTO.ResponseDTO;
import webb_lanches.webb_lanches.Commons.Services.TokenService;
import webb_lanches.webb_lanches.Usuarios.Usuario;
import webb_lanches.webb_lanches.Usuarios.UsuarioRepository;
import webb_lanches.webb_lanches.Usuarios.DTO.AlterarUsuarioDTO;
import webb_lanches.webb_lanches.Usuarios.DTO.DadosUsuarioDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody @Valid DadosUsuarioDTO dados) throws AuthException {
        try {
            var token = new UsernamePasswordAuthenticationToken(dados.user(), dados.password());
            var auth = manager.authenticate(token);

            Usuario usuario = (Usuario) auth.getPrincipal();

            if (!usuario.getAtivo()) {
                return ResponseEntity.status(403).body(new ResponseDTO("", "Usuário inativo, procure a administração!", "", ""));
            }
            
            var tokenJWT = tokenService.generateToken(usuario);

            return ResponseEntity.status(200).body(new ResponseDTO(tokenJWT, "", "", ""));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(e.getMessage(), "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }

    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<ResponseDTO> criarUsuario(@RequestBody @Valid DadosUsuarioDTO dados) {
        try {
            Usuario usuario = (Usuario) repository.findByUser(dados.user());

            if (usuario != null) {
                var response = new ResponseDTO("", "Usuário já cadastrado!", "", "");
                return ResponseEntity.status(409).body(response);
            }

            String encryptedPassword = passwordEncoder.encode(dados.password());
            DadosUsuarioDTO dadosUserCriptografado = new DadosUsuarioDTO(dados.user(), encryptedPassword); 
            Usuario user = new Usuario(dadosUserCriptografado);

            repository.save(user);

            return ResponseEntity.status(200).body(new ResponseDTO("", "", "Usuário cadastrado com sucesso!", ""));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }

    @PutMapping("/alterar")
    @Transactional
    public ResponseEntity<ResponseDTO> alterarUsuario(@RequestBody @Valid AlterarUsuarioDTO dados) {
        try {
            Usuario user = repository.findByIdUser(dados.idUser());
            
            if(user == null) {
                return ResponseEntity.status(200).body(new ResponseDTO("", "Dados informados incorretos!", "", ""));
            }
            String encryptedPassword = (!dados.password().isBlank()) ? passwordEncoder.encode(dados.password()) : null;
            AlterarUsuarioDTO dadosUserCriptografado = new AlterarUsuarioDTO(dados.idUser(), dados.user(), encryptedPassword, dados.ativo());
            
            user.alterarUsuario(dadosUserCriptografado);
            repository.save(user);

            return ResponseEntity.status(200).body(new ResponseDTO("", "", "Usuário alterado com sucesso!", ""));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(e.getMessage(), "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }
}
