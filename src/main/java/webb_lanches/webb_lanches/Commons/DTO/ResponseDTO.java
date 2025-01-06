package webb_lanches.webb_lanches.Commons.DTO;

public record ResponseDTO(
        Object resposta,
        String msgErro,
        String msgSucesso,
        String msgAlerta
    ) {
}
