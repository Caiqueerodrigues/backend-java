package webb_lanches.webb_lanches.Usuarios.DTO;

import jakarta.validation.constraints.NotBlank;

public record DadosUsuarioDTO(
    
    @NotBlank
    String user,

    @NotBlank
    String password

) {

}
