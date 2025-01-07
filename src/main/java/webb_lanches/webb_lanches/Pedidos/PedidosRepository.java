package webb_lanches.webb_lanches.Pedidos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import webb_lanches.webb_lanches.Caixa.DTO.GetPagamentos;
import webb_lanches.webb_lanches.Caixa.DTO.ListagemPagamentos;

public interface PedidosRepository extends JpaRepository<Pedido, Long> {
    // List<Pedido> findByidPedidoLike(String data);
    List<Pedido> findByIdPedidoContaining(String data);

}
