package ast.code.parser.javacodeparser;

import ast.code.parser.javacodeparser.Types.ClassVisitors;
import ast.code.parser.javacodeparser.Types.MethodVisitors;
import ast.code.parser.javacodeparser.Types.PackageVisitors;
import ast.code.parser.javacodeparser.Types.VariableVisitors;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ClassStatistics {

    public static long classCounter(List<File> projectFiles) {
        ClassVisitors classVisitors = new ClassVisitors();
        projectFiles.forEach(file -> {
            try {
                String content = FileHandler.read(file.getAbsolutePath());
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(classVisitors);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return classVisitors
                .getClasses()
                .stream()
                .filter(typeDeclaration -> !typeDeclaration.isInterface())
                .count();
    }

    public static long methodCounter(List<File> projectFiles) {
        MethodVisitors methodVisitors = new MethodVisitors();
        projectFiles.forEach(file -> {
            try {
                String content = FileHandler.read(file.getAbsolutePath());
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(methodVisitors);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return methodVisitors.getMethods()
                .size();
    }

    public static long attributeCounter(List<File> projectFiles) {
        VariableVisitors variableVisitors = new VariableVisitors();
        projectFiles.forEach(file -> {
            try {
                String content = FileHandler.read(file.getAbsolutePath());
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(variableVisitors);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return variableVisitors.getFields()
                .size();
    }

    public static long packageCounter(List<File> projectFiles) {
        PackageVisitors packageVisitors = new PackageVisitors();
        projectFiles.forEach(file -> {
            try {
                String content = FileHandler.read(file.getAbsolutePath());
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(packageVisitors);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return packageVisitors.getPackages()
                .size();
    }

    public static Long lineCounter(List<File> projectFiles) {
        AtomicLong numberLines = new AtomicLong();
        projectFiles.forEach(file -> {
            try {
                String content = FileHandler.read(file.getAbsolutePath());
                numberLines.addAndGet(content.lines().count());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return numberLines.get();
    }

    public static double meanNumberMethodsPerClass(List<File> projectFiles) {
        return (double) methodCounter(projectFiles) / (double) classCounter(projectFiles);
    }

    public static double meanNumberLinePerMethod(List<File> projectFiles) {
        return (double) lineCounter(projectFiles) / (double) methodCounter(projectFiles);
    }

    public static double meanNumberAttributePerClass(List<File> projectFiles) {
        return (double) attributeCounter(projectFiles) / (double) classCounter(projectFiles);
    }

    public static List<TypeDeclaration> highestNumberMethod(List<File> projectFiles) {
        ClassVisitors classVisitors = new ClassVisitors();
        projectFiles.forEach(file -> {
            try {
                String content = FileHandler.read(file.getAbsolutePath());
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(classVisitors);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return classVisitors
                .getClasses()
                .stream()
                .filter(typeDeclaration -> !typeDeclaration.isInterface())
                .sorted(Comparator.comparingInt(o -> o.getMethods().length))
                .limit((long) Math.ceil(10 * classVisitors.getClasses().size()))
                .collect(Collectors.toList());
    }

    public static List<TypeDeclaration> highestNumberAttribute(List<File> projectFiles) {
        ClassVisitors classVisitors = new ClassVisitors();
        projectFiles.forEach(file -> {
            try {
                String content = FileHandler.read(file.getAbsolutePath());
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(classVisitors);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return classVisitors
                .getClasses()
                .stream()
                .filter(typeDeclaration -> !typeDeclaration.isInterface())
                .sorted(Comparator.comparingInt(o -> o.getFields().length))
                .limit((long) Math.ceil(10 * classVisitors.getClasses().size()))
                .collect(Collectors.toList());
    }


    public static List<TypeDeclaration> findClassesHaveMoreThan(List<File> projectFiles, int value) {

        ClassVisitors classVisitors = new ClassVisitors();
        projectFiles.forEach(file -> {
            try {
                String content = FileHandler.read(file.getAbsolutePath());
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(classVisitors);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return classVisitors
                .getClasses()
                .stream()
                .filter(typeDeclaration -> !typeDeclaration.isInterface())
                .filter(typeDeclaration -> typeDeclaration.getMethods().length > value)
                .collect(Collectors.toList());
    }

    public static List<MethodDeclaration> highestNumberOfLineInMethod(List<File> projectFiles) {
        ClassVisitors classVisitors = new ClassVisitors();
        projectFiles.forEach(file -> {
            try {
                String content = FileHandler.read(file.getAbsolutePath());
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(classVisitors);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return classVisitors
                .getClasses()
                .stream()
                .filter(typeDeclaration -> !typeDeclaration.isInterface())
                .map(typeDeclaration -> {
                    MethodVisitors methodVisitors = new MethodVisitors();
                    typeDeclaration.accept(methodVisitors);
                    return methodVisitors
                            .getMethods()
                            .stream()
                            .filter(methodDeclaration -> methodDeclaration.getBody() != null)
                            .sorted(Comparator.comparingInt(o -> o.getBody().statements().size()))
                            .limit((long) Math.ceil(10 * methodVisitors.getMethods().size()))
                            .collect(Collectors.toList());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }


    public static int numberMaxOfParams(List<File> projectFiles) {
        ClassVisitors classVisitors = new ClassVisitors();
        projectFiles.forEach(file -> {
            try {
                String content = FileHandler.read(file.getAbsolutePath());
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(classVisitors);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        MethodVisitors methodVisitors = new MethodVisitors();
        classVisitors
                .getClasses()
                .stream()
                .filter(typeDeclaration -> !typeDeclaration.isInterface())
                .forEach(typeDeclaration -> typeDeclaration.accept(methodVisitors));

        return methodVisitors.getMethods()
                .stream()
                .map(MethodDeclaration::parameters)
                .max(Comparator.comparingInt(List::size))
                .get().size();
    }
}
