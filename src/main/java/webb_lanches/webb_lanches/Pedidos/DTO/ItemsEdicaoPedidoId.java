package webb_lanches.webb_lanches.Pedidos.DTO;

import java.util.List;

public record ItemsEdicaoPedidoId(
    Long id,
    List<Integer> adicionais,
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
