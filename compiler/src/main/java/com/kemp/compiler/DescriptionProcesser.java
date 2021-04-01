package com.kemp.compiler;

import com.google.auto.service.AutoService;
import com.kemp.annotations.Description;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

@SupportedAnnotationTypes("com.kemp.annotations.Description")
@AutoService(Processor.class)
public class DescriptionProcesser extends AbstractProcessor {
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        HashMap<String, String> maps = new HashMap<>();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Description.class)) {
            String des = element.getAnnotation(Description.class).value();
            String simpleName = element.getSimpleName().toString();

            maps.put(simpleName, des);
        }

        JavaFile javaFile = buildJavaFile(maps);
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private JavaFile buildJavaFile(HashMap<String, String> maps) {
        MethodSpec.Builder initBuilder = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(HashMap.class, String.class, String.class))
                .addStatement("HashMap<String, String> deses = new HashMap<>()");

        for (Map.Entry<String, String> entry : maps.entrySet()) {
            initBuilder.addStatement("deses.put($S, $S)", entry.getKey(), entry.getValue());
        }

        MethodSpec init = initBuilder.addStatement("return deses").build();

        TypeSpec typeSpec = TypeSpec.classBuilder("DescriptionCollector")
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addMethod(init)
                .build();
        return JavaFile.builder("com.kemp.compiler", typeSpec).build();
    }
}
