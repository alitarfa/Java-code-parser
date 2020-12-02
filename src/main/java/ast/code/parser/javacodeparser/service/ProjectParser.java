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
    public void copyToLayout(Set<String> sources, String destination) {
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

    public void copyTo(Set<String> sources, String destination) {
        sources.forEach(source -> {
            try {
                Path sourcePath = new File(source).toPath();
                String destinationName = source.split("/")[source.split("/").length - 1];
                String newPath = source.replace("/home/tarfa/Phd/carl-mob-app/android/src/main/java", destination);
                Path destinationPath = new File(newPath).toPath();
                if (!destinationPath.toFile().exists()) {
                    Files.copy(sourcePath, destinationPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void packageReplacement(String projectPath) {
        List<File> files = FileHandler.readJavaFiles(new File(projectPath));
        files.forEach(file -> {
            List<String> collect = null;
            try {
                collect = Files.lines(file.toPath())
                        .map(s -> s.replace("import com.carl.touch.dto", "import com.carl.touch.android.moduleex.dto"))
                        .map(s -> s.replace("import com.carl.touch.android.R", "import com.carl.touch.R"))
                        .map(s -> s.replace("import android.support.v7.app.AppCompatActivity", "import androidx.appcompat.app.AppCompatActivity"))
                        .map(s -> s.replace("import android.support.annotation.NonNull;", "import androidx.annotation.NonNull;"))
                        .map(s -> s.replace("import android.support.annotation.CallSuper;", "import androidx.annotation.CallSuper;"))
                        .map(s -> s.replace("import android.support.v4.app.Fragment;", "import androidx.fragment.app.Fragment;"))
                        .map(s -> s.replace("import android.support.v4.app.FragmentManager;", "import androidx.fragment.app.FragmentManager;"))
                        .map(s -> s.replace("import android.support.v7.app.ActionBar;", "import androidx.appcompat.app.ActionBar;"))
                        .map(s -> s.replace("import android.support.annotation.Nullable;", "import androidx.annotation.Nullable;"))
                        .map(s -> s.replace("import android.support.v4.app.FragmentTransaction;", "import androidx.fragment.app.FragmentTransaction;"))
                        .map(s -> s.replace("import android.support.annotation.VisibleForTesting;", "import androidx.annotation.VisibleForTesting;"))
                        .map(s -> s.replace("import android.support.v4.app.DialogFragment;", ""))
                        .map(s -> s.replace("import android.support.v4.view.GravityCompat;", "import androidx.core.view.GravityCompat;"))
                        .map(s -> s.replace("import android.support.v4.widget.DrawerLayout;", "import androidx.drawerlayout.widget.DrawerLayout;"))
                        .map(s -> s.replace("import com.carl.touch.dao", "import com.carl.touch.android.moduleex.dao"))
                        .map(s -> s.replace("import android.support.v4.content.LocalBroadcastManager;", "import androidx.localbroadcastmanager.content.LocalBroadcastManager;"))
                        .collect(Collectors.toList());
                Files.write(file.toPath(), collect);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
