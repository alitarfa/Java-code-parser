package ast.code.parser.javacodeparser.service;

import ast.code.parser.javacodeparser.FileHandler;
import ast.code.parser.javacodeparser.ParserFactory;
import ast.code.parser.javacodeparser.models.ClassType;
import ast.code.parser.javacodeparser.models.DependencyModel;
import ast.code.parser.javacodeparser.typevisitors.ClassVisitors;
import ast.code.parser.javacodeparser.typevisitors.MethodInvocationVisitors;
import ast.code.parser.javacodeparser.typevisitors.MethodVisitors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jdt.core.dom.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AdapterParser {
    private Set<DependencyModel> listMetaDataClasses = new HashSet<>();
    Gson gson = new Gson();

    public Set<DependencyModel> constructGraph(String projectPath) throws IOException {
        //List<File> projectFiles = FileHandler.readJavaFiles(new File(projectPath));
        ClassVisitors classVisitor = new ClassVisitors();
        // projectFiles.forEach(file -> {
        try {
            //String content = FileHandler.read(file.getAbsolutePath());
            //String content = FileHandler.read("/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/InitializationActivity.java");
            //String content = FileHandler.read("/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/UpdateAppActivity.java");
            //String content = FileHandler.read("/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/VisuEntityDetailActivity.java");
            //String content = FileHandler.read("/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/AbstractEntityDetailFragment.java");
            String content = FileHandler.read("/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/utils/adapter/DetailFragmentPagerAdapter.java");
            CompilationUnit result = ParserFactory.getInstance(content);
            result.accept(classVisitor);
            /**
             * Iterating through the classes existing inside a given class
             */
            for (TypeDeclaration cls : classVisitor.getClasses()) {
                DependencyModel dependencyModel = new DependencyModel();
                Set<String> methods = new HashSet<>();
                /***
                 * Get the information from the class {className, Methods, Interfaces, Enum, ...etc}
                 */
                String className = cls.getName().toString();
                dependencyModel.setClassName(className);



            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // });
        Writer writer = new FileWriter("/home/tarfa/DetailFragmentPagerAdapter.json");
        Gson gson = new GsonBuilder().create();
        gson.toJson(listMetaDataClasses, writer);
        writer.flush(); //flush data to file   <---
        writer.close();
        return listMetaDataClasses;
    }
}
