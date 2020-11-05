package ast.code.parser.javacodeparser.visitors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class TypeDeclarationImp extends VoidVisitorAdapter<List<ClassOrInterfaceDeclaration>> {

    @Override
    public void visit(ClassOrInterfaceDeclaration n, List<ClassOrInterfaceDeclaration> arg) {
        super.visit(n, arg);
        arg.add(n);
    }
}
