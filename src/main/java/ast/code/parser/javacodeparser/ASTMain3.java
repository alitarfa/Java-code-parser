package ast.code.parser.javacodeparser;

import ast.code.parser.javacodeparser.visitors.MethodInvocationImp;
import ast.code.parser.javacodeparser.visitors.TypeDeclarationImp;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.metamodel.BaseNodeMetaModel;
import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;


public class ASTMain3 {

    public static void main(String[] args) throws Exception {

        Set<Map<String, Set<String>>> entries = new HashSet<>();

        String jdkPath = "/usr/lib/jvm/java-11-openjdk-amd64/";
        String mainClass = "/home/tarfa/projects/main.java";

        List<File> projectFiles = FileHandler.readJavaFiles(new File("/home/tarfa/MySpace/Tekit/soon-back/src/main"));

        CombinedTypeSolver combinedSolver = new CombinedTypeSolver
                (
                        new JavaParserTypeSolver(new File("/home/tarfa/MySpace/Tekit/soon-back/src/main/java/")),
                        new JavaParserTypeSolver(new File(jdkPath)),
                        new ReflectionTypeSolver()
                );

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        CompilationUnit cu = null;

        try {
            cu = StaticJavaParser.parse(projectFiles.get(7));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<ClassOrInterfaceDeclaration> classes = new ArrayList<>();
        TypeDeclarationImp typeDeclarationImp = new TypeDeclarationImp();
        typeDeclarationImp.visit(cu, classes);
        Set<String> collect = classes.stream()
                .map(classOrInterfaceDeclaration -> {
                    List<MethodCallExpr> collection = new ArrayList<>();
                    MethodInvocationImp methodInvocationImp = new MethodInvocationImp();
                    classOrInterfaceDeclaration.accept(methodInvocationImp, collection);
                    return collection;
                })
                .flatMap(Collection::stream)
                .map(methodCallExpr -> {
                    return methodCallExpr
                            .getScope()
                            .stream()
                            .filter(Expression::isNameExpr)
                            .map(Expression::calculateResolvedType)
                            .map(ResolvedType::asReferenceType)
                            .map(ResolvedReferenceType::getQualifiedName)
                            .map(s -> s.split("\\."))
                            .map(strings -> strings[strings.length - 1])
                            .collect(Collectors.toSet());
                })
                .filter(expressions -> expressions.size() != 0)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        collect.forEach(System.out::println);

    }

}
