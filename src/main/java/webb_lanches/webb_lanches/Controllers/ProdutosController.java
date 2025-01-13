package webb_lanches.webb_lanches.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import webb_lanches.webb_lanches.Commons.DTO.ResponseDTO;
import webb_lanches.webb_lanches.Produtos.AdicionaisRepository;
import webb_lanches.webb_lanches.Produtos.Adicional;
import webb_lanches.webb_lanches.Produtos.Produto;
import webb_lanches.webb_lanches.Produtos.ProdutosRepository;
import webb_lanches.webb_lanches.Produtos.DTO.AlterarProduto;
import webb_lanches.webb_lanches.Produtos.DTO.CadastrarProduto;
import webb_lanches.webb_lanches.Produtos.DTO.EditarAdiconalDTO;
import webb_lanches.webb_lanches.Produtos.DTO.ListagemAdicionaisMenu;
import webb_lanches.webb_lanches.Produtos.DTO.ListagemCompletaMenu;
import webb_lanches.webb_lanches.Produtos.DTO.ListagemGeralEstoque;
import webb_lanches.webb_lanches.Produtos.DTO.ListagemProdutosMenu;
import webb_lanches.webb_lanches.Produtos.DTO.ProdutoDTO;
import webb_lanches.webb_lanches.Produtos.ENUM.TipoProduto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/produtos")
public class ProdutosController {

    @Autowired
    private ProdutosRepository produtoRepository;

    @Autowired
    private AdicionaisRepository adicionalRepository;

    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<ResponseDTO> criarProduto(@RequestBody @Valid CadastrarProduto dados) {

        try {
            if (dados.categoria().equalsIgnoreCase("Adicionais") || dados.categoria().equalsIgnoreCase("Adicional")) {
                var exists = adicionalRepository.findByNome(dados.nomeProduto());
            
                if (exists == null) {
                    var adicional = new Adicional(dados);
                    adicionalRepository.save(adicional);
            
                    var response = new ResponseDTO("", "", "Adicional cadastrado com sucesso!", "");
                    return ResponseEntity.status(200).body(response);
                } else {
                    var response = new ResponseDTO("", "Adicional já cadastrado anteriormente!", "", "");
                    return ResponseEntity.status(400).body(response);
                }
            } else {
                var exists = produtoRepository.findByNomeProduto(dados.nomeProduto()); 
    
                if(exists == null) {
                    var produto = new Produto(dados);
                    produtoRepository.save(produto);
    
                    var response = new ResponseDTO("", "", "Produto cadastrado com sucesso!", "");
                    return ResponseEntity.status(200).body(response);
                } else {
                    var response = new ResponseDTO("", "Produto já cadastrado anteriormente!", "", "");
                    return ResponseEntity.status(400).body(response);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "",""));
        }
        
    }
    
