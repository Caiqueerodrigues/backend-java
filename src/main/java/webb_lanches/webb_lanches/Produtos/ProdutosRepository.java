package webb_lanches.webb_lanches.Produtos;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ProdutosRepository extends JpaRepository<Produto, Long> {

    Produto findByNomeProduto(String name);
    Produto findByIdProduto(Long id);

    List<Produto> findByAtivo(Boolean ativo);
}
