package webb_lanches.webb_lanches.Pedidos.DTO;

public record ItemsPedidoDTO(
    Long idProduto,
    String nomeProduto,
    Double valorProduto,
    int qtd,
    String status
) {

}
