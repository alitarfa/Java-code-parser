package ast.code.parser.javacodeparser;

import ast.code.parser.javacodeparser.visitors.MethodInvocationImp;
import ast.code.parser.javacodeparser.visitors.TypeDeclarationImp;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import org.jboss.shrinkwrap.resolver.api.Resolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class ASTMain3 {


    public static void main(String[] args) throws Exception {
        String jdkPath = "/usr/lib/jvm/java-11-openjdk-amd64/";
        String projectPath = "/home/tarfa/MySpace/Tekit/soon-back/src/main/java";

        final File[] depsOpenshift = Resolvers.use(MavenResolverSystem.class).
                loadPomFromFile("/home/tarfa/MySpace/Tekit/soon-back/pom.xml").
                importRuntimeAndTestDependencies().
                resolve().
                withTransitivity().
                asFile();

        List<File> projectFiles = FileHandler.readJavaFiles(new File("/home/tarfa/MySpace/Tekit/soon-back/src/main"));
        CombinedTypeSolver combinedSolver = new CombinedTypeSolver
                (
                        new JavaParserTypeSolver(new File(projectPath)),
                        new JavaParserTypeSolver(new File(jdkPath)),
                        new ReflectionTypeSolver()
                );

        for (File file : depsOpenshift) {
            combinedSolver.add(new JarTypeSolver(file));
        }

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedSolver);
        StaticJavaParser
                .getConfiguration()
                .setSymbolResolver(symbolSolver);

        projectFiles.forEach(file -> {
            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(file.getAbsoluteFile());
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

        });
    }
}
