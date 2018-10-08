import Diagram.Automatic;
import io.Reader;
import parser.Scanner;

public class MainAutomatic {
    public static void main(String[] args) {
        String text;
        try {
            text = Reader.getDate("Test.jaton");
        } catch (Exception e) {
            System.out.println("Неверны тип файла");
            return;
        }

        Scanner scanner = new Scanner(text);
        Automatic automatic = new Automatic(scanner);
        automatic.start();
    }
}
