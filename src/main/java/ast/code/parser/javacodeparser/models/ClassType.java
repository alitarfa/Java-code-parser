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
        if (type.getSuperclassType() != null) {
            String superClass = type.getSuperclassType().toString();
            String className = type.getName().toString();
            return superClass.contains("Activity") || className.contains("Activity");
        } else return false;
    }


    public static boolean isService(TypeDeclaration type) {
        if (type.getSuperclassType() != null) {
            String superClass = type.getSuperclassType().toString();
            String className = type.getName().toString();
            return superClass.contains("Service") || className.contains("Service");
        } else return false;
    }

    public static boolean isFragment(TypeDeclaration type) {
        if (type.getSuperclassType() != null) {
            String superClass = type.getSuperclassType().toString();
            String className = type.getName().toString();
            return superClass.contains("Fragment") || className.contains("Fragment");
        } else return false;
    }

    public static boolean isBroadcastReceiver(TypeDeclaration type) {
        if (type.getSuperclassType() != null) {
            String superClass = type.getSuperclassType().toString();
            String className = type.getName().toString();
            return superClass.contains("Receiver") || className.contains("Receiver");
        } else return false;
    }

    public static boolean isAdapter(TypeDeclaration type) {
        if (type.getSuperclassType() != null) {
            String superClass = type.getSuperclassType().toString();
            String className = type.getName().toString();
            return superClass.contains("Adapter") || className.contains("Adapter");
        } else return false;
    }

    public static boolean isAsyncTask(TypeDeclaration type) {
        if (type.getSuperclassType() != null) {
            String superClass = type.getSuperclassType().toString();
            String className = type.getName().toString();
            return superClass.contains("Async") || className.contains("Async");
        } else return false;
    }

    public static boolean isClassJava(TypeDeclaration type) {
        return !isActivity(type) && !isAdapter(type) && !isAsyncTask(type) && !isBroadcastReceiver(type)
                && !isFragment(type) && !isService(type);
    }
}

