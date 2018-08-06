import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class ReadXML implements Commandable {

    private InputStream inputStream;

    @Override
    public String toString(){

        return "1. Read XML File";
    }

    public void Invoke(Menu menu){

        if (menu.isStartGame() == false) {

            inputStream = getXMLInputFromUser();

            if (inputStream != null) {

                try {
                    menu.setTheGame(GameFactory.CreateGame(inputStream));
                    menu.setLoadedXML(true);
                    menu.showBoard();
                } catch (FileDataException e) {

                    System.out.println(e.getMessage());

                } catch (JAXBException e) {

                    throw new RuntimeException();
                }
            }
        }
    }

    private InputStream getXMLInputFromUser() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the path of the XML file: ");
        String input = scanner.nextLine();

        File tmpFile = new File(input);
        if (!tmpFile.exists()) {

            System.out.println("The file does not exist");
            return null;
        }
        else {

            if (tmpFile.isFile()) {

                String extension;

                int index = input.lastIndexOf('.');
                extension = input.substring(index+1);
                extension = extension.toLowerCase();
                if (extension.matches("xml")) {

                    try {
                        InputStream inputStream = Files.newInputStream(Paths.get(input));
                        return inputStream;
                    }
                    catch (IOException e) {

                        throw new RuntimeException();
                    }
                }
                else {

                    System.out.println("Not an XML file");
                    return null;
                }
            }
            else {

                System.out.println("The specified path is not a file");
                return null;
            }
        }
    }

}
