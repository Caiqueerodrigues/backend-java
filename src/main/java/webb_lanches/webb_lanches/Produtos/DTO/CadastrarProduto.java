package webb_lanches.webb_lanches.Produtos.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastrarProduto(
    @NotBlank
    String nomeProduto,

    String descricao,

    Boolean ativo,

    @NotNull
    int qtdProduto,

    @NotNull
    Double precoProduto,

    @NotBlank
    String categoria
) {

}
