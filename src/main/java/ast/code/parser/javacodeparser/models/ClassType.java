package ast.code.parser.javacodeparser.models;

import org.eclipse.jdt.core.dom.TypeDeclaration;

public enum ClassType {
    CLASS,
    INTERFACE,
    ENUM,
    ACTIVITY,
    FRAGMENT,
    ASYNC_TASK,
    SUPPER_CLASS,
    SERVICE,
    ADAPTER;

    public static boolean isActivity(TypeDeclaration type) {
        String superClass = type.getSuperclassType().toString();
        String className = type.getName().toString();
        return superClass.contains("Activity") || className.contains("Activity");
    }


    public static boolean isService(TypeDeclaration type) {
        String superClass = type.getSuperclassType().toString();
        String className = type.getName().toString();
        return superClass.contains("Service") || className.contains("Service");
    }

    public static boolean isFragment(TypeDeclaration type) {
        String superClass = type.getSuperclassType().toString();
        String className = type.getName().toString();
        return superClass.contains("Fragment") || className.contains("Fragment");
    }

    public static boolean isBroadcastReceiver(TypeDeclaration type) {
        String superClass = type.getSuperclassType().toString();
        String className = type.getName().toString();
        return superClass.contains("Receiver") || className.contains("Receiver");
    }

    public static boolean isAdapter(TypeDeclaration type) {
        String superClass = type.getSuperclassType().toString();
        String className = type.getName().toString();
        return superClass.contains("Adapter") || className.contains("Adapter");
    }

    public static boolean isAsyncTask(TypeDeclaration type) {
        String superClass = type.getSuperclassType().toString();
        String className = type.getName().toString();
        return superClass.contains("Async") || className.contains("Async");
    }
}

