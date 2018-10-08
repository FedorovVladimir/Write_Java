import io.Reader;
import parser.Scanner;
import parser.Token;
import parser.TokenType;

public class MainScanner {
    public static void main(String[] args) {
        String text;
        try {
            text = Reader.getDate("Test.jaton");
        } catch (Exception e) {
            System.out.println("Неверны тип файла");
            return;
        }

        Scanner scanner = new Scanner(text);
        Token token;
        try {
            do {
                token = scanner.nextScanner();
                System.out.println(token);
            } while (token.getType() != TokenType.EOF);
        } catch (Exception e) {
            e.fillInStackTrace();
        }

    }
}
