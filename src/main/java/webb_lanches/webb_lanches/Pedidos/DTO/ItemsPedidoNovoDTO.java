package webb_lanches.webb_lanches.Pedidos.DTO;

public record ItemsPedidoNovoDTO(
    
    Long idProduto,
    
    String obs,

    Double preco,

    String status,

    Double total,

    String retirada,
    
    String idAdicional,

    String pago
) {

}
