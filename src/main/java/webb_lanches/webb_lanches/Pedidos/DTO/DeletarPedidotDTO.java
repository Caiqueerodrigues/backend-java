package webb_lanches.webb_lanches.Pedidos.DTO;

import jakarta.validation.constraints.NotBlank;

public record DeletarPedidotDTO(
    
    @NotBlank
    String idPedido
) {

}
