package ast.code.parser.javacodeparser.service;

import java.io.File;
import java.util.List;

public class PathResolver {

    public static String getPath(List<File> projectFiles, String target) {
        // TODO: 25/11/2020 in some cases we have two classes have same name with with different location
        return projectFiles.stream()
                .map(File::getAbsoluteFile)
                .map(File::toString)
                .filter(file -> file.contains(target))
                .findFirst()
                .orElse(null);
    }
}
