import java.io.*;
import java.net.*;

public class Server {
    private static final String BOOKS_DB_NAME = "booksDB.dat";
    private static BooksDB booksDB;                                                          // Gestor de base de dades de llibres

    public static void main(String[] args) {
        try {
            booksDB = new BooksDB(BOOKS_DB_NAME);                                            // Utilitza la classe BooksDB per obtindre la "base de dades" i la guarda en la variable booksDB
            ServerSocket serverSocket = new ServerSocket(4444);                         // Es crea el socket del server en el port 4444
            System.out.println("Servidor actiu, a l'espera d'usuaris...");
            while (true) {
                Socket clientSocket = serverSocket.accept();                                 // Accepta una connexió entrant del client
                System.out.println("Client conectat: " + clientSocket);                      // Indica que s'ha connectat un client

                Thread clientHandler = new Thread(() -> handleClient(clientSocket));         // Crea un fil per gestionar el client
                clientHandler.start();                                                       // Inicia el fil per gestionar el client
            }
        } catch (IOException ex) {
            System.err.println("Error inicinat el servidor: " + ex.getMessage());            // Maneig d'errors en iniciar el servidor
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream()); // per enviar dades al client
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());     // per rebre dades del client

            for (;;) {                                                                       // Bucle infinit per gestionar les peticions del client
                int option = in.readInt();                                                  
                switch (option) {
                    case 1:
                        sendTitles(out);                                                     // Envia títols de llibres al client
                        break;
                    case 2:
                        sendBookInfo(in, out);                                               // Envia informació d'un llibre al client
                        break;
                    case 3:
                        receiveAndAddBook(in, out);                                          // Rep i afegeix un llibre a la base de dades
                        break;
                    case 4:
                        receiveAndDeleteBook(in, out);                                       // Rep i elimina un llibre de la base de dades
                        break;
                    case 5:
                        quit(clientSocket, out, in);                                         // Tanca la connexió amb el client
                        return;
                }
            }
        } catch (IOException ex) {
            System.err.println("Error gestionant el client: " + ex.getMessage()); 
        }
    }

    private static void sendTitles(ObjectOutputStream out) throws IOException {
        int numBooks = booksDB.getNumBooks(); 
        out.writeInt(numBooks); 
        out.flush(); 

        for (int i = 0; i < numBooks; i++) {
            try {
                BookInfo book = booksDB.readBookInfo(i); 
                out.writeObject(book.getTitle());                                            // Envia el títol del llibre al client
                out.flush();                                                                 // Assegura que les dades s'envien immediatament
            } catch (IOException ex) {
                System.err.println("Error a la base de dades"); 
            }
        }
    }

    private static void sendBookInfo(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        try {
            String title = (String) in.readObject();                                        // Rep el títol del llibre del client
            int index = booksDB.searchBookByTitle(title);                                   // Cerca el llibre per títol a la base de dades
            out.writeBoolean(index != -1);                                                  // Envia un indicador d'èxit o fracàs al client
            out.flush();
    
            if (index != -1) {                                                              // Si el llibre existeix
                BookInfo book = booksDB.readBookInfo(index);                                // Llegeix la informació del llibre de la base de dades
                byte[] bookBytes = book.toBytes();                                          // Obté els bytes de la informació del llibre
    
                // Envia els bytes de la informació del llibre al client
                out.writeInt(bookBytes.length);
                out.write(bookBytes);
                out.flush();
            }
        } catch (ClassNotFoundException ex) {
            System.err.println("Classe no trobada: " + ex.getMessage());
        }
    }
    
    
    

    private static void receiveAndAddBook(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        try {
            BookInfo book = (BookInfo) in.readObject(); 
            boolean success = booksDB.insertNewBook(book);                                   // Insereix el llibre a la base de dades
            out.writeBoolean(success);                                                       // Envia un indicador d'èxit o fracàs al client
            out.flush(); 
        } catch (ClassNotFoundException ex) {
            System.err.println("Classe no trobada: " + ex.getMessage()); // Maneixa errors de classe no trobada
        }
    }

    private static void receiveAndDeleteBook(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        try {
            String title = (String) in.readObject(); 
            boolean success = booksDB.deleteByTitle(title);                                  // Elimina el llibre de la base de dades
            out.writeBoolean(success); 
            out.flush();                                                                     
        } catch (ClassNotFoundException ex) {
            System.err.println("Classe no trobada: " + ex.getMessage());
        }
    }

    private static void quit(Socket clientSocket, ObjectOutputStream out, ObjectInputStream in) {
        try {
            clientSocket.close();                                                            // Tanca el socket del client
            booksDB.close();                                                                 // Tanca la base de dades
            System.out.println("Client disconnected.");
            out.close(); 
            in.close(); 
            System.exit(0);                                                           // Finalitza el programa
        } catch (IOException ex) {
            System.err.println("Error al tancar: " + ex.getMessage()); 
            System.exit(-1); 
        }
    }
}
