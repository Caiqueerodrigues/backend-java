package webb_lanches.webb_lanches.Caixa.DTO;

public record ListagemPagamentos(
    String tipoPagamento,
    String valorPagamento
) {
    public String getTipoPagamento() {
        return this.tipoPagamento;
    }
    
    public String getValorPagamento() {
        return this.valorPagamento;
    }
}
