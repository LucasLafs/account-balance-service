# Account Balance Service

Serviço responsável por consultar saldos de contas e processar transações financeiras, implementado com arquitetura hexagonal.

## Descrição

O serviço disponibiliza um endpoint REST para consulta de saldo de contas e processa automaticamente transações financeiras enviadas para uma fila SQS simulada pelo LocalStack. Mensagens geradas pelo message-generator são consumidas e processadas em tempo real.

## Funcionalidade principal

- **Consultar saldo da conta**: Endpoint REST para obter o saldo atual de uma conta específica.
- **Processamento de transações financeiras**: Mensagens enviadas para a fila SQS do LocalStack são consumidas automaticamente e atualizam o saldo da conta.

## Tecnologias utilizadas

- **Kotlin / Spring Boot**: Linguagem e framework principal da aplicação.
- **LocalStack**: Simulação local de serviços AWS, incluindo SQS.
- **PostgreSQL**: Banco de dados relacional para persistência de dados.
- **Grafana**: Visualização de métricas e dashboards.
- **Prometheus**: Monitoramento e coleta de métricas.
- **Docker Compose**: Orquestração de containers para facilitar o ambiente de desenvolvimento.

## Pré-requisitos

- Docker e Docker Compose instalados.
- Java 11 ou superior.
- Gradle instalado.

## Como rodar a aplicação

### 1. Build da aplicação

Compile a aplicação com Gradle:

```bash
./gradlew clean build
```

### 2. Subir containers de suporte

Serviços externos (Grafana, Prometheus, LocalStack e Postgresql) via Docker Compose:

```bash
docker-compose up
```
Aguarde o message-generator finalizar a geração de mensagens antes de subir a aplicação principal.


### 3. Start da aplicação

```bash
./gradlew bootRun
```

Após iniciar, a aplicação irá consumir automaticamente as mensagens geradas pelo message-generator na fila do LocalStack e processá-las.

O endpoint REST para consulta de saldo estará disponível em http://localhost:7050. Parametro para consulta é o id da conta

```bash
curl --location 'http://localhost:7050/balances/97a331dc-16f4-4fd7-88e1-923d1d216eb4'
```

### Observabilidade

Métricas habilitadas via Spring Boot Actuator.
Métricas Prometheus disponíveis em /actuator/prometheus.
Dashboards podem ser criados no Grafana acessando http://localhost:3000.

### Credenciais
Grafana: admin / admin
Postgres: configuração padrão local (ajustável no docker-compose)
LocalStack: sem autenticação, endpoints simulados via localhost:4566

## TRADEOFF

### Arquitetura Hexagonal

Facilita a separação entre regras de negócio e infraestrutura.
Aumenta a testabilidade do código e desacopla integrações externas 
facilitando alterações sem alterar o dominio e as regras de negócio da aplicação.

### PostgreSQL

Postgres garante a atomicidade consistencia isolamento e durabilidade o que combina muito bem
com o contexto da aplicação pensando no teorema de CAP onde a consistencia prevalece sobre a disponibilidade.

### Prometheus + Grafana

Por serem ferramentas open source, fáceis de integrar com o Spring Boot Actuator e já consolidadas no mercado. 
A stack também permite a criação de dashboards e alertas personalizados, oferecendo visibilidade completa do sistema principalmente para um serviço simples local.

### Processamento de mensagens

O método escolhido foi um processamento de lotes de mensagens também adicionando multiplos consumidores através do SQSListener e também de inserção em lotes na base de dados para melhor perfomance.
Com mais tempo, poderia adicionar as corrotinas do Kotlin, que permitem paralelismo leve sobre a jvm que podem ser muito mais escaladas do que threads convencionais.
Também gostaria de ter adicionado meu próprio fluxo de fila para controle maior de cada transação e reprocessamento se necessario.

### Implantação em cloud

Utilizaria de um CI/CD como esteira com alguns steps principais de validação para mitigação de riscos.

Lint → verificação automática de estilo e padrões de código.
Analisador de qualidade (SonarQube) → detecta code smells, vulnerabilidades e pontos de melhoria.
Build → compila a aplicação e gera os artefatos executáveis.
Testes regressivos → executa a suíte de testes automatizados de componentes para garantir que novas mudanças não quebrem funcionalidades já existentes.
Delivery -> Entrega do artefato para ser implantado
Deploy → versão validada é implantada no ambiente (local, homologação ou produção).

### Deploy

Usuário → API Gateway → Load Balancer → Orquestrador de Containers (Kubernetes/ECS) C/ auto escalonamento horizontal e verital→ Pods da aplicação
Banco de dados gerenciado (Postgres DBaaS) para persistência.
Fila de Mensagens (SQS) para processamento assíncrono.
Prometheus + Grafana para observabilidade.
Cache para resiliencia de futuras features