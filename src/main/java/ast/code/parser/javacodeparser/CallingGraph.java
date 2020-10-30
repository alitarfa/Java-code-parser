package ast.code.parser.javacodeparser;

import ast.code.parser.javacodeparser.Types.ClassVisitors;
import ast.code.parser.javacodeparser.Types.MethodInvocationVisitors;
import ast.code.parser.javacodeparser.Types.MethodVisitors;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CallingGraph {

    private Set<Map.Entry<String, String>> entries = new HashSet<>();

    public Set<Map.Entry<String, String>> generateGraph(ClassVisitors classVisitors) {
        classVisitors.getClasses()
                .forEach(typeDeclaration -> {
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
                                            System.out.println(expr.resolveTypeBinding());
                                            String invoked = "";
                                            if (expr != null) {
                                                invoked = expr + "::" + methodInvocation.getName().toString();
                                            } else {
                                                invoked = typeDeclaration.getName() + "::" + methodInvocation.getName().toString();
                                            }
                                            entries.add(Map.entry(typeDeclaration.getName() + "::" + nameMethod.toString(), invoked));
                                        });
                            });

                });
        return entries;
    }
}
