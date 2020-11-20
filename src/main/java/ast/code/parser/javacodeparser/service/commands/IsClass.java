package ast.code.parser.javacodeparser.service.commands;

import ast.code.parser.javacodeparser.models.DependencyModel;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class IsClass implements Command<DependencyModel> {
    private final TypeDeclaration typeDeclaration;
    private final DependencyModel dependencyModel = new DependencyModel();

    public IsClass(TypeDeclaration typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

    @Override
    public DependencyModel apply() {
        return null;
    }
}
