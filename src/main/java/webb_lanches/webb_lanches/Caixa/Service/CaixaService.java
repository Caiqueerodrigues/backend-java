package webb_lanches.webb_lanches.Caixa.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import webb_lanches.webb_lanches.Caixa.DTO.ListagemPagamentos;
import webb_lanches.webb_lanches.Pedidos.PedidosRepository;

@Service
public class CaixaService {

    @Autowired
    private PedidosRepository pedidosRepository; 

    public List<ListagemPagamentos> getPagamentosLista(String data) {
        var payments = pedidosRepository.findByIdPedidoContaining(data);

        List<ListagemPagamentos> pagamentos = new ArrayList();

        payments.forEach((day) -> {
            String tipoPagamento = day.getPago().equals("0") ? "FIADO" : day.getPago().toUpperCase();

            Optional<ListagemPagamentos> existeOpt = pagamentos
                    .stream()
                    .filter(item -> item.getTipoPagamento().equals(tipoPagamento))
                    .findFirst();

                DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("pt", "BR"));
                simbolos.setDecimalSeparator(',');
                simbolos.setGroupingSeparator('.'); // Usar ponto como separador de milhar

                DecimalFormat df = new DecimalFormat("#,##0.00", simbolos);
                String totalFormatado = df.format(day.getTotal());

            if (existeOpt.isEmpty()) {
                var novoPagamento = new ListagemPagamentos(tipoPagamento, totalFormatado);

                pagamentos.add(novoPagamento);
            } else {
                ListagemPagamentos existe = existeOpt.get();

                double novoValor = Double.parseDouble(existe.getValorPagamento().replace(",", ".")) + day.getTotal();
                String novoValorFormatado = df.format(novoValor);

                ListagemPagamentos novoPagamento = new ListagemPagamentos(
                    existe.getTipoPagamento(), 
                    novoValorFormatado
                );
                
                pagamentos.remove(existe);
                pagamentos.add(novoPagamento);
            }
        });

        return pagamentos;
    }
}
