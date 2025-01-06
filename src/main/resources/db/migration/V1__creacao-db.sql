CREATE TABLE IF NOT EXISTS `usuarios` (
    `id_User` INT NOT NULL AUTO_INCREMENT,
    `user` VARCHAR(50) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `ativo` TINYINT(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`id_User`)
);

CREATE TABLE IF NOT EXISTS `abertura` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `date` VARCHAR(10) NOT NULL,
    `usuario` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `adicionais` (
    `id_Adicional` INT NOT NULL AUTO_INCREMENT,
    `nome` VARCHAR(100) NOT NULL,
    `preco` DOUBLE NOT NULL,
    `status` TINYINT NULL DEFAULT '1',
    PRIMARY KEY (`id_Adicional`)
);

CREATE TABLE IF NOT EXISTS `produtos` (
    `id_Produto` INT NOT NULL AUTO_INCREMENT,
    `nome_Produto` VARCHAR(100) NOT NULL,
    `descricao` VARCHAR(500) NULL DEFAULT NULL,
    `qtd_Produto` INT NOT NULL,
    `ativo` TINYINT(1) NULL DEFAULT '1',
    `preco_Produto` DOUBLE NOT NULL,
    `categoria` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id_Produto`)
);

CREATE TABLE IF NOT EXISTS `caixa` (
    `id_Pagamento` INT NOT NULL AUTO_INCREMENT,
    `tipo_Pagamento` VARCHAR(100) NOT NULL,
    `valor_Pagamento` DOUBLE NOT NULL,
    `data_Pagamento` DATETIME NULL DEFAULT NULL,
    PRIMARY KEY (`id_Pagamento`)
);

CREATE TABLE IF NOT EXISTS `pedidos` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `id_Pedido` VARCHAR(255) NOT NULL,
    `obs` VARCHAR(255) NULL DEFAULT NULL,
    `preco` DOUBLE NOT NULL,
    `quantidade` INT NOT NULL,
    `status` VARCHAR(20) NULL DEFAULT NULL,
    `total` DOUBLE NOT NULL,
    `pago` VARCHAR(10) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NOT NULL DEFAULT '0',
    `id_Produto` INT NULL DEFAULT NULL,
    `id_Adicional` VARCHAR(110) NULL DEFAULT NULL,
    `retirada` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_pedido_produto_idx` (`id_Produto` ASC) VISIBLE,
    CONSTRAINT `fk_pedido_produto`
    FOREIGN KEY (`id_Produto`)
    REFERENCES `webb-lanches`.`produtos` (`id_Produto`)
);