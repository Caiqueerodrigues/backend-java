DELIMITER $$

CREATE PROCEDURE `getPedidoId`(IN id VARCHAR(100))
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
        pedidos.id_Produto,
        pedidos.pago, 
        pedidos.retirada,
        produtos.nome_Produto AS nomeProduto 
    FROM 
        pedidos
    JOIN 
        produtos ON pedidos.id_Produto = produtos.id_Produto
    WHERE
        pedidos.id_Pedido = id;
END$$

DELIMITER ;
