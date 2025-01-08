package webb_lanches.webb_lanches.Pedidos;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webb_lanches.webb_lanches.Pedidos.DTO.PedidoDTO;

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

    @Transient
    private String nomeProduto;
    
    private Double preco;
    private int quantidade;
    private String status;
    private Double total;
    
    private String pago;
    private Long idProduto;
    private String idAdicional;
    
    private String retirada;

    public Pedido(PedidoDTO dados) {
        this.idPedido = dados.idPedido();
        this.preco = dados.preco();
        this.quantidade = dados.quantidade();
        this.total = dados.total();
        this.pago = dados.pago();
        this.idProduto = dados.idProduto();
        this.retirada = dados.retirada();
        if(dados.obs() != null) this.obs = dados.obs();
        if(dados.status() != null) this.status = dados.status();
        if(dados.idAdicional() != null) this.idAdicional = dados.idAdicional();
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }
}
