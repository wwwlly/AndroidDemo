package com.kemp.compiler;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.lang.model.element.Modifier;

/**
 * Created by wangkp on 2018/1/25.
 */

public class Test {
    public static void main(String[] args) {
        test2();
    }

    private static void test1() {
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello World")
                .build();
        MethodSpec test = MethodSpec.methodBuilder("test")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(String.class)
                .addStatement("return $S", "Hello World")
                .build();
        TypeSpec typeSpec = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addMethod(main)
                .addMethod(test)
                .build();
        JavaFile javaFile = JavaFile.builder("com.kemp.compiler", typeSpec).build();
        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void test2() {

        ClassName mOptClz = ClassName.get("com.alibaba.ariver.kernel.api.extension", "ExtensionOpt");
        ClassName mOptInnerClz = ClassName.get("com.alibaba.ariver.kernel.api.extension", "ExtensionOpt.MethodInvokeOptimizer");

        MethodSpec.Builder mOpt1Method = MethodSpec.methodBuilder("opt1")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addJavadoc("Opt1: for normal extension optimize");
        MethodSpec.Builder mOpt2Method = MethodSpec.methodBuilder("opt2")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addJavadoc("Opt2: for bridge extension optimize");
        MethodSpec.Builder mOpt3Method = MethodSpec.methodBuilder("opt3")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addJavadoc("Opt3: for proxy optimize");

        try {
            MethodSpec.Builder optMethod = MethodSpec.methodBuilder("doMethodInvoke")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(String.class, "methodName")
                    .addParameter(ArrayTypeName.of(Object.class), "args")
                    .addException(Throwable.class)
                    .returns(Object.class);

            optMethod.addStatement("return null");
            String methodName = "setupMethodInvokeOptimizerForBridge";
            mOpt1Method.addStatement("$T." + methodName + "($T.class, $L)", mOptClz, Test.class, TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(mOptInnerClz)
                    .addMethod(optMethod.build())
                    .build());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            TypeSpec clazzName = TypeSpec.classBuilder("TestOpt")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addJavadoc("Auto generated on " + dateFormat.format(new Date()))
                    .addMethod(mOpt1Method.build())
                    .addMethod(mOpt2Method.build())
                    .addMethod(mOpt3Method.build())
                    .build();

            JavaFile.builder("com.kemp.compiler.apt", clazzName)
                    .build()
                    .writeTo(System.out);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
