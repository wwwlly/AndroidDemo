package com.kemp.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.lang.model.util.Types;

@SupportedAnnotationTypes({"com.kemp.annotations.AutoExtension", "com.kemp.annotations.ActionFilter"})
@AutoService(Processor.class)
public class ExtensionOptProcessor extends AbstractProcessor {

//    public static final String EXTENSION_CLAZZ = "com.alibaba.ariver.kernel.api.extension.Extension";
//    public static final String BRIDGE_EXTENSION_CLAZZ = "com.alibaba.ariver.kernel.api.extension.bridge.BridgeExtension";
    public static final String EXTENSION_CLAZZ = "com.kemp.demo.extension.Extension";
    public static final String BRIDGE_EXTENSION_CLAZZ = "com.kemp.demo.extension.BridgeExtension";
    public static final String FILTER_CLASS = "com.kemp.annotations.ActionFilter";

    private Set<Element> mAlreadyOptElement = new HashSet<>();
    private Elements mElementUtils;
    private Types mTypeUtils;
    private Filer mFiler;
    private MethodSpec.Builder mOpt1Method;
    private MethodSpec.Builder mOpt2Method;
    private MethodSpec.Builder mOpt3Method;
    private TypeMirror mBridgeExtensionType;
    private TypeMirror mNormalExtensionType;
    private ClassName mExtensionClz;
    private ClassName mOptClz;
    private ClassName mOptInnerClz;
    private ClassName mExtensionPointClz;
    private ClassName mProxyGeneratorClz;
    private String mFinalClassName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        mElementUtils = processingEnv.getElementUtils();
        mTypeUtils = processingEnv.getTypeUtils();
        mFiler = processingEnv.getFiler();

        mExtensionClz = ClassName.get("com.alibaba.ariver.kernel.api.extension", "Extension");
        mOptClz = ClassName.get("com.alibaba.ariver.kernel.api.extension", "ExtensionOpt");
        mOptInnerClz = ClassName.get("com.alibaba.ariver.kernel.api.extension", "ExtensionOpt.MethodInvokeOptimizer");
        mExtensionPointClz = ClassName.get("com.alibaba.ariver.kernel.api.extension", "ExtensionPoint");
        mProxyGeneratorClz = ClassName.get("com.alibaba.ariver.kernel.api.extension", "ExtensionPoint.ProxyGenerator");

        mOpt1Method = MethodSpec.methodBuilder("opt1")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addJavadoc("Opt1: for normal extension optimize");
        mOpt2Method = MethodSpec.methodBuilder("opt2")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addJavadoc("Opt2: for bridge extension optimize");
        mOpt3Method = MethodSpec.methodBuilder("opt3")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addJavadoc("Opt3: for proxy optimize");

        mBridgeExtensionType = mElementUtils.getTypeElement(BRIDGE_EXTENSION_CLAZZ).asType();
        mNormalExtensionType = mElementUtils.getTypeElement(EXTENSION_CLAZZ).asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("process " + roundEnvironment.processingOver());

