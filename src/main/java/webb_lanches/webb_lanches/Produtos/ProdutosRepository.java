package webb_lanches.webb_lanches.Produtos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import webb_lanches.webb_lanches.Produtos.DTO.ListagemProdutosMenu;

public interface ProdutosRepository extends JpaRepository<Produto, Long> {

    Produto findByNomeProduto(String name);
    Produto findByidProduto(Long id);
}
