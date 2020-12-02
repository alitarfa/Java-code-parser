package ast.code.parser.javacodeparser;


import ast.code.parser.javacodeparser.service.FileHandler;
import ast.code.parser.javacodeparser.service.ProjectParser;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class ASTMain7 {

    public static void main(String[] args) throws Exception {
        String projectPath = "/home/tarfa/AndroidStudioProjects/MicroAppClusterA/app/src/main/java/com/carl/touch/";
        ProjectParser projectParser = new ProjectParser();
        projectParser.packageReplacement(projectPath);

        // just for testing
        // after this one go and work on the GUI Angular part
        List<File> files = FileHandler.readJavaFiles(new File(projectPath));
        files.stream()
                .map(File::getAbsoluteFile)
                .map(File::toString)
                .filter(file -> file.contains("CarlActivity"))
                .forEach(System.out::println);
    }

}
