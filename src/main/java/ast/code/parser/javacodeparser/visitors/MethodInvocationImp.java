package ast.code.parser.javacodeparser.visitors;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class MethodInvocationImp extends VoidVisitorAdapter<List<MethodCallExpr>> {

    @Override
    public void visit(MethodCallExpr method, List<MethodCallExpr> collection) {
        collection.add(method);
        super.visit(method, collection);
    }
}
