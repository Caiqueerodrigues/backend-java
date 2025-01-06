package webb_lanches.webb_lanches.Produtos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webb_lanches.webb_lanches.Produtos.DTO.AlterarProduto;
import webb_lanches.webb_lanches.Produtos.DTO.CadastrarProduto;

@Table(name = "produtos")
@Entity(name = "produtos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idProduto")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduto;
    private String nomeProduto;
    
    private String descricao;
    private Boolean ativo;
    private Double precoProduto;
    private int qtdProduto;
    private String categoria;

    public Produto(CadastrarProduto dados) {
        this.nomeProduto = dados.nomeProduto();
        this.descricao = dados.descricao();
        this.ativo = (dados.ativo()) ? dados.ativo() : false;
        this.precoProduto = dados.precoProduto();
        this.qtdProduto = dados.qtdProduto();
        this.categoria = dados.categoria();
    }

    public void alterarProduto(AlterarProduto dados) {
        this.nomeProduto = dados.nomeProduto();
        this.descricao = (!dados.descricao().isBlank()) ? dados.descricao() : "";
        this.ativo = (dados.ativo()) ? dados.ativo() : false;
        this.precoProduto = dados.precoProduto();
        this.qtdProduto = dados.qtdProduto();
        this.categoria = dados.categoria();
    }
}
