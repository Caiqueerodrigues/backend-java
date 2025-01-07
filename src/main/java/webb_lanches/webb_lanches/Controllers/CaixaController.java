package webb_lanches.webb_lanches.Controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import webb_lanches.webb_lanches.Caixa.DTO.GetPagamentos;
import webb_lanches.webb_lanches.Caixa.DTO.ListagemPagamentos;
import webb_lanches.webb_lanches.Commons.DTO.ResponseDTO;
import webb_lanches.webb_lanches.Pedidos.PedidosRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/caixa")
public class CaixaController {

    @Autowired
    private PedidosRepository pedidosRepository;

    @PostMapping
    public ResponseEntity<ResponseDTO> getPagamentos(@RequestBody @Valid GetPagamentos data) {
        try {
            var payments = pedidosRepository.findByIdPedidoContaining(data.data());

            List<ListagemPagamentos> pagamentos = new ArrayList();

            payments.forEach((day) -> {
                String tipoPagamento = day.getPago().equals("0") ? "FIADO" : day.getPago().toUpperCase();

                Optional<ListagemPagamentos> existeOpt = pagamentos
                        .stream()
                        .filter(item -> item.getTipoPagamento().equals(tipoPagamento))
                        .findFirst();

                if (existeOpt.isEmpty()) {
                    var novoPagamento = new ListagemPagamentos(tipoPagamento, day.getTotal().toString());

                    pagamentos.add(novoPagamento);
                } else {
                    ListagemPagamentos existe = existeOpt.get();
                    
                    double novoValor = Double.parseDouble(existe.getValorPagamento()) + day.getTotal();
                    ListagemPagamentos novoPagamento = new ListagemPagamentos(
                        existe.getTipoPagamento(), 
                        String.valueOf(novoValor)
                    );
                    
                    pagamentos.remove(existe);
                    pagamentos.add(novoPagamento);
                }
            });

            return ResponseEntity.status(200).body(new ResponseDTO(pagamentos, "", "", ""));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(e, "Desculpe, tente novamente mais tarde!", "",""));
        }
    }
}
