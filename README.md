# API de Customer Wishlist

Uma API para gerenciamento de customer-wishlist, construída com Java 17 e Spring Boot 3.1.3, utilizando MongoDB como banco de dados.

## Endpoints

### Adição de Produto à uma customer Wishlist

Adiciona um produto à wishlist do cliente.
- **Método:** POST
- **Rota:** `/wishlist/{customerId}/produtos`

### Remoção de Produto da Wishlist

Remove um produto da wishlist do cliente.
- **Método:** DELETE
- **Rota:** `/wishlist/{customerId}/products`


### Consulta da Lista de Produtos na Wishlist

Obtém a lista de produtos na wishlist do cliente.
- **Método:** GET
- **Rota:** `/wishlist/{customerId}/products`

### Verificação de Existência de Produto na Wishlist
Verifica se um produto existe na wishlist do cliente.

- **Método:** GET
- **Rota:** `/api/wishlist/{customerId}/products/{productId}`

## Documentação da API

A documentação da API está disponível no formato Swagger OpenAPI JSON na raiz do projeto, no arquivo `api-docs.json`.

### swagger-editor
Você pode visualizar e interagir com a documentação usando o [Swagger Editor](https://editor.swagger.io/).
1. Acesse o [Swagger Editor](https://editor.swagger.io/).
2. No Swagger Editor, clique em "File" e selecione "Import File".
3. Carregue o arquivo `api-docs.json` do diretório raiz do projeto.
4. Agora você pode explorar a documentação da API, testar os endpoints e entender como a API funciona.

### swagger-ui
Além disso, com a aplicação rodando em ambiente local é possível visualizar o doc através do http://localhost:8080/swagger-ui/index.html

### postman-collection
Também disponibilizado na pasta raiz da aplicação um arquivo `wishlist.postman_collection.json` contendo a collection da api, podendo ser importada no postman.


## Configuração do Ambiente

- Java 17
- Spring Boot 3.1.3
- Maven
- MongoDB

## Como Executar

1. Clone o repositório.
2. Configure as propriedades do banco de dados no arquivo `application.yml`.
3. O banco de dados, pode ser provisionado a partir do arquivo `docker-compose.yml`
```
docker-compose up -d
```
. *Certifique-se de ter o Docker e o Docker Compose instalados em sua máquina.
4. Execute o projeto usando sua IDE ou através do comando `./mvnw spring-boot:run`.
```
./mvnw spring-boot:run
```
