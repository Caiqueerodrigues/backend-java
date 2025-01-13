package webb_lanches.webb_lanches.Pedidos.DTO;

import java.util.List;

import webb_lanches.webb_lanches.Produtos.Adicional;

public record ItemsEdicaoPedidoId(
    Long id,
    List<Adicional> adicionais,
    String idAdicional,
    Long idProduto,
    String nomeProduto,
    String obs,
    String pago,
    double preco,
    int quantidade,
    String retirada,
    String status, 
    double total
) {

}
