import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4444);                                  // Connecta al servidor a l'adreça i port especificats
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());                // per enviar dades al servidor
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());                    // per rebre dades del servidor
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));          // per llegir les opcions de l'usuari

            while (true) {
                printMenu();                                                                          // Imprimeix el menú de opcions per a l'usuari
                int option = getOption(userInput);                                                    // Obté l'opció de l'usuari
                out.writeInt(option);                                                                 // Envia l'opció al servidor
                out.flush();                                                                         

                switch (option) {
                    case 1:
                        receiveAndPrintTitles(in);                                                    // Rep i imprimeix els títols dels llibres enviats pel servidor
                        break;
                    case 2:
                        searchAndPrintBookInfo(userInput, out, in);                                   // Cerca i imprimeix la informació d'un llibre especificat per l'usuari
                        break;
                    case 3:
                        addBook(userInput, out, in);                                                  // Afegeix un llibre especificat per l'usuari a la base de dades del servidor
                        break;
                    case 4:
                        deleteBook(userInput, out, in);                                               // Elimina un llibre especificat per l'usuari de la base de dades del servidor
                        break;
                    case 5:
                        quit(socket, out, in, userInput);                                             // Tanca la connexió amb el servidor i finalitza el programa
                        return;
                }
            }
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        } 
    }

    private static void printMenu() {
        // Imprimeix el menú de opcions per a l'usuari
        System.out.println("Menú de opciones:");
        System.out.println("1 - Listar todos los títulos.");
        System.out.println("2 - Obtener información de un libro.");
        System.out.println("3 - Añadir un libro.");
        System.out.println("4 - Eliminar un libro.");
        System.out.println("5 - Salir.");
    }

    private static int getOption(BufferedReader userInput) {
        // Obté l'opció de l'usuari a través de l'entrada estàndard
        int option = 0;
        try {
            System.out.println("Elija una opción: ");
            option = Integer.parseInt(userInput.readLine());
        } catch (IOException | NumberFormatException ex) {
            System.err.println("Error: " + ex.getMessage()); // Maneixa errors d'entrada/sortida o de conversió de tipus
        }
        return option;
    }

    private static void receiveAndPrintTitles(ObjectInputStream in) throws IOException {
        // Rep i imprimeix els títols dels llibres enviats pel servidor
        try {
            int numBooks = in.readInt();
            System.out.println("Número de libros: " + numBooks);
            for (int i = 0; i < numBooks; i++) {
                String title = (String) in.readObject();
                System.out.println(title);
            }
        } catch (ClassNotFoundException ex) {
            System.err.println("Classe no trobada: " + ex.getMessage()); // Maneixa errors de classe no trobada
        }
    }

    private static void searchAndPrintBookInfo(BufferedReader userInput, ObjectOutputStream out, ObjectInputStream in) throws IOException {
        // Cerca i imprimeix la informació d'un llibre especificat per l'usuari
        try {
            System.out.println("Escriu el títol del llibre: ");
            String title = userInput.readLine();
            
            // Envia el títol del llibre al servidor
            out.writeObject(title);
            out.flush();
    
            // Rep el missatge del servidor indicant si el llibre ha estat trobat
            boolean found = in.readBoolean();
    
            if (found) {
                // Si el llibre ha estat trobat, rep i imprimeix la informació del llibre
                int length = in.readInt();
                byte[] bookBytes = new byte[length];
                in.readFully(bookBytes);
                BookInfo book = BookInfo.fromBytes(bookBytes);
                System.out.println("Informació del llibre:");
                System.out.println(book);
            } else {
                // Si el llibre no ha estat trobat, mostra un missatge indicant-ho
                System.out.println("Llibre no trobat.");
            }
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
    
    

    private static void addBook(BufferedReader userInput, ObjectOutputStream out, ObjectInputStream in) throws IOException {
        // Afegeix un llibre especificat per l'usuari a la base de dades del servidor
        try {
            System.out.println("Escriba el título del libro a añadir: ");
            String title = userInput.readLine();
            System.out.println("Introduzca el número de páginas: ");
            int pages = Integer.parseInt(userInput.readLine());
            System.out.println("Indique el autor (deje en blanco si es anónimo): ");
            String author = userInput.readLine();
            System.out.println("Especifique la serie (deje en blanco si es un libro suelto): ");
            String series = userInput.readLine();

            BookInfo book = new BookInfo(title, pages, author, series);
            out.writeObject(book);
            out.flush();

            boolean success = in.readBoolean();
            if (!success) {
                System.out.println("¡Este libro ya estaba en la base de datos!");
            }
        } catch (NumberFormatException ex) {
            System.err.println("Error: " + ex.getMessage()); 
        }
    }

    private static void deleteBook(BufferedReader userInput, ObjectOutputStream out, ObjectInputStream in) throws IOException {
        // Elimina un llibre especificat per l'usuari de la base de dades del servidor
        System.out.println("Escriba el título del libro a eliminar: ");
        String title = userInput.readLine();
        out.writeObject(title);
        out.flush();

        try {
            boolean success = in.readBoolean();
            if (!success) {
                System.out.println("Libro no encontrado.");
            }
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    private static void quit(Socket socket, ObjectOutputStream out, ObjectInputStream in, BufferedReader userInput) {
        // Tanca la connexió amb el servidor i finalitza el programa
        try {
            socket.close();
            out.close();
            in.close();
            userInput.close();
            System.exit(0); // Finalitza el programa
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage()); 
            System.exit(-1); // Finalitza el programa amb un error
        }
    }
}
