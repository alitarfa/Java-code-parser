package ast.code.parser.javacodeparser.service;

import ast.code.parser.javacodeparser.typevisitors.MethodVisitors;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewResolver {

    public static Set<String> findActivityView(TypeDeclaration typeDeclaration) {
        MethodVisitors methodVisitors = new MethodVisitors();
        typeDeclaration.accept(methodVisitors);
        return methodVisitors.getMethods().stream()
                .filter(method -> method.getName().toString().equalsIgnoreCase("onCreate"))
                .map(MethodDeclaration::getBody)
                .map(ASTNode::toString)
                .map(body -> body.split(";"))
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .filter(s -> s.contains("setContentView"))
                .map(s -> s.split("\\."))
                .map(strings -> strings[strings.length - 1])
                .map(s -> s.split("\\)"))
                .map(strings -> strings[0])
                .collect(Collectors.toSet());
    }

    public static Set<String> findFragmentView(TypeDeclaration typeDeclaration) {
        // TODO: 25/11/2020 this not yet tested, test it first

        MethodVisitors methodVisitors = new MethodVisitors();
        typeDeclaration.accept(methodVisitors);
        return methodVisitors.getMethods().stream()
                .filter(method -> method.getName().toString().equalsIgnoreCase("onCreateView")
                        || method.getName().toString().equalsIgnoreCase("build")
                        || method.getName().toString().equalsIgnoreCase("initViews")
                        || method.getName().toString().equalsIgnoreCase("getItemListAdapter")
                )
                .map(MethodDeclaration::getBody)
                .filter(Objects::nonNull)
                .map(ASTNode::toString)
                .map(body -> body.split(";"))
                .map(Arrays::asList)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(s -> s.contains("R.layout"))
                .filter(Objects::nonNull)
                .map(s -> s.split("\\."))
                .map(strings -> strings[strings.length - 1])
                .map(s -> s.split(","))
                .map(strings -> strings[0])
                .map(s -> s.split("\\)"))
                .map(strings -> strings[0])
                .collect(Collectors.toSet());
    }
}
