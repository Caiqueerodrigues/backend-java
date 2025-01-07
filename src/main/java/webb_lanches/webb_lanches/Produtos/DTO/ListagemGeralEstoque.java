package webb_lanches.webb_lanches.Produtos.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ListagemGeralEstoque(
    Long idProduto,
    String nomeProduto,
    String descricao,
    int qtdProduto,
    int ativo,
    Double precoProduto,
    String categoria,
    String adicional
) {

}
