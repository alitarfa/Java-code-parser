package ast.code.parser.javacodeparser;


import ast.code.parser.javacodeparser.service.ActivityParser;

import java.io.IOException;

public class ASTMain4 {

    public static void main(String[] args) throws IOException {
        ActivityParser activityVsActivity = new ActivityParser();
        activityVsActivity.constructGraph("/home/tarfa/Phd/carl-mob-app/android");
    }
}