        if (roundEnvironment.processingOver()) {
            generateSource();
        } else {
            processInner(set, roundEnvironment);
        }
        return false;
    }

    private void processInner(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (annotations.size() == 0) {
            return;
        }

        for (TypeElement annotationElm : annotations) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(annotationElm);
            if (FILTER_CLASS.equals(annotationElm.asType().toString())) {
                for (Element pointMethod : elements) {
                    Element bridgeExtension = pointMethod.getEnclosingElement();
                    if (mAlreadyOptElement.contains(bridgeExtension)) {
                        continue;
                    }
                    mAlreadyOptElement.add(bridgeExtension);

                    if (mFinalClassName == null) {
                        mFinalClassName = "TestOpt";
                    }
                    optExtension(mOpt2Method, bridgeExtension, true);
                }
            }
            else {
                for (Element pointIntf : elements) {
                    if (pointIntf.getKind() == ElementKind.INTERFACE && mTypeUtils.isAssignable(pointIntf.asType(), mNormalExtensionType)) {
                        if (mFinalClassName == null) {
                            mFinalClassName = "TestOpt";
                        }
                        optExtension(mOpt1Method, pointIntf, false);
                        //optProxy(pointIntf);
                    } else if (mTypeUtils.isAssignable(pointIntf.asType(), mBridgeExtensionType)) {
                        if (mFinalClassName == null) {
                            mFinalClassName = "TestOpt";
                        }
                        if (mAlreadyOptElement.contains(pointIntf)) {
                            continue;
                        }

                        mAlreadyOptElement.add(pointIntf);
                        optExtension(mOpt2Method, pointIntf, true);
                    }
                }
            }
        }
    }

    private void generateSource() {
        if (mFinalClassName == null) {
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            TypeSpec clazzName = TypeSpec.classBuilder(mFinalClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addJavadoc("Auto generated on " + dateFormat.format(new Date()))
                    .addMethod(mOpt1Method.build())
                    .addMethod(mOpt2Method.build())
                    .addMethod(mOpt3Method.build())
                    .build();

            JavaFile.builder("com.kemp.compiler.apt", clazzName)
                    .build()
                    .writeTo(mFiler);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void optExtension(MethodSpec.Builder method, Element pointIntf, boolean bridge) {
        try {
            PackageElement packageElement = mElementUtils.getPackageOf(pointIntf);
            System.out.println("optNormalExtension element: " + pointIntf + " type: " + pointIntf.getKind() + " package: " + packageElement);
            MethodSpec.Builder optMethod = MethodSpec.methodBuilder("doMethodInvoke")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(String.class, "methodName")
                    .addParameter(ArrayTypeName.of(Object.class), "args")
                    .addException(Throwable.class)
                    .returns(Object.class);

            Map<String, Set<Integer>> methods = new HashMap<>();
            Map<String, Set<Integer>> dupNameAndSizeMethods = new HashMap<>();
            for (Element e : pointIntf.getEnclosedElements()) {
                if (e.getKind() == ElementKind.METHOD) {
                    ExecutableElement executableElement = asExecutable(e);
                    if (bridge && !isBridgeMethod(executableElement)) {
                        continue;
                    }

                    String methodName = String.valueOf(executableElement.getSimpleName());
                    int size = executableElement.getParameters().size();
                    if (methods.containsKey(methodName)) {
                        Set<Integer> set = methods.get(methodName);
                        if (set.contains(size)) {
                            Set<Integer> dupSet = dupNameAndSizeMethods.get(methodName);
                            if (dupSet == null) {
                                dupSet = new HashSet<>();
                            }
                            dupSet.add(size);
                            dupNameAndSizeMethods.put(methodName, dupSet);
                        }
                        set.add(size);
                    } else {
                        Set<Integer> set = new HashSet<>();
                        set.add(size);
                        methods.put(methodName, set);
                    }
                }
            }

            for (Element e : pointIntf.getEnclosedElements()) {
                if (e.getKind() == ElementKind.METHOD) {
                    ExecutableElement executableElement = asExecutable(e);
                    if (bridge && !isBridgeMethod(executableElement)) {
                        continue;
                    }

                    String simpleName = String.valueOf(executableElement.getSimpleName());
                    List<? extends VariableElement> params = executableElement.getParameters();
                    int size = params.size();
                    StringBuilder cfBuilder = new StringBuilder("if (\"$N\".equals(methodName) && args.length == ").append(size);

                    if (dupNameAndSizeMethods.containsKey(simpleName) && dupNameAndSizeMethods.get(simpleName).contains(size)) {
                        Object[] cfFormats = new Object[size + 1];
                        cfFormats[0] = executableElement.getSimpleName();
                        for (int i = 0; i < size; i++) {
                            cfBuilder.append(" && (args[").append(i).append("] == null || $T.class.isAssignableFrom(args[").append(i).append("].getClass()))");
                            TypeMirror typeMirror = params.get(i).asType();
                            cfFormats[i + 1] = mTypeUtils.erasure(typeMirror);
                        }
                        cfBuilder.append(")");
                        optMethod.beginControlFlow(cfBuilder.toString(), cfFormats);
                    } else {
                        cfBuilder.append(")");
                        optMethod.beginControlFlow(cfBuilder.toString(), executableElement.getSimpleName());
                    }

                    StringBuilder stringBuilder = new StringBuilder();
                    if (executableElement.getReturnType() != null && executableElement.getReturnType().getKind() != TypeKind.VOID) {
                        stringBuilder.append("return ");
                    }
                    stringBuilder.append("(($T)extension).").append(e.getSimpleName()).append("(");

                    TypeMirror[] args = new TypeMirror[size + 1];
                    args[0] = pointIntf.asType();
                    for (int i = 0; i < size; i++) {
                        if (i > 0) {
                            stringBuilder.append(",");
                        }
                        stringBuilder.append("($T) args[").append(i).append("]");
                        args[i + 1] = params.get(i).asType();
                    }
                    stringBuilder.append(")");
                    optMethod.addStatement(stringBuilder.toString(), args);
                    optMethod.endControlFlow();
                    System.out.println("");
                }
            }
            optMethod.addStatement("return null");
            String methodName = bridge ? "setupMethodInvokeOptimizerForBridge" : "setupMethodInvokeOptimizer";
            method.addStatement("$T." + methodName + "($T.class, $L)", mOptClz, pointIntf, TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(mOptInnerClz)
                    .addMethod(optMethod.build())
                    .build());

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private abstract static class CastingElementVisitor<T> extends SimpleElementVisitor6<T, Void> {
        private final String label;

        CastingElementVisitor(String label) {
            this.label = label;
        }

        @Override
        protected T defaultAction(Element e, Void unused) {
            throw new IllegalArgumentException(e + " does not represent a " + label);
        }
    }

    private static final class ExecutableElementVisitor extends CastingElementVisitor<ExecutableElement> {

        private static final ExecutableElementVisitor INSTANCE = new ExecutableElementVisitor();

        ExecutableElementVisitor() {
            super("executable element");
        }

        @Override
        public ExecutableElement visitExecutable(ExecutableElement e, Void unused) {
            return e;
        }
    }

    public static ExecutableElement asExecutable(Element element) {
        return element.accept(ExecutableElementVisitor.INSTANCE, null);
    }

    private boolean isBridgeMethod(ExecutableElement executableElement) {
        List<? extends AnnotationMirror> annotationMirrors = executableElement.getAnnotationMirrors();
        if (annotationMirrors == null) {
            return false;
        }
        boolean haveActionFilter = false;
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            if (FILTER_CLASS.equals(annotationMirror.getAnnotationType().toString())) {
                haveActionFilter = true;
            }
        }
        return haveActionFilter;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
