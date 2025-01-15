package webb_lanches.webb_lanches.Usuarios;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webb_lanches.webb_lanches.Usuarios.DTO.AlterarUsuarioDTO;
import webb_lanches.webb_lanches.Usuarios.DTO.DadosUsuarioDTO;

@Table(name = "usuarios")
@Entity(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idUser")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idUser;

    String user;

    String password;

    Boolean ativo;

    public Usuario(DadosUsuarioDTO dados) {
        this.user = dados.user();
        this.password = dados.password();
        this.ativo = true;
    }

    public void alterarUsuario(AlterarUsuarioDTO dados) {
        if(dados.user() != null) this.user = dados.user();
        if(dados.password() != null) this.password = dados.password();
        if(dados.ativo() != null) this.ativo = dados.ativo();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //controle de acesso por perfis
        return List.of(new SimpleGrantedAuthority(("ROLE_USER")));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.user;
    }
}
