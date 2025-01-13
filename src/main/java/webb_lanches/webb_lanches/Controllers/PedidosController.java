package webb_lanches.webb_lanches.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import webb_lanches.webb_lanches.Caixa.DTO.ListagemPagamentos;
import webb_lanches.webb_lanches.Commons.DTO.DateDTO;
import webb_lanches.webb_lanches.Commons.DTO.ResponseDTO;
import webb_lanches.webb_lanches.Commons.Services.ObterDataHoraBrasilia;
import webb_lanches.webb_lanches.Exceptions.InsufficientQuantityException;
import webb_lanches.webb_lanches.Pedidos.Pedido;
import webb_lanches.webb_lanches.Pedidos.PedidosRepository;
import webb_lanches.webb_lanches.Pedidos.DTO.PedidoDTO;
import webb_lanches.webb_lanches.Produtos.Adicional;
import webb_lanches.webb_lanches.Produtos.Produto;
import webb_lanches.webb_lanches.Produtos.ProdutosRepository;
import webb_lanches.webb_lanches.Pedidos.DTO.CriarPedidoDTO;
import webb_lanches.webb_lanches.Pedidos.DTO.DadosEdicaoPedidoDTO;
import webb_lanches.webb_lanches.Pedidos.DTO.DadosListagemPedidoId;
import webb_lanches.webb_lanches.Pedidos.DTO.DeletarPedidotDTO;
import webb_lanches.webb_lanches.Pedidos.DTO.ItemsEdicaoPedidoId;
import webb_lanches.webb_lanches.Pedidos.DTO.ItemsListagemPedidoId;
import webb_lanches.webb_lanches.Pedidos.DTO.ItemsPedidoDTO;
import webb_lanches.webb_lanches.Pedidos.DTO.ItemsPedidoNovoDTO;
import webb_lanches.webb_lanches.Pedidos.DTO.ListagemPedidosCaixa;
import webb_lanches.webb_lanches.Produtos.AdicionaisRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/pedidos")
public class PedidosController {

    @Autowired
    private PedidosRepository repository;

    @Autowired
    private ProdutosRepository produtosRepository;

    @Autowired
    private AdicionaisRepository adicionaisRepository;

    @Autowired
    private ObterDataHoraBrasilia dataHoraBrasilia;

