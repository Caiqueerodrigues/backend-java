package webb_lanches.webb_lanches.Pedidos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import webb_lanches.webb_lanches.Caixa.DTO.ListagemPagamentos;
import webb_lanches.webb_lanches.Commons.DTO.DateDTO;

public interface PedidosRepository extends JpaRepository<Pedido, Long> {
    // List<Pedido> findByidPedidoLike(String data);
    List<Pedido> findByIdPedidoContaining(String data);

    @Query(nativeQuery = true, value = "CALL todosPedidos(:data)")
    List<Pedido> findByTodosPedidos(String data);

    @Query(nativeQuery = true, value = "CALL pedidosFiltro('Fiado')")
    List<Pedido> findByPedidosFiltro();

}
