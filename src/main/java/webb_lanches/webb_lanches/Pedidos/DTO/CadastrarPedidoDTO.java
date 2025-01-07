package webb_lanches.webb_lanches.Pedidos.DTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import webb_lanches.webb_lanches.Pedidos.Enums.TiposRetirada;

public record CadastrarPedidoDTO(
    @NotBlank
    String idPedido,

    String obs,
    
    Double preco,
    
    int quantidade,
    
    String status,
    
    Double total,
    
    String pago,
    
    Long idProduto,
    
    String idAdicional,
    
    @Enumerated(EnumType.STRING)
    TiposRetirada retirada
) {

}
