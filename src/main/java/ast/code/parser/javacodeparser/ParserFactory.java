package ast.code.parser.javacodeparser;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Map;

public class ParserFactory {

    public static CompilationUnit getInstance(String content) {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);
        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);
        parser.setUnitName("");
        String[] sources = {""};
        String[] classpath = {""};
        parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);
        parser.setSource(content.toCharArray());
        CompilationUnit instance = (CompilationUnit) parser.createAST(null);
        if (instance.getAST().hasBindingsRecovery()) {
          //  System.out.println("Binding activated.");
        }
        return instance;
    }
}
