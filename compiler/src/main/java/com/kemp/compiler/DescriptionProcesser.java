package com.kemp.compiler;

import com.google.auto.service.AutoService;
import com.kemp.annotations.Description;
import com.kemp.annotations.Label;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
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

@SupportedAnnotationTypes({"com.kemp.annotations.Description", "com.kemp.annotations.Label"})
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

        HashMap<String, String> desMaps = new HashMap<>();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Description.class)) {
            TypeName typeName = ClassName.get(element.asType());
            System.out.println("typeName.toString() " + typeName.toString());
            String des = element.getAnnotation(Description.class).value();
            String simpleName = element.getSimpleName().toString();
            boolean isClass = element.getKind().isClass();
            System.out.println(isClass ? "element is class" : "element is not class");

            if (isClass)
                desMaps.put(simpleName, des);
        }

        HashMap<String, String> labelMaps = new HashMap<>();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Label.class)) {
            String labels = element.getAnnotation(Label.class).value();
            String simpleName = element.getSimpleName().toString();

            if (element.getKind().isClass())
                labelMaps.put(simpleName, labels);
        }

        JavaFile javaFile = buildJavaFile(desMaps, labelMaps);
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private JavaFile buildJavaFile(HashMap<String, String> DesMaps, HashMap<String, String> labelMaps) {
        MethodSpec.Builder initDesBuilder = MethodSpec.methodBuilder("initDescriptions")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(HashMap.class, String.class, String.class))
                .addStatement("HashMap<String, String> deses = new HashMap<>()");

        for (Map.Entry<String, String> entry : DesMaps.entrySet()) {
            initDesBuilder.addStatement("deses.put($S, $S)", entry.getKey(), entry.getValue());
        }
        MethodSpec initDes = initDesBuilder.addStatement("return deses").build();

        MethodSpec.Builder initLabelBuilder = MethodSpec.methodBuilder("initLabels")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(HashMap.class, String.class, String.class))
                .addStatement("HashMap<String, String> labels = new HashMap<>()");

        for (Map.Entry<String, String> entry : labelMaps.entrySet()) {
            initLabelBuilder.addStatement("labels.put($S, $S)", entry.getKey(), entry.getValue());
        }
        MethodSpec initLabel = initLabelBuilder.addStatement("return labels").build();

        TypeSpec typeSpec = TypeSpec.classBuilder("DescriptionCollector")
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addMethod(initDes)
                .addMethod(initLabel)
                .build();
        return JavaFile.builder("com.kemp.compiler", typeSpec).build();
    }
}
