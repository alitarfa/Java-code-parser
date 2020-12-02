package ast.code.parser.javacodeparser;


import ast.code.parser.javacodeparser.service.ProjectParser;

public class ASTMain7 {

    public static void main(String[] args) throws Exception {
        String projectPath = "/home/tarfa/AndroidStudioProjects/MicroAppClusterA/app/src/main/java/com/carl/touch/";
        ProjectParser projectParser = new ProjectParser();
        projectParser.packageReplacement(projectPath);
    }

}
