package ast.code.parser.javacodeparser;


import ast.code.parser.javacodeparser.service.FileHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class ASTMain7 {

    public static void main(String[] args) throws Exception {

        String projectPath = "/home/tarfa/AndroidStudioProjects/MicroAppClusterA/app/src/main/java/com/carl/touch/";
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
                        .collect(Collectors.toList());
                Files.write(file.toPath(), collect);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
