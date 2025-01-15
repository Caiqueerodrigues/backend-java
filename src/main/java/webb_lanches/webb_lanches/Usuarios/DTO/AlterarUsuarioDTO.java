package webb_lanches.webb_lanches.Usuarios.DTO;

import jakarta.validation.constraints.NotBlank;

public record AlterarUsuarioDTO(

    Long idUser,

    @NotBlank
    String user,

    @NotBlank
    String password,

    Boolean ativo
) {

}
