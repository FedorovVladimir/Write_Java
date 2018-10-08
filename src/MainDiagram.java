import Diagram.Diagram;
import io.Reader;
import parser.Scanner;
import parser.Token;
import parser.TokenType;

public class MainDiagram {
    public static void main(String[] args) {

        String text;
        try {
            text = Reader.getDate("Test.jaton");
        } catch (Exception e) {
            System.out.println("Неверны тип файла");
            return;
        }

        Scanner scanner = new Scanner(text);
        Diagram diagram = new Diagram(scanner);

        diagram.program();

        Token token = scanner.nextScanner();
        if (token.getType() == TokenType.EOF)
            System.out.println("Синтаксических ошибок не обнаружено!");
        else
            diagram.printError("Лишний текст в конце программы");
    }

}
