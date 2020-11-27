package ast.code.parser.javacodeparser.service;

import ast.code.parser.javacodeparser.models.DependencyModel;
import ast.code.parser.javacodeparser.service.commands.*;
import ast.code.parser.javacodeparser.typevisitors.ClassVisitors;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ast.code.parser.javacodeparser.models.ClassType.*;

@Service
public class ActivityParser {

    private Set<DependencyModel> listMetaDataClasses = new HashSet<>();

    public Set<DependencyModel> constructGraph(String projectPath) {
        List<String> listFiles = List.of(
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/utils/adapter/DetailFragmentPagerAdapter.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/InitializationActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/UpdateAppActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/VisuEntityDetailActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/utils/adapter/DetailFragmentPagerAdapter.java"
        );


        // cluster A ---> dependencies + view

        ClassVisitors classVisitor = new ClassVisitors();
        listFiles.forEach(file -> {
            try {
                String content = FileHandler.read(file);
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(classVisitor);
                for (TypeDeclaration cls : classVisitor.getClasses()) {

                    if (isActivity(cls) && !cls.isInterface()) {
                        DependencyModel dependencyModel = new IsActivity(cls).apply();
                        listMetaDataClasses.add(dependencyModel);
                    }

                    if (isFragment(cls) && !cls.isInterface()) {
                        DependencyModel dependencyModel = new IsFragment(cls).apply();
                        listMetaDataClasses.add(dependencyModel);
                    }

                    if (isService(cls) && !cls.isInterface()) {
                        DependencyModel dependencyModel = new IsService(cls).apply();
                        listMetaDataClasses.add(dependencyModel);
                    }

                    if (isAsyncTask(cls) && !cls.isInterface()) {
                        DependencyModel dependencyModel = new IsAsyncTask(cls).apply();
                        listMetaDataClasses.add(dependencyModel);
                    }

                    if (isAdapter(cls) && !cls.isInterface()) {
                        DependencyModel dependencyModel = new IsAdapter(cls).apply();
                        listMetaDataClasses.add(dependencyModel);
                    }

                    if (isClassJava(cls) && !cls.isInterface()) {
                        // this mean it's simple class
                        DependencyModel dependencyModel = new IsClass(cls).apply();
                        listMetaDataClasses.add(dependencyModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return listMetaDataClasses;
    }
}
