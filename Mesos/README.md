## Componenti del gruppo

- Riccardo Libera
- Francesco Leucci
- Matteo Mancin
- Cesare Manzini

## Funzionalità implementate

- Regole complete
- TUI + GUI
- RMI + Socket
- Funzionalità avanzate
  - Classifica partite su DB
  - Partite multiple

## Compilazione (build)

Per generare i file eseguibili del gioco, aprire il terminale nella cartella principale del progetto ed eseguire il comando: 
```bash
mvn clean package
```
Maven scaricherà le dipendenze in automatico e creerà i file server.jar e client.jar all'interno della cartella target/

## Esecuzione del jar

Posto che il percorso per la cartella \bin del JDK sia già incluso nella variabile d'ambiente PATH, per l'esecuzione del progetto è sufficiente usare i seguenti comandi nel terminale, spostandosi preventivamente nella cartella target/ del progetto:

Esecuzione server:
```bash
java -jar server.jar
```
Esecuzione client:
```bash
java -jar client.jar
```
