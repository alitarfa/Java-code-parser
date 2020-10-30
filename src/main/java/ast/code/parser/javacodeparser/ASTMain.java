package ast.code.parser.javacodeparser;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.io.File;
import java.util.List;


public class ASTMain {

    public static void main(String args[]) throws Exception {

        List<File> projectFiles = FileHandler.readJavaFiles(new File("/home/tarfa/MySpace/Tekit"));
        long packages = ClassStatistics.packageCounter(projectFiles);
        long classes = ClassStatistics.classCounter(projectFiles);
        long methods = ClassStatistics.methodCounter(projectFiles);
        long attributes = ClassStatistics.attributeCounter(projectFiles);
        Long lines = ClassStatistics.lineCounter(projectFiles);
        System.out.println("Package Count : " + packages);
        System.out.println("Class Count : " + classes);
        System.out.println("Methods Count : " + methods);
        System.out.println("Attribute Count : " + attributes);
        System.out.println("Line Count : " + lines);

        ClassStatistics.highestNumberMethod(projectFiles)
                .stream()
                .map(TypeDeclaration::getName)
                .forEach(System.out::println);

        ClassStatistics.highestNumberAttribute(projectFiles)
                .stream()
                .map(TypeDeclaration::getName)
                .forEach(System.out::println);

        ClassStatistics.findClassesHaveMoreThan(projectFiles, 5)
                .stream()
                .map(TypeDeclaration::getName)
                .forEach(System.out::println);

        ClassStatistics.highestNumberOfLineInMethod(projectFiles)
                .stream()
                .map(MethodDeclaration::getName)
                .forEach(System.out::println);

        int i = ClassStatistics.numberMaxOfParams(projectFiles);
        System.out.println(i);
    }

}
