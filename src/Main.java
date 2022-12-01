import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        runApp(new App());
    }

    static void runApp(App app) {
        app.showIntro();
        int choice = 0;
        while (choice != 3) {
            app.mainMenu();
            choice = app.userInput();
            app.executeChoice(choice);
        }

    }
}

class App {

    Scanner scan;
    Folder folder;

    App() {
        scan = new Scanner(System.in);
        folder = new Folder();
    }

    void showIntro() {
        System.out.println("Locked App.");
        System.out.println("Developed by Ahmad AlAmiri");
    }

    void mainMenu() {
        System.out.println("What would you like to do?");
        System.out.println("1- show files in directory");
        System.out.println("2- go to action menu context");
        System.out.println("3- exit");
        System.out.print("choice: ");
    }

    int userInput() {
        int input = scan.nextInt();
        scan.nextLine();
        return input;
    }

    void executeChoice(int choice) {
        switch (choice) {
            case (1) -> showFiles();
            case (2) -> folderCommands();
            case (3) -> exit();
            default -> errorMessage();
        }
    }

    void errorMessage() {
        System.out.println("Incorrect input");
    }

    void showFiles() {
        System.out.println(folder.files());
    }

    void folderCommands() {
    }

    void exit() {
        System.out.println("Thank you for using Locked app.");
    }

}

record Folder(ArrayList<String> files) {

    static final String PATH = "app-folder";

    Folder() {
        this(readFiles(PATH));
    }

    static ArrayList<String> readFiles(String path) {
        File folder = new File(path);
        if (!folder.exists()) folder.mkdir();
        File[] folderContents = new File(path).listFiles();
        ArrayList<String> files = new ArrayList<>();
        assert folderContents != null;
        for (File f : folderContents) {
            if (f.isFile()) files.add(f.getName());
        }
        return files;
    }
}