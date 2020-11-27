package ast.code.parser.javacodeparser.models;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Information {
    private Set<String> dependencies = new HashSet<>();
    private Set<String> activityViews = new HashSet<>();
    private Set<String> fragmentView = new HashSet<>();
}
