package webb_lanches.webb_lanches.Produtos.DTO;

import java.util.List;

public record ListagemCompletaMenu(
    List<ListagemProdutosMenu> produtos,
    List<ListagemAdicionaisMenu> adicionais
) {

}
