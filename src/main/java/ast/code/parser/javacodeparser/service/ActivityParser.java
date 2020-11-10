package ast.code.parser.javacodeparser.service;

import ast.code.parser.javacodeparser.FileHandler;
import ast.code.parser.javacodeparser.ParserFactory;
import ast.code.parser.javacodeparser.models.ClassType;
import ast.code.parser.javacodeparser.models.DependencyModel;
import ast.code.parser.javacodeparser.typevisitors.ClassVisitors;
import ast.code.parser.javacodeparser.typevisitors.MethodInvocationVisitors;
import ast.code.parser.javacodeparser.typevisitors.MethodVisitors;
import ast.code.parser.javacodeparser.typevisitors.VariableVisitors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jdt.core.dom.*;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ast.code.parser.javacodeparser.models.ClassType.isActivity;
import static ast.code.parser.javacodeparser.models.ClassType.isFragment;

@Service
public class ActivityParser {

    private Set<DependencyModel> listMetaDataClasses = new HashSet<>();
    Gson gson = new Gson();

    private final List<String> unsupported = List.of(
            "Map",
            "TextView",
            "List",
            "Intent",
            "Bundle",
            "IntentFilter",
            "Button",
            "RecyclerView",
            "ProgressBar",
            "Toolbar",
            "ImageView",
            "Builder",
            "Handler",
            "ViewPager"
    );

