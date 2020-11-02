package ast.code.parser.javacodeparser.typevisitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.HashSet;
import java.util.Set;

public class VariableVisitors extends ASTVisitor {

    private Set<VariableDeclarationFragment> fields = new HashSet<>();

    @Override
    public boolean visit(VariableDeclarationFragment node) {
        fields.add(node);
        return super.visit(node);
    }

    public Set<VariableDeclarationFragment> getFields() {
        return fields;
    }
}
