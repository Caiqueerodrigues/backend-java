package webb_lanches.webb_lanches.Pedidos.DTO;

import java.util.List;

public record ItemsListagemPedidoId(
    
    String obs,
    double preco,
    int quantidade,
    String status,
    double total,
    String pago,
    Long idProduto,
    String idAdicional,
    String nomeProduto,
    String retirada,
    List<Long> id,
    List<String> adicionais
) {

}
