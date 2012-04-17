package org.jboss.arquillian.qunit.junit;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

public class ClassGenerator {

    public Class<?>[] getClasses() {

        try {
            ClassPool pool = ClassPool.getDefault();

            // create the class
            CtClass clazz = pool.makeClass("GeneratedTestClass");

            // create the method
            CtMethod method = CtNewMethod.make("public void test () {};", clazz);
            clazz.addMethod(method);

            ClassFile classFile = clazz.getClassFile();
            ConstPool constPool = classFile.getConstPool();

            // create the annotation
            AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation annotation = new Annotation("org.junit.Test", constPool);
            attr.addAnnotation(annotation);

            method.getMethodInfo().addAttribute(attr);

            Class<?> generatedClass = clazz.toClass();

            return new Class<?>[] { generatedClass };

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

    }

}
