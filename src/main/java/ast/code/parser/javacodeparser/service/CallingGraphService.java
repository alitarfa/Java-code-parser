package ast.code.parser.javacodeparser.service;

import ast.code.parser.javacodeparser.typevisitors.ClassVisitors;
import ast.code.parser.javacodeparser.typevisitors.MethodInvocationVisitors;
import ast.code.parser.javacodeparser.typevisitors.MethodVisitors;
import lombok.AllArgsConstructor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class CallingGraphService {

    private Set<Map.Entry<String, String>> entries = new HashSet<>();

    public Set<Map.Entry<String, String>> generateGraph(String projectPath) {
        List<File> projectFiles = FileHandler.readJavaFiles(new File(projectPath));
        ClassVisitors classVisitors = new ClassVisitors();
        projectFiles.forEach(file -> {
            try {
                String content = FileHandler.read(file.getAbsolutePath());
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(classVisitors);
                classVisitors.getClasses().forEach(typeDeclaration -> {
                    MethodVisitors methodVisitors = new MethodVisitors();
                    typeDeclaration.accept(methodVisitors);
                    methodVisitors.getMethods()
                            .forEach(methodDeclaration -> {
                                SimpleName nameMethod = methodDeclaration.getName();
                                MethodInvocationVisitors methodInvocationVisitors = new MethodInvocationVisitors();
                                methodDeclaration.accept(methodInvocationVisitors);
                                methodInvocationVisitors.getMethods()
                                        .forEach(methodInvocation -> {
                                            Expression expr = methodInvocation.getExpression();
                                            String invoked = "";
                                            if (expr != null) {
                                                ITypeBinding type = expr.resolveTypeBinding();
                                                if (type != null) {
                                                    invoked = type.getName() + "::" + methodInvocation.getName().toString();
                                                } else {
                                                    invoked = expr + "::" + methodInvocation.getName().toString();
                                                }
                                            } else {
                                                invoked = typeDeclaration.getName() + "::" + methodInvocation.getName().toString();
                                            }
                                            entries.add(Map.entry(typeDeclaration.getName() + "::" + nameMethod.toString(), invoked));
                                        });
                            });

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return entries;
    }

}
