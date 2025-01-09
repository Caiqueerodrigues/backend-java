package webb_lanches.webb_lanches.Caixa.DTO;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record DadosRelatorioPDF(
    
    @NotBlank
    String data,

    List<ListagemPagamentos> items,
    @NotBlank
    String total
) {

}
