# CATALOG ONLINE - API REST com Spring  
## Sobre o Projeto
O Catalog Online é uma aplicação web desenvolvida com o propósito de oferecer uma listagem paginada de produtos acessível aos usuários públicos,
além de proporcionar uma área administrativa privada para o gerenciamento CRUD (Create, Read, Update, Delete) por perfis autenticados.  
O principal objetivo deste projeto é servir como um ambiente de estudo para testes automatizados e a prática de TDD (Test Drive Development). 
## Modelo Conceitual
![model](https://github.com/MatheJuan/assets/blob/main/Model_CatalogProject.png)

## Funcionalidades
* Listagem paginada de produtos para usuários públicos.
* Área administrativa com operações CRUD para gerenciamento de produtos.
 
## Tecnologias, Boas práticas e Padrões
Durante o desenvolvimento, foram utilizadas as seguintes tecnologias e práticas:

* Banco de dados H2
* Implementação de TDD com Mockito
* Spring Security
* Exception Handling 
* OAuth2 e JWT  
* Testes automatizados 
* Exception Handling
* Data transfer Object(DTO)
* 
# Como Executar  
Certifique-se de ter o Java e o Maven instalados em sua máquina.
Para executar o projeto localmente, siga as instruções abaixo:  

``` bash
1. **Clone o repositório: **
git clone https://github.com/MatheJuan/catalogproject

2. **Entrar na pasta do projeto back end**
cd catalogproject

3. **Compile o projeto**
 mvn clean install

4. **Execute o JAR Gerado**
java -jar target/catalogproject.jar

4.1 ** Ou execute por meio do spring **
./mvnw spring-boot:run

```
