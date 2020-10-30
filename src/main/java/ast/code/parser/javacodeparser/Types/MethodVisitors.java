package ast.code.parser.javacodeparser.Types;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.HashSet;
import java.util.Set;

public class MethodVisitors extends ASTVisitor {

    private Set<MethodDeclaration> methods = new HashSet<>();

    @Override
    public boolean visit(MethodDeclaration node) {
        methods.add(node);
        return super.visit(node);
    }

    public Set<MethodDeclaration> getMethods() {
        return methods;
    }
}
