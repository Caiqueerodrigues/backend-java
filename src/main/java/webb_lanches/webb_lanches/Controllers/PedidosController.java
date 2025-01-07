package webb_lanches.webb_lanches.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import webb_lanches.webb_lanches.Caixa.DTO.ListagemPagamentos;
import webb_lanches.webb_lanches.Commons.DTO.DateDTO;
import webb_lanches.webb_lanches.Commons.DTO.ResponseDTO;
import webb_lanches.webb_lanches.Pedidos.PedidosRepository;
import webb_lanches.webb_lanches.Pedidos.DTO.ItemsCaixaDTO;
import webb_lanches.webb_lanches.Pedidos.DTO.ListagemPedidosCaixa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/pedidos")
public class PedidosController {

    @Autowired
    private PedidosRepository repository;

    @PostMapping("/caixa-listagem")
    public ResponseEntity<ResponseDTO> getPedidosMenu(@RequestBody DateDTO date) {
        try {
            var pedidosRespository = (date.data() != null) ?
                repository.findByTodosPedidos(date.data()) : 
                repository.findByPedidosFiltro();

            List<ListagemPedidosCaixa> pedidos = new ArrayList();

            if(!pedidosRespository.isEmpty()) {
                pedidosRespository.forEach((pedido) -> {
                    Optional<ListagemPedidosCaixa> existeOpt = pedidos
                    .stream()
                    .filter(item -> item.getIdPedido().equals(pedido.getIdPedido()))
                    .findFirst();

                    if (existeOpt.isEmpty()) {
                        String nome = (pedido.getIdPedido() != null) ? pedido.getIdPedido().split("\\.")[0] : "";

                        ItemsCaixaDTO itemPedido = new ItemsCaixaDTO(pedido.getIdProduto(), pedido.getNomeproduto(), pedido.getPreco(), pedido.getQuantidade(), "");

                        List<ItemsCaixaDTO> items = new ArrayList();
                        items.add(itemPedido);

                        var pedidoNovo = new ListagemPedidosCaixa(
                            nome, 
                            pedido.getIdPedido(), 
                            pedido.getPago(), 
                            items, 
                            pedido.getRetirada(), 
                            (pedido.getIdAdicional() != null) ? true : false, 
                            (pedido.getObs() != null) ? true : false, 
                            pedido.getTotal()
                            );
                        
                        pedidos.add(pedidoNovo);
                    } else {
                        ItemsCaixaDTO itemPedido = new ItemsCaixaDTO(pedido.getIdProduto(), pedido.getNomeproduto(), pedido.getPreco(), pedido.getQuantidade(), "");

                        existeOpt.get().getItems().add(itemPedido);
                    }

                });
                return ResponseEntity.status(200).body(new ResponseDTO(pedidos, "", "",""));
            }

            return ResponseEntity.status(500).body(new ResponseDTO("", "", "","Não existem pedidos para esta data ainda!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(e, "Desculpe, tente novamente mais tarde!", "",""));
        }
    }
}
