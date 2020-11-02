package ast.code.parser.javacodeparser.typevisitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import java.util.HashSet;
import java.util.Set;

public class PackageVisitors extends ASTVisitor {

    private Set<PackageDeclaration> packages = new HashSet<>();

    @Override
    public boolean visit(PackageDeclaration node) {
        packages.add(node);
        return super.visit(node);
    }

    public Set<PackageDeclaration> getPackages() {
        return packages;
    }
}
