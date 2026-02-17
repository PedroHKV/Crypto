uma aplicação para criptografar e descriptografar arquivos, com a segurança do algoritmo AES e a simplicidade de uma interface gráfica

1- é usado o algoritmo AES com saltos e um vetor de inicialização aleatórios
2- a interface é intuitiva o bastante para que sejam despençadas explicações sobre uso
3- para usar basta ter uma ferramenta para executar o codigo (JRE) e uma para compilar ou empacotar (MAVEN)
   tendo isso basta abrir um terminal na pasta que contem o pom.xml e rodar o comando: mvn package, que irá gerar
   o Crypto-1.0.jar dentro da pasta target, assim, basta abrir a pasta que contenha o jar e digitar java -jar Crypto-1.0.jar
   ou dar duplo click sobre o jar (caso o JRE esteja configurado corretamente)

4- pretendo adicionar opções de algoritmos diferentes posteriormente
