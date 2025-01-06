package webb_lanches.webb_lanches.Produtos.DTO;

public record ProdutoDTO(
    int idProduto,
    String nomeProduto,
    double preco,
    String descricao,
    String adicional
) {

}
