import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateTests {
    public static char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    public static String REQ_DIR = "requests/";
    public static String PAY_DIR = "payloads/";
    public static String FEAT_DIR = "features/";
    public static String TC_PREF = "TC-";
    public static List<File> REQUESTS;
    public static void main(String[] args) {
        REQUESTS = new ArrayList<>();
        readFiles();
        int i = 0;
        for(File file : REQUESTS){
            generateTestsForFile(file, Character.toString(alphabet[i]));
            i++;
        }
    }

    public static void readFiles(){
        File dir = new File(REQ_DIR);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if(child.getName().endsWith(".json")){
                    REQUESTS.add(child);
                }
            }
        }
    }
    public static void generateTestsForFile(File file, String x){
        String apiName = file.getName().split("/")[file.getName().split("/").length-1].replace(".json","");
        StringBuilder featureString = new StringBuilder("Feature: Testing " + apiName + " Api\n\n");
        try{
            List<String> reqLines = Files.readAllLines(Paths.get(REQ_DIR+file.getName()), StandardCharsets.UTF_8);
            int i=1;
            for(String line : reqLines){
                if(isJSONPair(line)){
                    generatePayloadEmpty(TC_PREF+x+i, reqLines, line);
                    featureString.append(getScenarioEmpty(line, apiName, TC_PREF+x+i));
                    i++;
                    generatePayloadXXX(TC_PREF+x+i, reqLines, line);
                    featureString.append(getScenarioXXX(line, apiName, TC_PREF+x+i));
                    i++;
                    generatePayloadxxx(TC_PREF+x+i, reqLines, line);
                    featureString.append(getScenarioxxx(line, apiName, TC_PREF+x+i));
                    i++;
                }
            }
            printFile(FEAT_DIR + apiName + ".feature", String.valueOf(featureString));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static String getScenarioEmpty(String line, String apiName, String tcKey){
        String scenario =
                "\t@ApiName="+apiName+
                        "\n\t@TestCaseKey="+tcKey+
                        "\n\t@dev"+
                        "\n\t@pass"+
                        "\n\tScenario: Field " + line.split(":")[0].trim() + " is empty." +
                        "\n\t\tGiven: For given payload"+
                        "\n\t\tThen: records fetched with 200\n\n";
        return scenario;
    }
    private static String getScenarioXXX(String line, String apiName, String tcKey){
        String scenario =
                "\t@ApiName="+apiName+
                        "\n\t@TestCaseKey="+tcKey+
                        "\n\t@dev"+
                        "\n\t@pass"+
                        "\n\tScenario: Field " + line.split(":")[0].trim() + " is \"xxx\"." +
                        "\n\t\tGiven: For given payload"+
                        "\n\t\tThen: records fetched with 200\n\n";
        return scenario;
    }
    private static String getScenarioxxx(String line, String apiName, String tcKey){
        String scenario =
                "\t@ApiName="+apiName+
                        "\n\t@TestCaseKey="+tcKey+
                        "\n\t@dev"+
                        "\n\t@pass"+
                        "\n\tScenario: Field " + line.split(":")[0].trim()+ " is xxx." +
                        "\n\t\tGiven: For given payload"+
                        "\n\t\tThen: records fetched with 200\n\n";
        return scenario;
    }
    private static void generatePayloadEmpty(String tcKey, List<String> payload, String line){
        StringBuilder res = new StringBuilder();
        for(String l : payload){
            if(l.equals(line)){
                String[] x = l.split(":");
                res.append(x[0]);
                res.append(" : ");
                if(l.endsWith(","))  res.append("\"\",\n");
                else res.append("\"\"\n");
            }
            else res.append(l+"\n");
        }
        printFile(PAY_DIR+tcKey+".json", String.valueOf(res));
    }
    private static void generatePayloadXXX(String tcKey, List<String> payload, String line){
        StringBuilder res = new StringBuilder();
        for(String l : payload){
            if(l.equals(line)){
                String[] x = l.split(":");
                res.append(x[0]);
                res.append(" : ");
                if(l.endsWith(","))  res.append("\"xxx\",\n");
                else res.append("\"xxx\"\n");
            }
            else res.append(l+"\n");
        }
        printFile(PAY_DIR+tcKey+".json", String.valueOf(res));
    }
    private static void generatePayloadxxx(String tcKey, List<String> payload, String line){
        StringBuilder res = new StringBuilder();
        for(String l : payload){
            if(l.equals(line)){
                String[] x = l.split(":");
                res.append(x[0]);
                res.append(" : ");
                if(l.endsWith(",")) res.append("xxx,\n");
                else res.append("xxx\n");
            }
            else res.append(l+"\n");
        }
        printFile(PAY_DIR+tcKey+".json", String.valueOf(res));
    }
    private static void printFile(String path, String in){
        PrintWriter save = null;
        try {
            save = new PrintWriter(path);
            save.print(in);
            save.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isJSONPair(String line){
        line = line.trim();
        String regex = "^\"[^\"]+\"\\s*:\\s*\"[^\"]+\"\\s*,?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }
}
