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

@Table(name = "adicionais")
@Entity(name = "adicionais")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idAdicional")
public class Adicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAdicional;

    private String nome;
    private double preco;
    private Boolean status;

    public Adicional(CadastrarProduto dados) {
        this.nome = dados.nomeProduto();
        this.preco = dados.precoProduto();
        this.status = (dados.ativo()) ? dados.ativo() : false;
    }

    public void alterarAdicional(AlterarProduto dados) {
        this.nome = dados.nomeProduto();
        this.preco = dados.precoProduto();
        this.status = (dados.ativo()) ? dados.ativo() : false;
    }
}
