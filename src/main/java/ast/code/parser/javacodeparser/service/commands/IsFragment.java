package ast.code.parser.javacodeparser.service.commands;

import ast.code.parser.javacodeparser.models.ClassType;
import ast.code.parser.javacodeparser.models.DependencyModel;
import ast.code.parser.javacodeparser.typevisitors.MethodInvocationVisitors;
import ast.code.parser.javacodeparser.typevisitors.MethodVisitors;
import ast.code.parser.javacodeparser.typevisitors.VariableVisitors;
import ast.code.parser.javacodeparser.utils.Utils;
import org.eclipse.jdt.core.dom.*;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class IsFragment implements Command<DependencyModel> {

    private final TypeDeclaration typeDeclaration;
    private final DependencyModel dependencyModel = new DependencyModel();

    public IsFragment(TypeDeclaration typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

    @Override
    public DependencyModel apply() {
        dependencyModel.setClassName(typeDeclaration.getName().toString());
        dependencyModel.setClassType(ClassType.FRAGMENT);
        MethodVisitors methodVisitor = new MethodVisitors();
        typeDeclaration.accept(methodVisitor);
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

        dependencyModel.setDependencies(collect);
        dependencyModel.getDependencies().addAll(variableListTyped);
        Set<String> filtered = dependencyModel.getDependencies()
                .stream()
                .filter(s -> !Utils.unsupported.contains(s))
                .filter(s -> !(s.contains("<") && s.contains(">")))
                .collect(Collectors.toSet());
        dependencyModel.setDependencies(filtered);
        return dependencyModel;
    }
}
