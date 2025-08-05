# 🛒 Products Manager

Aplicação Java Desktop para gerenciamento de produtos, com autenticação de usuários, construída com JavaFX, JPA (Hibernate), MySQL, Maven e seguindo os padrões **MVC + DAO**.

---

## 💡 Sobre o projeto

O **Products Manager** é um projeto CRUD simples, com interface gráfica, criado para colocar em prática os conhecimentos adquiridos durante a faculdade e estudos complementares em Java. Seu principal objetivo é demonstrar domínio de boas práticas, arquitetura de software e integração com banco de dados em uma aplicação real.

O projeto inclui autenticação de usuários com senha criptografada (BCrypt), persistência de dados com JPA/Hibernate e uma interface em JavaFX para realizar operações com produtos.

---

## 🖼️ Screenshots

### 🔐 LoginView
![LoginView](assets/LoginView.png)

### 🧾 RegisterView
![RegisterView](assets/RegisterView.png)

### 📋 MainView
![MainView](assets/MainView.png)

---

## 🧪 Como testar o projeto

1. **Configure o banco de dados:**
   - Abra o arquivo `script.sql`, localizado na pasta `/sql`.
   - Execute esse script no seu MySQL Workbench para criar o banco `pm_appdb`.

2. **Configure suas credenciais:**
   - No arquivo `database.properties`, localizado na pasta `src/main/resources`, preencha os seguintes campos com suas informações:
     ```properties
     db.url=jdbc:mysql://localhost:3306/pm_appdb
     db.user= (Insira seu usuário aqui)
     db.password= (Insira sua senha aqui)
     ```

3. **Execute o projeto:**
   - Rode a classe `Main.java`, que está em:
     ```
     src/main/java/br/com/michael/productsmanager/view/Main.java
     ```

---

## 🔐 Como usar

1. **Login:**
   - Insira seu nome de usuário e senha, ou clique em "Registrar" se ainda não tem uma conta.
2. **Cadastro:**
   - Informe um nome único e uma senha (sem restrições específicas).
   - Confirme a senha e clique em "Registrar".
3. **Tela principal:**
   - Realize operações CRUD (Criar, Ler, Atualizar, Excluir) sobre os produtos.
   - Cada produto possui: **nome**, **preço (em R$)** e **descrição**.
   - Use o botão "Limpar" para resetar os campos de texto.

---

## 🧱 Estrutura do projeto

- `model` → Entidades JPA.
- `dao` → Camada de persistência (DAO).
- `controller` → Lógica das telas e ações.
- `view` → Telas FXML e classe `Main`.
- `util` → Classe de configuração com `database.properties`.
- `session` → Classe para manter o estado do usuário logado.

---

## 📄 Documentação

- A documentação gerada com **JavaDoc** está disponível na pasta `/docs`.

---

## 🚧 Próximas melhorias (roadmap)

- Adicionar suporte a banco de dados em **Docker**.
- Gerar um instalador da aplicação com **JPackage**.
- Criar modo "demo" com banco de dados embutido para facilitar testes.
- Melhorar o layout e a experiência visual da interface.

---

## 👨‍💻 Autor

Desenvolvido por **Michael Nascimento** — estudante de Análise e Desenvolvimento de Sistemas e aspirante a desenvolvedor backend Java.

📧 Email: [michael.nascimentodev@gmail.com](mailto:michael.nascimentodev@gmail.com)  
🔗 LinkedIn: [linkedin.com/in/michael-nascimento-675847277](https://www.linkedin.com/in/michael-nascimento-675847277/)

---

## ⚠️ Aviso

Este projeto foi criado com fins educacionais e para compor meu portfólio. Uso de **GitHub Copilot** e **ChatGPT** auxiliou na refatoração, otimização e documentação do código.

---

⭐ Se você gostou do projeto, considere deixar uma estrela!
