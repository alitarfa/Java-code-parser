package ast.code.parser.javacodeparser.service;

import ast.code.parser.javacodeparser.typevisitors.MethodVisitors;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewResolver {

    public static void findActivityView(TypeDeclaration typeDeclaration) {
        MethodVisitors methodVisitors = new MethodVisitors();
        typeDeclaration.accept(methodVisitors);
        Set<String> collect = methodVisitors.getMethods().stream()
                .filter(method -> method.getName().toString().equalsIgnoreCase("onCreate"))
                .map(MethodDeclaration::getBody)
                .map(ASTNode::toString)
                .map(body -> body.split(";"))
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .filter(s -> s.contains("setContentView"))
                .map(s -> s.split("\\."))
                .map(strings -> strings[strings.length - 1])
                .collect(Collectors.toSet());
        collect.forEach(System.out::println);
    }
}
