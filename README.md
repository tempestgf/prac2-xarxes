# Server

Aquest és un servidor simple que gestiona una "base de dades" de llibres mitjançant una connexió de sockets.

## Funcionalitat

El servidor proporciona les següents funcionalitats:

1. **Enviar títols de llibres**: El servidor envia els títols de tots els llibres a un client.
2. **Enviar informació d'un llibre**: El servidor envia informació detallada d'un llibre específic al client.
3. **Rebre i afegir un llibre a la base de dades**: Permet al client afegir un nou llibre a la base de dades del servidor.
4. **Rebre i eliminar un llibre de la base de dades**: Permet al client eliminar un llibre existent de la base de dades del servidor.
5. **Sortir**: Tanca la connexió amb el client i finalitza el servidor.

## Comentaris del Codi

El codi està comentat per facilitar la comprensió i descriu els següents punts clau:

- Creació del servidor i inicialització de la "base de dades" de llibres.
- Acceptació de connexions entrants dels clients.
- Creació de fils per gestionar múltiples clients simultàniament.
- Gestió de les diferents opcions rebudes dels clients.
- Enviament de dades al client i recepció de dades del client.
- Maneig d'errors durant l'execució del servidor.

## Dependències

Aquest servidor necessita una classe `BooksDB` per gestionar la "base de dades" de llibres. La gestió de la connexió del client es realitza mitjançant classes proporcionades per Java, com `ServerSocket`, `Socket`, `ObjectInputStream` i `ObjectOutputStream`.

## Instruccions d'Ús

Per utilitzar el servidor, compileu el codi i executeu l'aplicació. El servidor esperarà connexions entrants dels clients al port 4444.

# Client

Aquest és un client simple que es connecta a un servidor per interactuar amb una "base de dades" de llibres mitjançant una connexió de sockets.

## Funcionalitat

El client ofereix les següents funcionalitats:

1. **Llistar tots els títols de llibres**: El client pot sol·licitar al servidor que enviï tots els títols de llibres disponibles i els imprimeix per la sortida estàndard.
2. **Obtenir informació d'un llibre**: El client pot cercar informació detallada d'un llibre especificat per l'usuari i la mostra per la sortida estàndard.
3. **Afegir un llibre**: Permet a l'usuari afegir un nou llibre a la "base de dades" del servidor.
4. **Eliminar un llibre**: Permet a l'usuari eliminar un llibre existent de la "base de dades" del servidor.
5. **Sortir**: Tanca la connexió amb el servidor i finalitza l'execució del programa client.

## Comentaris del Codi

El codi està comentat per facilitar la comprensió i descriu els següents punts clau:

- Connexió al servidor mitjançant l'adreça IP i el port especificats.
- Gestió de l'entrada/sortida de dades amb el servidor.
- Impressió del menú d'opcions per a l'usuari.
- Obtenció de l'opció de l'usuari i envio al servidor.
- Gestió de les diferents opcions seleccionades per l'usuari.
- Recepció i impressió de les dades enviades pel servidor.
- Maneig d'errors durant l'execució del client.

## Dependències

Aquest client utilitza classes proporcionades per Java per gestionar la connexió amb el servidor, com `Socket`, `ObjectInputStream` i `ObjectOutputStream`.

## Instruccions d'Ús

Per utilitzar el client, compileu el codi i executeu l'aplicació. El client es connectarà al servidor i us permetrà interactuar amb la "base de dades" de llibres.
