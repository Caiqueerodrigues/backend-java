package webb_lanches.webb_lanches.Pedidos.DTO;

import java.util.List;

import lombok.Getter;

public record CriarPedidoDTO(

    String nomeCliente,

    double total,

    int quantidade,

    String pago,

    List<ItemsPedidoNovoDTO> items
) {

}
