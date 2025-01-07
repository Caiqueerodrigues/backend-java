package webb_lanches.webb_lanches.Pedidos;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webb_lanches.webb_lanches.Pedidos.DTO.CadastrarPedidoDTO;
import webb_lanches.webb_lanches.Pedidos.Enums.TiposRetirada;

@Table(name = "pedidos")
@Entity(name = "pedidos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idPedido;
    private String obs;
    private Double preco;
    private int quantidade;
    private String status;
    private Double total;
    
    private String pago;
    private Long idProduto;
    private String idAdicional;
    
    @Enumerated(EnumType.STRING)
    private TiposRetirada retirada;

    public Pedido(CadastrarPedidoDTO dados) {
        this.idPedido = dados.idPedido();
        this.preco = dados.preco();
        this.quantidade = dados.quantidade();
        this.total = dados.total();
        this.pago = dados.pago();
        this.idProduto = dados.idProduto();
        if(dados.obs() != null) this.obs = dados.obs();
        if(dados.status() != null) this.status = dados.status();
        if(dados.idAdicional() != null) this.idAdicional = dados.idAdicional();
    }
}
