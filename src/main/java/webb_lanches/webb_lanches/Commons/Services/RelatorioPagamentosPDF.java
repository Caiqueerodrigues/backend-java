package webb_lanches.webb_lanches.Commons.Services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import webb_lanches.webb_lanches.Caixa.DTO.DadosRelatorioPDF;
import webb_lanches.webb_lanches.Caixa.DTO.ListagemPagamentos;
import webb_lanches.webb_lanches.Caixa.Service.CaixaService;
import webb_lanches.webb_lanches.Pedidos.Pedido;
import webb_lanches.webb_lanches.Produtos.Produto;
import webb_lanches.webb_lanches.Produtos.ProdutosRepository;

@Service
public class RelatorioPagamentosPDF {
    
    @Autowired
    private CaixaService caixaService;

    @Autowired
    private ProdutosRepository produtosRepository;
    
    public String gerarPDF(List<Pedido> dados, String data) throws IOException {

        Document relatorio = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(relatorio, byteArrayOutputStream);

        // Abrir o documento para escrever
        relatorio.open();

        Paragraph pulaLinha = new Paragraph(" ");

        String[] partesData = data.split("-");

        Paragraph dataHeader = new Paragraph();
            dataHeader.setAlignment(Element.ALIGN_CENTER);
            dataHeader.add( 
                new Chunk(
                    "DATA: " + partesData[2] + "-" + partesData[1] + "-" + partesData[0], 
                    new Font(Font.TIMES_ROMAN, 32, Font.BOLD)
                )
            );

        relatorio.add(dataHeader);
        relatorio.add(pulaLinha);
        
        List<String> nomesCampos = new ArrayList<>(Arrays.asList(
            "Nome", "Item", "QTD", "Preço", "Pgto", "Consumo", "Total Pedido"
        ));

        PdfPTable tabela = new PdfPTable(7); 
            tabela.setWidthPercentage(100); // Tamanho da tabela ocupa 100% da largura
            tabela.setSpacingBefore(10f); // Espaçamento antes da tabela
            tabela.setSpacingAfter(10f);  // Espaçamento depois da tabela
            
        for(int i = 0; i < nomesCampos.size(); i++) {
            PdfPCell celula = new PdfPCell(new Phrase(nomesCampos.get(i), new Font(Font.TIMES_ROMAN, 12, Font.BOLD)));
                celula.setPadding(10);
                celula.setBorderWidth(0);
                if(i == 0) celula.setBorderWidthLeft(2);
                if(i == nomesCampos.size() - 1) celula.setBorderWidthRight(2);
                celula.setBorderWidthTop(2);
                celula.setBorderWidthBottom(2);
                celula.setHorizontalAlignment(Element.ALIGN_CENTER);
                celula.setVerticalAlignment(Element.ALIGN_MIDDLE);

            tabela.addCell(celula);
        };

        Integer cont = 0;
        List<Produto> listaProdutos = produtosRepository.findAll();

        for (Pedido pedido : dados) {
            boolean isLastRow = cont == dados.size() - 1;
            
            tabela.addCell(createCell(pedido.getIdPedido().contains(".") ? pedido.getIdPedido().split("\\.")[0] : "", isLastRow, 2, 0));
            
            Optional<Produto> produtoNome = listaProdutos.stream()
                .filter(item -> item.getIdProduto().equals(pedido.getIdProduto()))
                .findFirst();
            String nomeProduto = produtoNome.map(Produto::getNomeProduto).orElse("Produto não encontrado");
            tabela.addCell(createCell(nomeProduto, isLastRow, 0, 0));
            
            tabela.addCell(createCell(String.valueOf(pedido.getQuantidade()), isLastRow, 0, 0));
        
            String precoTipoPagamento = !pedido.getPago().equalsIgnoreCase("Fiado") && !pedido.getPago().equalsIgnoreCase("A receber") ?
                    "R$ " + String.format("%.2f", pedido.getPreco()) :
                    "R$ - " + String.format("%.2f", pedido.getPreco());
            tabela.addCell(createCell(precoTipoPagamento, isLastRow, 0, 0));
        
            tabela.addCell(createCell(pedido.getPago(), isLastRow, 0, 0));
        
            tabela.addCell(createCell(pedido.getRetirada(), isLastRow, 0, 0));
        
            String totalText = !pedido.getPago().equalsIgnoreCase("Fiado") && !pedido.getPago().equalsIgnoreCase("A receber") ?
                    "R$ " + String.format("%.2f", (pedido.getPreco() * pedido.getQuantidade())) :
                    "R$ - " + String.format("%.2f", (pedido.getPreco() * pedido.getQuantidade()));
            tabela.addCell(createCell(totalText, isLastRow, 0, 2));
        
            cont++;
        }

        relatorio.add(tabela);

        List<ListagemPagamentos> resumoPagamentos = caixaService.getPagamentosLista(data);
        
        List<String> listaTotais = getResumoPagamentos(resumoPagamentos);

        listaTotais.forEach(tot -> {
            Paragraph textTot = new Paragraph();
                textTot.setAlignment(Element.ALIGN_RIGHT);
                textTot.setSpacingBefore(10f); 
                textTot.setSpacingAfter(10f);
                textTot.add(
                    new Chunk(
                        tot,
                        new Font(
                            Font.TIMES_ROMAN, 14, Font.NORMAL
                        )
                    )
                );

            relatorio.add(textTot);
        });

        // Fechar o documento
        relatorio.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        // Obter os bytes do PDF gerado
        return Base64.getEncoder().encodeToString(pdfBytes);
    }

    private List<String> getResumoPagamentos(List<ListagemPagamentos> resumoPagamentos) {
        AtomicReference<Double> total = new AtomicReference<>(0.0);
        AtomicReference<Integer> contador = new AtomicReference<>(0);

        List<String> listaTotais = new ArrayList();

        resumoPagamentos.forEach(pagamento -> {
            String valorDot = pagamento.getValorPagamento().replace(",", ".");
            double valor = Double.parseDouble(valorDot);
            
            if(!pagamento.getTipoPagamento().equalsIgnoreCase("Fiado") && !pagamento.getTipoPagamento().equalsIgnoreCase("A receber"))  {
                total.updateAndGet(t -> t + valor);
            } else total.updateAndGet(t -> t - valor);

            String text = !pagamento.getTipoPagamento().equalsIgnoreCase("Fiado") && !pagamento.getTipoPagamento().equalsIgnoreCase("A receber") ? 
                "Total " + pagamento.getTipoPagamento() + " R$ " + pagamento.getValorPagamento() :
                "Total " + pagamento.getTipoPagamento() + " R$ - " + pagamento.getValorPagamento();

            listaTotais.add(text);
            contador.updateAndGet(t -> t ++);
        });

        listaTotais.add("Total R$ " + String.format("%.2f", total.get()));

        return listaTotais;
    }

    private PdfPCell createCell(String content, boolean isLastRow, int borderLeft, int borderRight) {
        PdfPCell cell = new PdfPCell(new Phrase(content, new Font(Font.TIMES_ROMAN, 12)));
        cell.setPadding(10);
        cell.setBorderWidth(0);
        
        if (isLastRow) {
            cell.setBorderWidthBottom(2);
        }
        if (borderLeft > 0) {
            cell.setBorderWidthLeft(borderLeft);
        }
        if (borderRight > 0) {
            cell.setBorderWidthRight(borderRight);
        }
        
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
}
