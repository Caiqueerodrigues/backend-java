package webb_lanches.webb_lanches.Produtos.DTO;

import java.util.List;

public record ListagemProdutosMenu(
    String categoria,
    List<ProdutoDTO> items
) {

}