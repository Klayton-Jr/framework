# Teste Framework

## Introdução

> Foi criado uma api cumprindo as seguintes definições:
* Segurança
    * Permitir o cadastro de usuários e login com autenticação via token JWT.
* Post
    * Permitir o cadastro e consulta de posts com texto, imagens e links.
    * Apenas o criador do post poderá ter permissão para excluí-lo.
* Comentários
    * Suportar a adição e exclusão de comentários em posts. Os posts poderão ser visíveis a todos os usuários. Apenas o criador do comentário poderá ter permissão para excluí-lo.
* Fotos
    * Permitir a criação de álbuns de fotos. As fotos dos álbuns poderão ser visíveis a todos os usuários. Apenas o dono de um álbum poderá excluí-lo.

## Detalhes dos endpoints

> Foi criado 4 Controladores onde os endpoints são divididos em:
* Album
    * Consulta todos, cria, adiciona foto, deleta
* Authentication
    * Consulta todos, cria, gera token(loga na aplicação)
* Comment
    * Cria um novo, deleta
* Post
    * Consulta todos, consulta pelo id, cadastra um novo, remove um post

## Instalação

> A parte do banco de dados usei uma imagem do postgres pelo docker. O nome do datasource deve ser "framework", o usuário deve ser "postgres" e a senha é "senha".
Já para a aplicação, basta rodar a classe main: FrameworkApplication
O token JWT de autenticação deve ser passado no Header em todas as requisições, com exceção das de autenticação
Exemplo: Authorization: Bearer {token}