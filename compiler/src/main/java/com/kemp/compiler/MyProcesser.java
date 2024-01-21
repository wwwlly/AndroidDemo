package com.kemp.compiler;

import com.google.auto.service.AutoService;
import com.kemp.annotations.CustomAnnotation;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes("com.kemp.annotations.CustomAnnotation")
@AutoService(Processor.class)
public final class MyProcesser extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("test process " + roundEnvironment.processingOver());

        for (Element element :roundEnvironment.getElementsAnnotatedWith(CustomAnnotation.class)) {
            String str = element.getAnnotation(CustomAnnotation.class).value();

            JavaFile javaFile = buildJavaFile(str);
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private JavaFile buildJavaFile(String retrunStr){
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello World")
                .build();
        MethodSpec test = MethodSpec.methodBuilder("test")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(String.class)
                .addStatement("return $S", retrunStr)
                .build();
        TypeSpec typeSpec = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addMethod(main)
                .addMethod(test)
                .build();
        return JavaFile.builder("com.kemp.compiler", typeSpec).build();
    }
}
