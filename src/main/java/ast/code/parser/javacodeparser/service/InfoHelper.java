package ast.code.parser.javacodeparser.service;


import ast.code.parser.javacodeparser.typevisitors.ClassVisitors;
import ast.code.parser.javacodeparser.typevisitors.MethodInvocationVisitors;
import ast.code.parser.javacodeparser.typevisitors.MethodVisitors;
import ast.code.parser.javacodeparser.typevisitors.VariableVisitors;
import org.eclipse.jdt.core.dom.*;

public class InfoHelper {

    public static void show(ClassVisitors classVisitors) {
        classVisitors.getClasses().forEach(InfoHelper::showClassInfo);
        classVisitors.getClasses().forEach(InfoHelper::showMethodInfo);
        classVisitors.getClasses().forEach(InfoHelper::showVariableInfo);
    }

    private static void showClassInfo(TypeDeclaration typeDeclaration) {
        System.out.println("******* Class *******");
        System.out.println("Class " + typeDeclaration.getName());
        System.out.println("Parent Class" + typeDeclaration.getSuperclassType());
    }

    private static void showMethodInfo(TypeDeclaration typeDeclaration) {
        System.out.println("******* Methods *******");
        MethodVisitors methodVisitors = new MethodVisitors();
        typeDeclaration.accept(methodVisitors);
        methodVisitors
                .getMethods()
                .forEach(methodDeclaration -> {
                    MethodInvocationVisitors methodInvocationVisitors = new MethodInvocationVisitors();
                    Type returnType = methodDeclaration.getReturnType2();
                    SimpleName name = methodDeclaration.getName();
                    System.out.println(returnType + " " + name);
                    methodDeclaration.accept(methodInvocationVisitors);
                    methodInvocationVisitors
                            .getMethods()
                            .stream().map(MethodInvocation::getName)
                            .map(simpleName -> name + " method invoke ---> " + simpleName + " method")
                            .forEach(System.out::println);
                });
    }

    private static void showVariableInfo(TypeDeclaration typeDeclaration) {
        System.out.println("******* Variables *******");
        VariableVisitors variableVisitors = new VariableVisitors();
        typeDeclaration.accept(variableVisitors);
        variableVisitors
                .getFields()
                .stream()
                .map(VariableDeclaration::getName)
                .forEach(System.out::println);
    }
}
