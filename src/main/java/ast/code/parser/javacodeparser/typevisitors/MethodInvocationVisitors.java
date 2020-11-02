package ast.code.parser.javacodeparser.typevisitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.HashSet;
import java.util.Set;

public class MethodInvocationVisitors extends ASTVisitor {

    private Set<MethodInvocation> methods = new HashSet<>();

    @Override
    public boolean visit(MethodInvocation node) {
        methods.add(node);
        return true;
    }

    public Set<MethodInvocation> getMethods() {
        return methods;
    }
}
