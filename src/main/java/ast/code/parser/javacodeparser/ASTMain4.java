package ast.code.parser.javacodeparser;


import ast.code.parser.javacodeparser.models.DependencyModel;
import ast.code.parser.javacodeparser.service.ActivityParser;
import ast.code.parser.javacodeparser.service.jsonhandler.JsonHandler;

import java.io.IOException;
import java.util.Set;

public class ASTMain4 {

    public static void main(String[] args) throws IOException {
        ActivityParser activityVsActivity = new ActivityParser();
        Set<DependencyModel> dependencyModels = activityVsActivity.constructGraph("/home/tarfa/Phd/carl-mob-app/android");
        JsonHandler<Set<DependencyModel>> jsonHandler = new JsonHandler<>();
        jsonHandler.generateJson(dependencyModels);
    }
}
