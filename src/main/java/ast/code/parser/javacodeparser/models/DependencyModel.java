package ast.code.parser.javacodeparser.models;

import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DependencyModel {
    private String className;
    private ClassType classType;
    private Set<String> dependencies;
    private Set<String> methods;
}
