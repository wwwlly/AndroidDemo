package com.kemp.compiler;

import javax.lang.model.type.TypeMirror;

public class TypeMirrorUtils {

    public static String getDefaultValue(TypeMirror mirror) {
        switch (mirror.getKind()) {
            case BOOLEAN:
                return "false";
            case BYTE:
            case SHORT:
            case INT:
            case LONG:
            case CHAR:
            case FLOAT:
            case DOUBLE:
                return "0";
            default:
                return "null";
        }
    }

}
