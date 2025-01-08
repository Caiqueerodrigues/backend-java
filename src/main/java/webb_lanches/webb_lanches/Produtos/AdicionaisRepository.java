package webb_lanches.webb_lanches.Produtos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdicionaisRepository extends JpaRepository<Adicional, Long> {
    Adicional findByNome(String name);
    Adicional findByIdAdicional(Long id);

    List<Adicional> findAll(); 
}
