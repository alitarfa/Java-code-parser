package ast.code.parser.javacodeparser.service.commands;

import ast.code.parser.javacodeparser.models.ClassType;
import ast.code.parser.javacodeparser.models.DependencyModel;
import ast.code.parser.javacodeparser.service.ViewResolver;
import ast.code.parser.javacodeparser.typevisitors.MethodInvocationVisitors;
import ast.code.parser.javacodeparser.typevisitors.MethodVisitors;
import ast.code.parser.javacodeparser.typevisitors.VariableVisitors;
import org.eclipse.jdt.core.dom.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ast.code.parser.javacodeparser.utils.Utils.unsupported;

public class IsActivity implements Command<DependencyModel> {

    private final TypeDeclaration typeDeclaration;
    private final DependencyModel dependencyModel = new DependencyModel();
    Set<String> intentDep = new HashSet<>();

    public IsActivity(TypeDeclaration typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

    @Override
    public DependencyModel apply() {
        dependencyModel.setClassName(typeDeclaration.getName().toString());
        Set<String> activityView = ViewResolver.findActivityView(typeDeclaration);
        dependencyModel.setViews(activityView);
        dependencyModel.setClassType(ClassType.ACTIVITY);
        Type supperClass = typeDeclaration.getSuperclassType();
        // todo find another way to store this info
        if (supperClass != null) {
            String superClassName = supperClass.toString();
        }
        MethodVisitors methodVisitor = new MethodVisitors();
        typeDeclaration.accept(methodVisitor);

        // TODO: 20/11/2020 refactoring this code to functional style
        for (MethodDeclaration method : methodVisitor.getMethods()) {
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

        typeDeclaration.accept(methodInvocationVisitors);
        Set<String> collect = methodInvocationVisitors.getMethods()
                .stream()
                .map(MethodInvocation::getExpression)
                .filter(Objects::nonNull)
                .map(Expression::resolveTypeBinding)
                .filter(Objects::nonNull)
                .map(ITypeBinding::getName)
                .collect(Collectors.toSet());

        Set<String> packages = methodInvocationVisitors.getMethods()
                .stream()
                .map(MethodInvocation::getExpression)
                .filter(Objects::nonNull)
                .map(Expression::resolveTypeBinding)
                .filter(Objects::nonNull)
                .map(iTypeBinding -> {
                    IPackageBinding aPackage = iTypeBinding.getPackage();
                    return aPackage.getName();
                })
                .collect(Collectors.toSet());

        VariableVisitors variableVisitors = new VariableVisitors();
        typeDeclaration.accept(variableVisitors);
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
        return dependencyModel;
    }
}
