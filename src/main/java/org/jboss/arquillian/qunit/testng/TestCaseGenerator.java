package org.jboss.arquillian.qunit.testng;

import java.lang.reflect.Field;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

import org.testng.annotations.Test;

public class TestCaseGenerator {

    private static final String METHOD_SOURCE = "public void %s () { handler.call(\"%s%s\"); };";

    private ClassPool classPool;
    private CallbackHandler callbackHandler;
    private String testCaseName;

    private CtClass ctClass;
    private ClassFile classFile;
    private ConstPool constPool;
    private Class<?> generatedClass;

    

    public TestCaseGenerator(String testCaseName, ClassPool classPool, CallbackHandler callbackHandler) throws RuntimeException, NotFoundException {
        this.classPool = classPool;
        this.callbackHandler = callbackHandler;
        this.testCaseName = testCaseName;

        initialize(testCaseName);
    }

    private void initialize(String testCaseName) throws RuntimeException, NotFoundException {
        this.ctClass = classPool.makeClass(testCaseName, classPool.get(SuperClass.class.getName()));
        this.classFile = ctClass.getClassFile();
        this.constPool = classFile.getConstPool();
    }

    public void addTestMethod(String methodName, Callback callback) throws CannotCompileException {
        callbackHandler.add(testCaseName + methodName, callback);

        String source = String.format(METHOD_SOURCE, methodName, testCaseName, methodName);

        CtMethod ctMethod = CtNewMethod.make(source, ctClass);
        ctClass.addMethod(ctMethod);

        // add @Test annotation
        AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation annotation = new Annotation(Test.class.getName(), constPool);
        attr.addAnnotation(annotation);
        ctMethod.getMethodInfo().addAttribute(attr);
    }

    public Class<?> toClass() throws CannotCompileException {
        if (generatedClass == null) {
            generatedClass = ctClass.toClass();
            finalizeClass();
        }
        return generatedClass;
    }

    private void finalizeClass() {
        setupCallbackHandlerToTestClass();
    }

    private void setupCallbackHandlerToTestClass() {
        try {
            Field field = SuperClass.class.getDeclaredField("handler");
            field.set(null, callbackHandler);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
