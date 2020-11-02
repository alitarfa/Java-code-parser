package ast.code.parser.javacodeparser.models;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GraphModel {
    private String key;
    private String value;
}
