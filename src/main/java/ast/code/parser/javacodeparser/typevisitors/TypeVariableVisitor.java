package ast.code.parser.javacodeparser.typevisitors;

import lombok.Getter;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
public class TypeVariableVisitor extends ASTVisitor {
    private List<VariableDeclarationStatement> variables = new ArrayList<>();

    @Override
    public boolean visit(VariableDeclarationStatement node) {
        for (Iterator iter = node.fragments().iterator(); iter.hasNext();) {
            System.out.println("------------------");
            VariableDeclarationFragment fragment = (VariableDeclarationFragment) iter.next();
            IVariableBinding binding = fragment.resolveBinding();
            System.out.println("binding variable declaration: " +binding.getVariableDeclaration());
            System.out.println("binding: " +binding);
        }
        return super.visit(node);
    }

}
