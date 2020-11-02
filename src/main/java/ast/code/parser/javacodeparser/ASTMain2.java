package ast.code.parser.javacodeparser;

import ast.code.parser.javacodeparser.typevisitors.ClassVisitors;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Map;
import java.util.Set;

public class ASTMain2 {

    public static void main(String args[]) throws Exception {
        String read = FileHandler.read("/home/tarfa/projects/main.java");
        CompilationUnit instance = ParserFactory.getInstance(read);
        ClassVisitors classVisitors = new ClassVisitors();
        instance.accept(classVisitors);
        CallingGraph callingGraph = new CallingGraph();
        Set<Map.Entry<String, String>> stringStringMap = callingGraph.generateGraph(classVisitors);
        stringStringMap.forEach(stringStringEntry -> {
            System.out.println(stringStringEntry.getKey() + " -----> " + stringStringEntry.getValue());
        });
    }
}
