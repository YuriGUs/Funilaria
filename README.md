# Sistema de Gestão para Funilaria e Pintura

Este projeto é um **sistema desktop** desenvolvido em **Java com JavaFX** e banco de dados **SQLite**, projetado para atender as necessidades de oficinas de funilaria e pintura. A aplicação permite o gerenciamento eficiente de clientes, veículos, orçamentos de serviços e impressão de relatórios em PDF.

## 🛠️ Funcionalidades

- **Login com autenticação**
- **Cadastro de Clientes** com validações e máscaras para CPF/CNPJ, telefone e campos limitados por tamanho.
- **Cadastro de Veículos** associados a clientes, com validação de placa conforme o padrão nacional.
- **Listagem dinâmica** de clientes e seus respectivos veículos.
- **Criação de Orçamentos**, com adição de múltiplos serviços, cálculo automático de totais e geração de arquivos PDF.
- **Impressão de orçamento em PDF** personalizado com data atual, valor total e campo para assinatura.
- **Persistência de dados** com SQLite, acesso via JDBC.
- **Interface amigável e responsiva**, construída com JavaFX.

## 📦 Tecnologias Utilizadas

- Java 21
- JavaFX
- SQLite (persistência local)
- JDBC
- OpenPDF (geração de PDF)
- FXML (organização das telas)

## 🎨 Estrutura de Telas

- **Tela de Login**
- **Tela de Cadastro de Cliente e Veículo**
- **Tela de Listagem de Clientes**
- **Tela de Orçamentos**

## 📁 Organização de Pacotes

- `controller` – Lógicas e eventos das interfaces (FXML).
- `model` – Entidades da aplicação (Cliente, Veículo, ItemOrcamento etc.).
- `DAO` – Camada de acesso ao banco de dados.
- `service` – Camada intermediária entre controller e DAO.
- `util` – Utilitários diversos (conexão com o banco, formatação, etc).

## ✅ Requisitos

- JDK 17 ou superior (recomendado Java 21)
- JavaFX configurado no classpath
- IDE como IntelliJ IDEA ou Eclipse

## ▶️ Como Executar


# 1. Clone o repositório
```bash 
  git clone https://github.com/seuusuario/funilaria-pintura.git
```
## 2. Importe o projeto na sua IDE (IntelliJ, Eclipse...)

## 3. Compile e execute a classe principal:
###    src/main/java/dev/yuri/App.java

## 📄 Exemplo de Uso

1. Acesse o sistema com seu login (username: admin, password: admin123).
2. Cadastre um cliente e seu respectivo veículo.
3. Crie um orçamento e adicione os serviços desejados.
4. Gere e imprima o orçamento em formato PDF com layout profissional.
5. O orçamento pode ser arquivado, impresso, assinado e entregue ao cliente.

## 📷 Capturas de Tela

> *Em breve serão adicionadas imagens demonstrando o funcionamento do sistema, incluindo as telas de cadastro, orçamento e geração de PDF.*

## ✅ Validações Aplicadas

- **CPF/CNPJ**: inserção com máscara automática no padrão nacional, limitando o número de dígitos e impedindo caracteres inválidos.
- **Telefone**: máscara automática no formato (99) 99999-9999 com validação de tamanho e caracteres.
- **Placa**: limitado a 7 caracteres, respeitando o formato padrão brasileiro.
- **Ano**: apenas 4 dígitos numéricos são aceitos.
- **Nome, Endereço, Cor e Modelo**: limite de caracteres para garantir padronização e evitar sobrecarga de armazenamento.
- **Campos obrigatórios**: verificação de preenchimento completo com alertas visuais e mensagens de erro.

## 🔮 Futuras Melhorias

- Controle de usuários com diferentes níveis de permissão (admin, atendente, gerente, funcionário).
- Geração de relatórios financeiros e estatísticos.
- Histórico detalhado de orçamentos por cliente.

## 🛡️ Licença

Este projeto está licenciado sob a **Licença Pública Geral GNU (GPL)**.  
Para mais informações, consulte o arquivo [LICENSE](LICENSE).
