package webb_lanches.webb_lanches.Pedidos.DTO;

import java.util.List;

public record DadosListagemPedidoId(
    
    String idPedido,
    String retirada,
    String pago,
    Double total,
    String nomeCliente,
    List<ItemsListagemPedidoId> items
) {

}