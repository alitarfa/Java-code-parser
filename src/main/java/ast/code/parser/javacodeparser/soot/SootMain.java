package ast.code.parser.javacodeparser.soot;

import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.jimple.JimpleBody;
import soot.options.Options;

import java.io.File;

public class SootMain {

    public static String sourceDirectory = System.getProperty("user.dir") + File.separator + "src/main/java/ast/code/parser/javacodeparser/soot";
    public static String clsName = "TestClass";
    public static String methodName = "printMe";

    public static void setupSoot() {
        G.reset();
        Options.v().set_prepend_classpath(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_soot_classpath(sourceDirectory);
        SootClass sc = Scene.v().loadClassAndSupport(clsName);
        sc.setApplicationClass();
        Scene.v().loadNecessaryClasses();
    }

    public static void main(String[] args) {
        setupSoot();
        var mainClass = Scene.v().getSootClass(clsName);
        var sm = mainClass.getMethodByName(methodName);
        var body = (JimpleBody) sm.retrieveActiveBody();
    }
}
