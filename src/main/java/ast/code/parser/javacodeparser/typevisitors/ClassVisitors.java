package ast.code.parser.javacodeparser.typevisitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.HashSet;
import java.util.Set;

public class ClassVisitors extends ASTVisitor {

    private Set<TypeDeclaration> classes = new HashSet<>();

    @Override
    public boolean visit(TypeDeclaration node) {
        classes.add(node);
        return super.visit(node);
    }

    public Set<TypeDeclaration> getClasses() {
        return classes;
    }
}