    @GetMapping("/{tipo}/{id}")
    public ResponseEntity<ResponseDTO> getProdutoAdicional(@PathVariable TipoProduto tipo, @PathVariable Long id) {
        try {
            if(tipo == TipoProduto.adicional) {
                var item = adicionalRepository.findByIdAdicional(id);

                EditarAdiconalDTO itemFormatado = new EditarAdiconalDTO(item.getNome(), item.getPreco(), "Adicional", item.getStatus());
    
                if(item != null) {
                    var response = new ResponseDTO(itemFormatado, "", "", "");
                    return ResponseEntity.status(200).body(response);
                }
            } else {
                var item = produtoRepository.findByIdProduto(id);
    
                if(item != null) {
                    var response = new ResponseDTO(item, "", "", "");
                    return ResponseEntity.status(200).body(response);
                }
            }
            return ResponseEntity.status(400).body(new ResponseDTO("", "Dados informados inválidos!", "", ""));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }
    
    @PutMapping
    @Transactional
    public ResponseEntity<ResponseDTO> alterarProduto(@RequestBody @Valid AlterarProduto dados) {
        try {
            
            if (dados.categoria().equalsIgnoreCase("Adcionais") || dados.categoria().equalsIgnoreCase("Adicional")) {
                var item = adicionalRepository.getReferenceById(dados.id());
    
                if(item != null) {
                    item.alterarAdicional(dados);

                    return ResponseEntity.status(200).body(new ResponseDTO("", "", "Adicional alterado com sucesso!", ""));
                }
            } else {
                var item = produtoRepository.getReferenceById(dados.id());
    
                if(item != null) {
                    item.alterarProduto(dados);
                    return ResponseEntity.status(200).body(new ResponseDTO("", "", "Produto alterado com sucesso!", ""));
                }
            }
            return ResponseEntity.status(400).body(new ResponseDTO("", "Dados informados inválidos!", "", ""));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }

    @GetMapping()
    public ResponseEntity<ResponseDTO> getAllProdutos() {
        try {
            List<ListagemGeralEstoque> response = new ArrayList<>();
            
            var items = produtoRepository.findAll(Sort.by(Sort.Order.asc("nomeProduto")));
            var adicionais = adicionalRepository.findAll(Sort.by(Sort.Order.asc("nome")));
            
            items.forEach((item) -> response.add(new ListagemGeralEstoque(
                item.getIdProduto(), 
                item.getNomeProduto(), 
                item.getDescricao(), 
                item.getQtdProduto(), 
                (item.getAtivo()) ? 1 : 0, 
                item.getPrecoProduto(), 
                item.getCategoria(), 
                item.getAdicional()
                )));
                
                adicionais.forEach((item) -> response.add(new ListagemGeralEstoque(
                    item.getIdAdicional(), 
                    item.getNome(), 
                    "", 
                    0, 
                    (item.getStatus()) ? 1 : 0, 
                    item.getPreco(), 
                    "Adicionais", 
                    ""
                )));

                return ResponseEntity.status(200).body(new ResponseDTO(response, "", "", ""));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
        
    }
    
    @GetMapping("/produtos-menu")
    public ResponseEntity<ResponseDTO> getProdutosMenu() {
        try {
            List<Adicional> adicionais = adicionalRepository.findByStatus(true);
            List<Produto> produtos = produtoRepository.findByAtivo(true);

            List<ListagemProdutosMenu> listaProdutos = new ArrayList<>();
            if(produtos != null && produtos.size() > 0) {
                produtos.forEach(prod -> {
                    ProdutoDTO produto = new ProdutoDTO(
                        prod.getIdProduto(), 
                        prod.getNomeProduto(), 
                        prod.getPrecoProduto(), 
                        prod.getDescricao(), 
                        prod.getAdicional()
                    );

                    listaProdutos.stream()
                        .filter(categoria -> categoria.categoria().equals(prod.getCategoria()))
                        .findFirst()
                        .ifPresentOrElse(
                            categoria -> categoria.items().add(produto),  // Adiciona o produto à lista da categoria existente
                            () -> { // Cria uma nova categoria caso não exista
                                List<ProdutoDTO> listaTemp = new ArrayList<>();
                                listaTemp.add(produto);
                                listaProdutos.add(new ListagemProdutosMenu(prod.getCategoria(), listaTemp));
                            }
                        );
                });

            } 

            List<ListagemAdicionaisMenu> listaAdicionais = new ArrayList<>();
            if(adicionais != null && adicionais.size() > 0) {

                adicionais.forEach(add -> {
                    ListagemAdicionaisMenu adicional = new ListagemAdicionaisMenu(
                        add.getIdAdicional(), 
                        add.getNome(), 
                        add.getPreco(), 
                        add.getStatus(), 
                        String.format("%.2f", add.getPreco())
                    );

                    listaAdicionais.add(adicional);
                });
            }

            ListagemCompletaMenu listaFinal = new ListagemCompletaMenu(listaProdutos, listaAdicionais);


            return ResponseEntity.status(200).body(new ResponseDTO(listaFinal, "", "", ""));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }
}
