package webb_lanches.webb_lanches.Pedidos.DTO;

public record PedidoDTO(
    String idPedido,

    String obs,
    
    Double preco,
    
    int quantidade,

    String nomeCliente,

    String nomeProduto,
    
    String status,
    
    Double total,
    
    String pago,
    
    Long idProduto,
    
    String idAdicional,
    
    String retirada
) {

}
