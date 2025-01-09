package webb_lanches.webb_lanches.Produtos.DTO;

public record ProdutoDTO(
    Long idProduto,
    String nomeProduto,
    double preco,
    String descricao,
    String adicional
) {

}
