package ast.code.parser.javacodeparser;

import ast.code.parser.javacodeparser.service.DependencyGraphService;
import ast.code.parser.javacodeparser.typevisitors.ClassVisitors;
import org.eclipse.jdt.core.dom.CompilationUnit;


public class ASTMain2 {

    public static void main(String args[]) throws Exception {
        String read = FileHandler.read("/home/tarfa/projects/main.java");
        CompilationUnit instance = ParserFactory.getInstance(read);
        ClassVisitors classVisitors = new ClassVisitors();
        instance.accept(classVisitors);
        DependencyGraphService dependencyGraphService = new DependencyGraphService();
        dependencyGraphService.generateDependencyGraph("/home/tarfa/MySpace/Tekit/soon-back");
    }
}
