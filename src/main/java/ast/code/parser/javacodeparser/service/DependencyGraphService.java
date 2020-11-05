package ast.code.parser.javacodeparser.service;

import ast.code.parser.javacodeparser.FileHandler;
import ast.code.parser.javacodeparser.ParserFactory;
import ast.code.parser.javacodeparser.models.DependencyModel;
import ast.code.parser.javacodeparser.typevisitors.ClassVisitors;
import ast.code.parser.javacodeparser.typevisitors.MethodInvocationVisitors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class DependencyGraphService {

    private Set<Map.Entry<String, String>> entries = new HashSet<>();
    private Set<Map<String, Set<String>>> maps = new HashSet<>();
    private Set<String> strings = new HashSet<>();

    private Set<DependencyModel> dependencyModels = new HashSet<>();

    public Set<DependencyModel> generateDependencyGraph(String projectPath) {
        List<File> projectFiles = FileHandler.readJavaFiles(new File(projectPath));

        ClassVisitors classVisitors = new ClassVisitors();
        projectFiles.stream().forEach(file -> {
            try {
                String content = FileHandler.read(file.getAbsolutePath());
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(classVisitors);
                classVisitors.getClasses().forEach(typeDeclaration -> {
                    DependencyModel dependencyModel = new DependencyModel();
                    dependencyModel.setClassName(typeDeclaration.getName().toString());
                    Map<String, Set<String>> setMap = new HashMap<>();
                    MethodInvocationVisitors methodInvocationVisitors = new MethodInvocationVisitors();
                    typeDeclaration.accept(methodInvocationVisitors);
                    Set<String> collect = methodInvocationVisitors.getMethods()
                            .stream()
                            .map(MethodInvocation::getExpression)
                            .filter(Objects::nonNull)
                            .map(Expression::resolveTypeBinding)
                            .filter(Objects::nonNull)
                            .map(ITypeBinding::getName)
                            .collect(Collectors.toSet());
                    //setMap.put(typeDeclaration.getName().toString(), collect);
                    dependencyModel.setDependencies(collect);
                    dependencyModels.add(dependencyModel);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return dependencyModels;
    }

}
