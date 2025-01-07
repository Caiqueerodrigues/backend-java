package webb_lanches.webb_lanches.Pedidos.DTO;

import java.util.List;

public record ListagemPedidosCaixa(
    String nome,
    String idPedido,
    String pago,
    List<ItemsCaixaDTO> items,
    String consumo,
    Boolean showPlus,
    Boolean showObs,
    Double total
) {

    public String getIdPedido() {
        return this.idPedido;
    }

    public List<ItemsCaixaDTO> getItems() {
        return this.items;
    }

}
