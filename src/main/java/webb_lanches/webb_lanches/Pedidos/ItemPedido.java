package webb_lanches.webb_lanches.Pedidos;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webb_lanches.webb_lanches.Pedidos.DTO.ItemsPedidoNovoDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idProduto")
public class ItemPedido {
    Long idProduto;
    String obs;
    Double preco;
    String status;
    Double total;
    String retirada;
    String idAdicional;
    String pago;

    public ItemPedido(ItemsPedidoNovoDTO dados) {
        this.idProduto = dados.idProduto();
        this.obs = dados.obs();
        this.preco = dados.preco();
        this.status = dados.status();
        this.total = dados.total();
        this.retirada = dados.retirada();
        this.idAdicional = dados.idAdicional();
        this.pago = dados.pago();
    }
}
