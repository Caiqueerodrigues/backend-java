CREATE PROCEDURE `todosPedidos`(IN dataParam VARCHAR(30))
BEGIN
    SELECT 
		pedidos.id,
        pedidos.id_Pedido,
        pedidos.obs, 
        pedidos.total,
        pedidos.preco, 
        pedidos.quantidade, 
        pedidos.id_Adicional, 
        pedidos.status, 
        pedidos.id_Produto ,
        pedidos.pago, 
        pedidos.retirada,
        produtos.nome_Produto AS nomeProduto 
    FROM 
        pedidos
    JOIN 
        produtos ON pedidos.id_Produto = produtos.id_Produto
    WHERE
        pedidos.preco IS NOT NULL
    AND 
   	    pedidos.id_Pedido LIKE CONCAT('%', dataParam, '%');
END;

CREATE PROCEDURE `pedidosFiltro`(IN dataParam VARCHAR(30))
BEGIN
    SELECT 
		pedidos.id,
        pedidos.id_Pedido,
        pedidos.obs, 
        pedidos.total,
        pedidos.preco, 
        pedidos.quantidade, 
        pedidos.id_Adicional, 
        pedidos.status, 
        pedidos.retirada,
        pedidos.pago, 
        pedidos.id_Produto ,
        produtos.nome_Produto AS nomeProduto 
    FROM 
        pedidos
    JOIN 
        produtos ON pedidos.id_Produto = produtos.id_Produto
    WHERE
        pedidos.preco IS NOT NULL
    AND 
   		pedidos.pago LIKE CONCAT('%', dataParam, '%');
END;

CREATE TRIGGER `update_estoque_after_delete` AFTER DELETE ON `pedidos` FOR EACH ROW BEGIN
    -- Atualiza a quantidade no estoque
    UPDATE produtos 
    SET qtd_Produto = qtd_Produto + OLD.quantidade
    WHERE id_Produto = OLD.id_Produto;
    
    -- Verifica se o estoque estava em 0 e se a quantidade agora é maior que 0, então atualiza o status para 1
    UPDATE produtos
    SET ativo = 1
    WHERE id_Produto = OLD.id_Produto AND qtd_Produto > 0;
END;

CREATE TRIGGER `update_estoque_after_insert` AFTER INSERT ON `pedidos` FOR EACH ROW BEGIN
    -- Atualiza a quantidade no estoque
    UPDATE produtos 
    SET qtd_Produto = qtd_Produto - NEW.quantidade
    WHERE id_Produto = NEW.id_Produto;
    
    -- Verifica se a quantidade resultante é 0 e, se for, atualiza o status do produto
    UPDATE produtos
    SET ativo = 0
    WHERE id_Produto = NEW.id_Produto AND qtd_Produto = 0;
END;