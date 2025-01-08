package webb_lanches.webb_lanches.Pedidos.DTO;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record DadosEdicaoPedidoDTO(
    
    List<Integer> deletados,
    
    @NotBlank
    String idPedido,
    
    
    String nomeCliente,
    String pago,
    String retirada,
    double total
) {

}
