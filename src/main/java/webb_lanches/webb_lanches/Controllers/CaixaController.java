package webb_lanches.webb_lanches.Controllers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import webb_lanches.webb_lanches.Caixa.DTO.DadosRelatorioPDF;
import webb_lanches.webb_lanches.Caixa.DTO.ListagemPagamentos;
import webb_lanches.webb_lanches.Caixa.Service.CaixaService;
import webb_lanches.webb_lanches.Commons.DTO.DateDTO;
import webb_lanches.webb_lanches.Commons.DTO.ResponseDTO;
import webb_lanches.webb_lanches.Commons.Services.RelatorioPagamentosPDF;
import webb_lanches.webb_lanches.Pedidos.Pedido;
import webb_lanches.webb_lanches.Pedidos.PedidosRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/caixa")
public class CaixaController {

    @Autowired
    private PedidosRepository pedidosRepository;

    @Autowired
    private RelatorioPagamentosPDF relatoriosService;

    @Autowired
    private CaixaService service;

    @PostMapping
    public ResponseEntity<ResponseDTO> getPagamentos(@RequestBody @Valid DateDTO data) {
        try {
            List<ListagemPagamentos> pagamentos = service.getPagamentosLista(data.data());
            if(pagamentos.size() > 0) {
                return ResponseEntity.status(200).body(new ResponseDTO(pagamentos, "", "", ""));
            } else {
                return ResponseEntity.status(200).body(new ResponseDTO("", "", "", "Não existem pedidos para esta data!"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(e.getMessage(), "Desculpe, tente novamente mais tarde!", "",""));
        }
    }

    @PostMapping("/gerar-relatorio")
    public ResponseEntity<ResponseDTO> gerarRelatorio(@RequestBody @Valid DadosRelatorioPDF dados) {
        try {

            List<Pedido> pedidos = pedidosRepository.findByIdPedidoContainingAndPagoNotNull(dados.data());
            if(pedidos.size() > 0) {
                Map<String, Pedido> pedidosAgrupados = new HashMap<>();

                // Iterando sobre todos os pedidos para realizar o agrupamento e soma das quantidades
                for (Pedido pedido : pedidos) {
                    String key = generateKey(pedido);  // Gera uma chave única baseada em idPedido, idProduto, idAdicional

                    if (pedidosAgrupados.containsKey(key)) {
                        // Se já existir um pedido com a mesma chave, soma a quantidade
                        Pedido pedidoExistente = pedidosAgrupados.get(key);
                        pedidoExistente.setQuantidade(pedidoExistente.getQuantidade() + pedido.getQuantidade());
                    } else {
                        // Se não existir, adiciona o pedido ao Map
                        pedidosAgrupados.put(key, pedido);
                    }
                }

                // Modifica a lista de pedidos original, substituindo os elementos pelo agrupamento
                pedidos.clear();
                pedidos.addAll(pedidosAgrupados.values());

                String pdfBase64 = relatoriosService.gerarPDF(pedidos, dados.data());
    
                return ResponseEntity.status(200).body(new ResponseDTO(pdfBase64, "", "",""));
            } else {
                return ResponseEntity.status(200).body(new ResponseDTO("", "", "","Não existem pedidos para esta data!"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(e.getMessage(), "Desculpe, tente novamente mais tarde!", "",""));
        }
    }

    private String generateKey(Pedido pedido) {
        // Gera uma chave única baseada nos campos idPedido, idProduto, idAdicional
        return pedido.getIdPedido() + "-" + pedido.getIdProduto() + "-" + pedido.getIdAdicional();
    }
}