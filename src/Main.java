import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
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

    private final Scanner scan;
    private final Folder folder;

    App() {
        scan = new Scanner(System.in);
        folder = new Folder();
    }

    void showIntro() {
        System.out.println("Locked App.");
        System.out.println("Developed by Ahmad AlAmiri");
    }

    void mainMenu() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("1- show files in directory");
        System.out.println("2- operate on directory");
        System.out.println("3- exit");
        System.out.print("choice: ");
    }

    int userInput() {
        try {
            int input = scan.nextInt();
            scan.nextLine();
            return input;
        } catch (InputMismatchException e) {
            scan.nextLine();
            return Integer.MIN_VALUE;
        }
    }

    void executeChoice(int choice) {
        switch (choice) {
            case (Integer.MIN_VALUE) -> inputMismatchError();
            case (1) -> showFiles();
            case (2) -> directoryOperations();
            case (3) -> exit();
            default -> incorrectChoiceError();
        }
    }

    private void incorrectChoiceError() {
        System.out.println("Incorrect choice");
    }

    private void inputMismatchError() {
        System.out.println("Please input an integer");
    }

    private void showFiles() {
        System.out.println(folder.files());
    }

    private void directoryOperations() {
        int choice = 0;
        while (choice != 4) {
            actionMenu();
            choice = userInput();
            executeActionChoice(choice);
        }
    }

    private void exit() {
        System.out.println("Thank you for using Locked app.");
    }

    private void actionMenu() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("1- add a file");
        System.out.println("2- delete a file");
        System.out.println("3- search for a file");
        System.out.println("4- go back");
        System.out.print("choice: ");
    }

    private void executeActionChoice(int choice) {
        try {
            switch (choice) {
                case (Integer.MIN_VALUE) -> inputMismatchError();
                case (1) -> addFile();
                case (2) -> deleteFile();
                case (3) -> searchFiles();
                case (4) -> {}
                default -> incorrectChoiceError();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addFile() {
        String file = takeFileName();
        folder.addFile(file);
    }

    private void deleteFile() {
        String file = takeFileName();
        folder.deleteFile(file);

    }


    private void searchFiles() {
        String query = takeFileName();
        folder.searchFiles(query);
    }

    private String takeFileName() {
        System.out.print("File Name: ");
        String file = scan.nextLine();
        if (file.equals("")) throw new IllegalArgumentException("file name can not be empty");
        return file;
    }


}

record Folder(ArrayList<String> files) {

    static final String PATH = "app-folder";

    Folder() {
        this(readFiles(PATH));
    }

    static ArrayList<String> readFiles(String path) {
        File folder = new File(path);
        if (!folder.exists()) //noinspection ResultOfMethodCallIgnored
            folder.mkdir();
        File[] folderContents = folder.listFiles();
        ArrayList<String> files = new ArrayList<>();
        assert folderContents != null;
        for (File f : folderContents) {
            if (f.isFile()) files.add(f.getName());
        }
        return files;
    }

    void addFile(String fileName) {
        String path = String.format("%s/%s", PATH, fileName);
        try {
            File file = new File(path);
            if (file.createNewFile()) {
                System.out.printf("file %s created successfully%n", file.getName());
                files.add(file.getName());
                sortFiles();
            } else
                System.out.println("File already exists.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }

    void deleteFile(String fileName) {
        String path = String.format("%s/%s", PATH, fileName);
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("File does not exist");
            return;
        }
        if (file.delete()) {
            files.removeIf(s -> s.equals(file.getName()));
            System.out.printf("file %s deleted successfully%n", file.getName());
            return;
        }
        System.out.println("Failed to delete the file");
    }

    void searchFiles(String query) {
        if (files.contains(query)) {
            System.out.printf("%s file found%n", query);
            return;
        }
        ArrayList<String> filesMatched = new ArrayList<>();
        for (String f : files)
            if (f.contains(query)) filesMatched.add(f);
        if (filesMatched.isEmpty())
            System.out.println("no possible matches");
        else
            System.out.print("possible matches: " + filesMatched);
    }

    private void sortFiles() {
        files.sort(String::compareTo);
    }
}