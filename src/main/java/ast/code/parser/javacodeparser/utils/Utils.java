package ast.code.parser.javacodeparser.utils;

import java.util.List;

public class Utils {

    public static final List<String> unsupported = List.of(
            "Map",
            "TextView",
            "List",
            "Intent",
            "Bundle",
            "IntentFilter",
            "Button",
            "RecyclerView",
            "ProgressBar",
            "Toolbar",
            "ImageView",
            "Builder",
            "Handler",
            "ViewPager"
    );
}