    public Set<DependencyModel> constructGraph(String projectPath) throws IOException {


        List<String> listFiles = List.of(
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/InitializationActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/UpdateAppActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/VisuEntityDetailActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/utils/adapter/DetailFragmentPagerAdapter.java"
        );


        // todo add other types ??????
        // todo optimaze the code ??????

        ClassVisitors classVisitor = new ClassVisitors();
        listFiles.forEach(file -> {
            try {
                String content = FileHandler.read(file);
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(classVisitor);

                for (TypeDeclaration cls : classVisitor.getClasses()) {
                    DependencyModel dependencyModel = new DependencyModel();
                    Set<String> intentDep = new HashSet<>();
                    Set<String> methods = new HashSet<>();

                    String className = cls.getName().toString();
                    dependencyModel.setClassName(className);
                    if (isActivity(cls) && !cls.isInterface()) {
                        dependencyModel.setClassType(ClassType.ACTIVITY);
                        Type supperClass = cls.getSuperclassType();
                        // todo find another way to store this info
                        if (supperClass != null) {
                            String superClassName = supperClass.toString();
                        }
                        MethodVisitors methodVisitor = new MethodVisitors();
                        cls.accept(methodVisitor);
                        for (MethodDeclaration method : methodVisitor.getMethods()) {
                            methods.add(method.getName().toString());
                            method.accept(new ASTVisitor() {
                                public boolean visit(VariableDeclarationFragment fd) {
                                    if (fd.toString().contains("new Intent")) {
                                        if (fd.toString().contains(",") && (fd.toString().contains(".class"))) {
                                            String[] secondpart = fd.toString().split(",");
                                            String parenthesispart = new String();
                                            if (secondpart.length > 1) {
                                                parenthesispart = secondpart[1];
                                                if (parenthesispart.contains("class")) {
                                                    String[] className = parenthesispart.split("class");
                                                    if (className.length > 1) {
                                                        intentDep.add(className[0]);
                                                    }
                                                }
                                            }
                                        }
                                        return false;
                                    }
                                    return false;
                                }
                            });
                        }

                        MethodInvocationVisitors methodInvocationVisitors = new MethodInvocationVisitors();

                        cls.accept(methodInvocationVisitors);
                        Set<String> collect = methodInvocationVisitors.getMethods()
                                .stream()
                                .map(MethodInvocation::getExpression)
                                .filter(Objects::nonNull)
                                .map(Expression::resolveTypeBinding)
                                .filter(Objects::nonNull)
                                .map(ITypeBinding::getName)
                                .collect(Collectors.toSet());

                        VariableVisitors variableVisitors = new VariableVisitors();
                        cls.accept(variableVisitors);
                        Set<String> variableListTyped = variableVisitors.getFields()
                                .stream()
                                .filter(fragment -> fragment.getParent() instanceof FieldDeclaration)
                                .map(fragment -> (FieldDeclaration) fragment.getParent())
                                .filter(fieldDeclaration -> fieldDeclaration.getType().isParameterizedType())
                                .map(fieldDeclaration -> (ParameterizedType) fieldDeclaration.getType())
                                .map(parameterizedType -> parameterizedType.typeArguments().get(0).toString())
                                .collect(Collectors.toSet());

                        dependencyModel.setDependencies(intentDep);
                        dependencyModel.getDependencies().addAll(collect);
                        dependencyModel.getDependencies().addAll(variableListTyped);
                        Set<String> filtered = dependencyModel.getDependencies()
                                .stream()
                                .filter(s -> !unsupported.contains(s))
                                .filter(s -> !(s.contains("<") && s.contains(">")))
                                .collect(Collectors.toSet());
                        dependencyModel.setDependencies(filtered);
                        listMetaDataClasses.add(dependencyModel);
                    }

                    /**
                     *   Fragment Case
                     */
                    if (isFragment(cls) && !cls.isInterface()) {
                        dependencyModel.setClassType(ClassType.FRAGMENT);
                        MethodVisitors methodVisitor = new MethodVisitors();
                        cls.accept(methodVisitor);
                        MethodInvocationVisitors methodInvocationVisitors = new MethodInvocationVisitors();
                        cls.accept(methodInvocationVisitors);

                        int size = methodInvocationVisitors.getMethods().size();
                        System.out.println(size);

                        Set<String> collect = methodInvocationVisitors.getMethods()
                                .stream()
                                .map(MethodInvocation::getExpression)
                                .filter(Objects::nonNull)
                                .map(Expression::resolveTypeBinding)
                                .filter(Objects::nonNull)
                                .map(ITypeBinding::getName)
                                .collect(Collectors.toSet());


                        VariableVisitors variableVisitors = new VariableVisitors();
                        cls.accept(variableVisitors);
                        Set<String> variableListTyped = variableVisitors.getFields()
                                .stream()
                                .filter(fragment -> fragment.getParent() instanceof FieldDeclaration)
                                .map(fragment -> (FieldDeclaration) fragment.getParent())
                                .filter(fieldDeclaration -> fieldDeclaration.getType().isParameterizedType())
                                .map(fieldDeclaration -> (ParameterizedType) fieldDeclaration.getType())
                                .map(parameterizedType -> parameterizedType.typeArguments().get(0).toString())
                                .collect(Collectors.toSet());

                        dependencyModel.setDependencies(collect);
                        dependencyModel.getDependencies().addAll(variableListTyped);
                        Set<String> filtered = dependencyModel.getDependencies()
                                .stream()
                                .filter(s -> !unsupported.contains(s))
                                .filter(s -> !(s.contains("<") && s.contains(">")))
                                .collect(Collectors.toSet());
                        dependencyModel.setDependencies(filtered);
                        listMetaDataClasses.add(dependencyModel);
                    }

                    /**
                     * Adapter Case
                     */
                    if (!cls.isInterface()) {
                        dependencyModel.setClassType(ClassType.ADAPTER);
                        MethodVisitors methodVisitor = new MethodVisitors();
                        cls.accept(methodVisitor);
                        MethodInvocationVisitors methodInvocationVisitors = new MethodInvocationVisitors();
                        cls.accept(methodInvocationVisitors);

                        Set<String> collect = methodInvocationVisitors.getMethods()
                                .stream()
                                .map(MethodInvocation::getExpression)
                                .filter(Objects::nonNull)
                                .map(Expression::resolveTypeBinding)
                                .filter(Objects::nonNull)
                                .map(ITypeBinding::getName)
                                .collect(Collectors.toSet());
                        dependencyModel.setDependencies(collect);
                        listMetaDataClasses.add(dependencyModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        listMetaDataClasses.forEach(dependencyModel -> {
            try {
                Writer writer = new FileWriter("/home/tarfa/clusters/clusterA.json");
                Gson gson = new GsonBuilder().create();
                gson.toJson(listMetaDataClasses, writer);
                writer.flush();
                writer.close();//flush data to file   <---
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return listMetaDataClasses;
    }
}
