# Social App per Developers 

Social network realizzato con Angular 14, Bootstrap 5.3, Spring Boot 3 (Java 17), PostgreSQL come Capstone Project per Epicode School.
## Funzionalità
:ticket: -> Login e Signup dell'utente <br /> 
<br /> 
:page_facing_up: -> Pubblicazione di post <br /> 
<br /> 
:bust_in_silhouette: -> Completamento del profilo <br /> 
<br /> 
:gift_heart: -> Possibilità di mettere like e commentare i post <br /> 
<br /> 
:pencil: -> Possibilità di modifica di informazioni e commenti <br /> 
<br /> 
:eye: -> Visione dei post a cui si è messo like nel proprio profilo <br /> 
<br /> 
:mag: -> Ricerca di altri utenti 


## Installazione

Aprire la cartella BE con il proprio IDE e inserire le proprietà corrispondenti al database utilizzato:

```bash
# Impostazione DB PostgreSQL
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:port/nome_database
spring.datasource.username=tuo_username
spring.datasource.password=tua_password
```

Aprire la cartella FE con il proprio IDE ed eseguire in terminale:

```bash
npm install
```
```bash
ng serve --o
```
Aprire il sito all'indirizzo indicato in terminale.
