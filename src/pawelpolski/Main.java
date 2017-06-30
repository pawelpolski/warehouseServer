package pawelpolski;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by macbookpro on 29.06.2017.
 */
public class Main {
    public static void main(String args[]) {
        try {
            Server server = new Server(9999);
            server.start();
            Scanner scan = new Scanner(System.in);

            while (true) {
                String command = scan.nextLine();
                if (command.equals("EXIT")) {
                    server.close();
                    break;
                } else if (command.equals("RESET")) {
                    server.reset();
                }
            }
        } catch (IOException ex) {
            System.out.println("Blad main");
        }
    }
}
