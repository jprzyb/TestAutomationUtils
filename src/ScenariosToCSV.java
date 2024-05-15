import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ScenariosToCSV {
    private static String[] headers = new String[]
            {"Key", "Name", "Status", "Precondition", "Objective", "Folder",
            "Priority", "Component", "Labels", "Estimated Time", "Coverage (Issues)",
            "Coverage (Pages)", "Test Script (Step-by-Step) - Step",
            "Test Script (Step-by-Step) - Test Data", "Test Script (Step-by-Step) - Expected Result",
            "Test Script (Plain Text)", "Test Script (BDD)"};
    public static void main(String[] args) {

        List<String> paths = findFeatureFiles();
        for (String p : paths){
            printCSV(getScenariosFromFile(p), p.replace(".feature",".csv"), List.of(headers));
        }
    }
    private static void printCSV(List<String> scenarios, String outFilePath, List<String> headers){
        String env ="";
        if(outFilePath.contains("DEV")) env="DEV";
        else if(outFilePath.contains("SIT")) env="SIT";
        else if(outFilePath.contains("UAT")) env="UAT";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outFilePath))){
            for(String h : headers){
                writer.print(h+";");
            }
            writer.print("\n");
            for (String s : scenarios){
                writer.print("TC-x"+";");
                writer.print(s+";");
                writer.print(""+";");
                writer.print(""+";");
                writer.print(outFilePath.replace(".csv","")+"/"+env+";");
                writer.print("Normal"+";");
                writer.print(""+";");
                writer.print(env+";");
                writer.print(""+";");
                writer.print(""+";");
                writer.print(""+";");
                writer.print(""+";");
                writer.print(""+";");
                writer.print(""+";");
                writer.print(""+";");
                writer.print(""+";");
                writer.print("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static List<String> getScenariosFromFile(String path){
        List<String> scenarios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Scenario: ")) {
                    System.out.println(line);
                    line = line.replaceAll("\t","");
                    scenarios.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scenarios;
    }
    private static List<String> findFeatureFiles(){
        List<String> paths = new ArrayList<>();
        // Ścieżka do katalogu, w którym zaczynamy wyszukiwanie
        String startDir = ""; // zmień tę ścieżkę na odpowiednią

        // Przechodzenie po drzewie katalogów i wyszukiwanie plików z rozszerzeniem .feature
        try {
            Files.walkFileTree(Paths.get(startDir), EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".feature")) {
                        paths.add(file.toAbsolutePath().toString());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }
}