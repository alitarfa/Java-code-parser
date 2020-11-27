package ast.code.parser.javacodeparser.service;

import ast.code.parser.javacodeparser.models.DependencyModel;
import ast.code.parser.javacodeparser.models.Information;
import ast.code.parser.javacodeparser.service.commands.*;
import ast.code.parser.javacodeparser.typevisitors.ClassVisitors;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ast.code.parser.javacodeparser.models.ClassType.*;

public class ProjectParser {

    Information information = new Information();

    /**
     * Helps to Parse the Project depends on the Class Type @Like{Activity, Fragment, Service, ...etc}
     *
     * @param listClasses List of classes of given Cluster to parse
     * @return Information object hold list of dependencies and list of views
     */
    public Information parse(Set<String> listClasses) {
        ClassVisitors classVisitor = new ClassVisitors();
        listClasses.forEach(file -> {
            try {
                String content = FileHandler.read(file);
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(classVisitor);
                for (TypeDeclaration cls : classVisitor.getClasses()) {

                    if (isActivity(cls) && !cls.isInterface()) {
                        DependencyModel dependencyModel = new IsActivity(cls).apply();
                        information.getDependencies().addAll(dependencyModel.getDependencies());
                        information.getActivityViews().addAll(dependencyModel.getActivityViews());
                    }

                    if (isFragment(cls) && !cls.isInterface()) {
                        DependencyModel dependencyModel = new IsFragment(cls).apply();
                        information.getDependencies().addAll(dependencyModel.getDependencies());
                        information.getFragmentView().addAll(dependencyModel.getFragmentViews());
                    }

                    if (isService(cls) && !cls.isInterface()) {
                        DependencyModel dependencyModel = new IsService(cls).apply();
                        information.getDependencies().addAll(dependencyModel.getDependencies());
                    }

                    if (isAsyncTask(cls) && !cls.isInterface()) {
                        DependencyModel dependencyModel = new IsAsyncTask(cls).apply();
                        information.getDependencies().addAll(dependencyModel.getDependencies());
                    }

                    if (isAdapter(cls) && !cls.isInterface()) {
                        DependencyModel dependencyModel = new IsAdapter(cls).apply();
                        information.getDependencies().addAll(dependencyModel.getDependencies());
                    }

                    if (isClassJava(cls) && !cls.isInterface()) {
                        DependencyModel dependencyModel = new IsClass(cls).apply();
                        information.getDependencies().addAll(dependencyModel.getDependencies());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return information;
    }

    /**
     * anther method to parse the java code without using the JDT API just Stream file
     * to get the dependencies of each class, it's very useful than other methods
     *
     * @param file path of file to be parsed
     * @return List of all its dependencies
     * @throws IOException
     */
    public Set<String> parse_v2(String file) throws IOException {
        Stream<String> lines = Files.lines(Paths.get(file));
        return lines
                .filter(s -> s.contains("import"))
                .map(s -> s.split("\\."))
                .map(strings -> strings[strings.length - 1])
                .map(s -> s.split(";"))
                .map(strings -> strings[0])
                .collect(Collectors.toSet());
    }

    /**
     * Copy the file to a specific directory that hold the same name of the original one
     *
     * @param sources     list of files to be copied
     * @param destination path of where we gonna copy the original file
     * @throws IOException
     */
    public void copyTo(List<String> sources, String destination) {
        sources.forEach(source -> {
            try {
                Path sourcePath = new File(source).toPath();
                String destinationName = source.split("/")[source.split("/").length - 1];
                Path destinationPath = new File(destination + "/" + destinationName).toPath();
                if (!destinationPath.toFile().exists()) {
                    Files.copy(sourcePath, destinationPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
