package webb_lanches.webb_lanches.Pedidos.DTO;

public record ItemsCaixaDTO(
    Long idProduto,
    String nomeProduto,
    Double valorProduto,
    int qtd,
    String status
) {

}
