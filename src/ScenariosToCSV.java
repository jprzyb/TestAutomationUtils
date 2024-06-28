/**
 * @author Jakub Przybylski
 * @license Proprietary
 * @date 28.06.2024
 * @description This code is protected by copyright.
 *              It may not be used, copied, or modified without explicit permission from the author.
 *              Contact: jakubp02.10@gmail.com
 */
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
        String[] p = outFilePath.split("\\\\");
        String csvName  = p[p.length-1];
        outFilePath = "csvFiles\\" + csvName;
        String env ="";
        String splitter = ",";
        if(outFilePath.contains("DEV")) env="DEV";
        else if(outFilePath.contains("SIT")) env="SIT";
        else if(outFilePath.contains("UAT")) env="UAT";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outFilePath))){
            for(String h : headers){
                writer.print(h+splitter);
            }
            writer.print("\n");
            for (String s : scenarios){
                writer.print("TC-x"+splitter);
                writer.print(s+splitter);
                writer.print("DRAFT"+splitter);
                writer.print(""+splitter);
                writer.print(""+splitter);
                writer.print(outFilePath.replace(".csv","")+"/"+env+splitter);
                writer.print("Normal"+splitter);
                writer.print(""+splitter);
                writer.print(env+splitter);
                writer.print(""+splitter);
                writer.print(""+splitter);
                writer.print(""+splitter);
                writer.print(""+splitter);
                writer.print(""+splitter);
                writer.print(""+splitter);
                writer.print(""+splitter);
                writer.print(""+splitter);
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
                    line = line.replaceAll("\t","");
                    line = line.replaceAll("Scenario: ","");
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
        String projectPath = String.valueOf(Paths.get("").toAbsolutePath());
        String[] dirSplit = projectPath.split("\\\\");
        String testAutomationPath = projectPath.replace(dirSplit[dirSplit.length-1],"");
        String startDir = testAutomationPath+"resources\\featureFiles\\";
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