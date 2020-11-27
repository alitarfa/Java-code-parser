package ast.code.parser.javacodeparser.service;

import ast.code.parser.javacodeparser.typevisitors.ClassVisitors;
import ast.code.parser.javacodeparser.typevisitors.MethodInvocationVisitors;
import ast.code.parser.javacodeparser.typevisitors.MethodVisitors;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
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
        return entries;
    }
}