    @PostMapping("/caixa-listagem")
    public ResponseEntity<ResponseDTO> getPedidosMenu(@RequestBody DateDTO date) {
        try {
            var pedidosRespository = (date.data() != null) ?
                repository.findByTodosPedidos(date.data()) : 
                repository.findByPedidosFiltro();

            List<ListagemPedidosCaixa> pedidos = new ArrayList();

            if(!pedidosRespository.isEmpty()) {
                pedidosRespository.forEach((pedido) -> {
                    Optional<ListagemPedidosCaixa> existeOpt = pedidos
                    .stream()
                    .filter(item -> item.getIdPedido().equals(pedido.getIdPedido()))
                    .findFirst();

                    if (existeOpt.isEmpty()) {
                        String nome = (pedido.getIdPedido() != null) ? pedido.getIdPedido().split("\\.")[0] : "";
                        Produto produtoRepos = produtosRepository.findByIdProduto(pedido.getIdProduto());

                        ItemsPedidoDTO itemPedido = new ItemsPedidoDTO(
                            pedido.getIdProduto(), 
                            produtoRepos.getNomeProduto(), 
                            pedido.getPreco(), 
                            pedido.getQuantidade(), 
                            ""
                        );

                        List<ItemsPedidoDTO> items = new ArrayList();
                        items.add(itemPedido);

                        var pedidoNovo = new ListagemPedidosCaixa(
                            nome, 
                            pedido.getIdPedido(), 
                            pedido.getPago(), 
                            items, 
                            pedido.getRetirada(), 
                            (pedido.getIdAdicional() != null) ? true : false, 
                            (pedido.getObs() != null) ? true : false, 
                            pedido.getTotal()
                            );
                        
                        pedidos.add(pedidoNovo);
                    } else {
                        ItemsPedidoDTO itemPedido = new ItemsPedidoDTO(pedido.getIdProduto(), pedido.getNomeProduto(), pedido.getPreco(), pedido.getQuantidade(), "");

                        existeOpt.get().getItems().add(itemPedido);
                    }

                });
                return ResponseEntity.status(200).body(new ResponseDTO(pedidos, "", "",""));
            }

            return ResponseEntity.status(400).body(new ResponseDTO("", "", "","Não existem pedidos para esta data ainda!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(e.getMessage(), "Desculpe, tente novamente mais tarde!", "",""));
        }
    }

    @PostMapping("/novo-pedido")
    @Transactional
    public ResponseEntity<ResponseDTO> newPedido(@RequestBody @Valid CriarPedidoDTO dados) {
        try {
            String idPedido = (dados.nomeCliente() != null && !dados.nomeCliente().isBlank()) ? 
                dados.nomeCliente() + "." + dataHoraBrasilia.dataHoraBrasilia() : 
                dataHoraBrasilia.dataHoraBrasilia();
                
                List<Pedido> novosPedidos = new ArrayList<>();

                for (ItemsPedidoNovoDTO item : dados.items()) {
                    Produto consult = produtosRepository.findByIdProduto((Long) item.idProduto());

                
                    if (consult != null && consult.getQtdProduto() - 1 >= 0) {
                        Pedido novoPedido = new Pedido();
                            novoPedido.setIdPedido(idPedido);
                            novoPedido.setObs(item.obs());
                            novoPedido.setPreco(item.preco());
                            novoPedido.setQuantidade(1);
                            novoPedido.setStatus("");
                            novoPedido.setTotal(item.total());
                            novoPedido.setPago(item.pago());
                            novoPedido.setIdProduto(item.idProduto());
                            novoPedido.setIdAdicional(item.idAdicional());
                            novoPedido.setRetirada(item.retirada());
                
                            novosPedidos.add(novoPedido);
                    } else {
                        throw new InsufficientQuantityException("Quantidade de " + consult.getNomeProduto() + " insuficiente!");
                    }
                }

                novosPedidos.forEach(pedido -> repository.save(pedido));

            return ResponseEntity.status(200).body(new ResponseDTO("", "", "Pedido Criado com sucesso!",""));
        } catch (InsufficientQuantityException e) {
            return ResponseEntity.status(400).body(new ResponseDTO("", e.getMessage(), "", ""));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(e.getMessage(), "Desculpe, tente novamente mais tarde!", "",""));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getPedidoId(@PathVariable String id) {
        try {
            var pedido = repository.findByPedidoId(id);

            if(pedido != null) {
                var nomeCliente = (pedido.get(0).getIdPedido().contains(".")) ? 
                    pedido.get(0).getIdPedido().split("\\.")[0] : "";

                List<ItemsListagemPedidoId> items = new ArrayList<>();

                DadosListagemPedidoId response = new DadosListagemPedidoId(
                    id, 
                    pedido.get(0).getRetirada(),
                    pedido.get(0).getPago(), 
                    pedido.get(0).getTotal(),
                    nomeCliente, 
                    items
                );

                List<Adicional> adicionais = adicionaisRepository.findAll();
                List<Long> idsPedidosAgrupados = new ArrayList<>();

                pedido.forEach((item) -> {
                    List<String> adicionaisItem = new ArrayList<>();

                    if(item.getIdAdicional() != null && !item.getIdAdicional().isBlank()) {
                        List<String> ids = Arrays.asList(item.getIdAdicional().split(", "));

                        ids.forEach(idAdd -> {
                            Long idLong = Long.parseLong(idAdd);
                                adicionais.stream()
                                    .filter(adicional -> adicional.getIdAdicional() == idLong)  // Filtrar pelo id
                                    .findFirst()
                                    .ifPresent(adicional -> adicionaisItem.add(adicional.getNome()));  // Adicionar o nome à lista
                        });
                    }

                    idsPedidosAgrupados.add(item.getId());

                    Produto produtoRepos = produtosRepository.findByIdProduto(item.getIdProduto());

                    ItemsListagemPedidoId produto = new ItemsListagemPedidoId(
                        item.getObs(), 
                        item.getPreco(), 
                        item.getQuantidade(), 
                        item.getStatus(), 
                        item.getTotal(), 
                        item.getPago(), 
                        item.getIdProduto(), 
                        item.getIdAdicional(), 
                        produtoRepos.getNomeProduto(), 
                        item.getRetirada(), 
                        idsPedidosAgrupados,
                        adicionaisItem
                    );

                    items.add(produto);
                });

                return ResponseEntity.status(200).body(new ResponseDTO(response, "", "",""));
            } else {
                return ResponseEntity.status(400).body(new ResponseDTO("", "Desculpe, pedido inexistente!", "",""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(e.getMessage(), "Desculpe, tente novamente mais tarde!", "",""));
        }
    }

    @PutMapping("/alterar-pedido")
    @Transactional
    public ResponseEntity<ResponseDTO> putPedido( @RequestBody @Valid DadosEdicaoPedidoDTO dados) {
        try {

            List<Pedido> pedido = repository.findByIdPedido(dados.idPedido());
            
            if(pedido != null && pedido.size() > 0) {
                if(dados.deletados() != null && dados.deletados().size() > 0) {
                    dados.deletados().forEach(idDeletado -> {
                        long idDeletadoLong = (long) idDeletado;
    
                        repository.deleteById(idDeletadoLong);
                    });
                }
    
                final String novoIdPedido;
                if((dados.nomeCliente() == null || dados.nomeCliente().isBlank()) && dados.idPedido().contains(".")) {
                    novoIdPedido = (dados.idPedido().split("\\.")[0] != null) ? 
                    dados.idPedido().split("\\.")[1].toString() : dados.idPedido().split("\\.")[0];                
                } else if(dados.nomeCliente() != null && !dados.nomeCliente().isBlank() && !dados.idPedido().contains(dados.nomeCliente())) {
                    System.out.println(dados.idPedido().contains("."));
                    novoIdPedido = (dados.idPedido().contains(".")) ?
                        dados.nomeCliente().toLowerCase() + "." + dados.idPedido().split("\\.")[1] : 
                        dados.nomeCliente().toLowerCase() + "." + dados.idPedido();
                } else novoIdPedido = null;
    
                List<Pedido> novosPedidos = new ArrayList<>();
    
                for(ItemsEdicaoPedidoId item: dados.items()) {
                    if(item.id() == null) {
                        Produto produtoConsult = produtosRepository.findByIdProduto(item.idProduto());
    
                        if(produtoConsult.getQtdProduto() - item.quantidade() >= 0) {
                            Pedido novoItem = new Pedido(
                                (novoIdPedido != null) ? novoIdPedido : dados.idPedido(),
                                item.obs(),
                                item.preco(),
                                item.quantidade(),
                                item.status(),
                                item.total(),
                                item.pago(),
                                item.idProduto(),
                                item.idAdicional(),
                                item.retirada()
                            );
    
                            novosPedidos.add(novoItem);
                        } else {
                            throw new InsufficientQuantityException("Quantidade de " + produtoConsult.getNomeProduto() + " insuficiente!");
                        }
                    } else {
                        Optional<Pedido> pedidoExistente = repository.findById(item.id());
                        
                        pedidoExistente.ifPresent(p -> {
                            if(item.obs() != null && !item.obs().isBlank()) p.setObs(item.obs());
                            p.setPreco(item.preco());
                            p.setQuantidade(item.quantidade());
                            if(item.status() != null && !item.status().isBlank()) p.setStatus(item.status());
                            p.setTotal(item.total());
                            p.setPago(item.pago());
                            if(item.idAdicional() != null && !item.idAdicional().isBlank()) p.setIdAdicional(item.idAdicional());
                            p.setRetirada(item.retirada());
                        });
                    }
                }
    
                if (novoIdPedido != null) {
                    pedido.forEach(ped -> {
                        ped.setIdPedido(novoIdPedido);
                        repository.save(ped);
                    });
                }
    
                if(novosPedidos.size() > 0) {
                    novosPedidos.forEach(np -> repository.save(np));
                }
    
                return ResponseEntity.status(200).body(new ResponseDTO("", "", "Pedido alterado com sucesso!",""));
            } else return ResponseEntity.status(404).body(new ResponseDTO("", "Desculpe, pedido inexistente!", "",""));
        } catch (InsufficientQuantityException e) {
            return ResponseEntity.status(400).body(new ResponseDTO("", e.getMessage(), "", ""));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(e.getMessage(), "Desculpe, tente novamente mais tarde!", "",""));
        }
    }
    @DeleteMapping("/apagar-pedido")
    @Transactional
    public ResponseEntity<ResponseDTO> apagarPedido(@RequestBody @Valid DeletarPedidotDTO data) {
        try {
            repository.deleteByIdPedido(data.idPedido());
            
            return ResponseEntity.status(200).body(new ResponseDTO("", "", "Pedido apagado com sucesso!",""));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO(e.getMessage(), "Desculpe, tente novamente mais tarde!", "",""));
        }
    }
}
