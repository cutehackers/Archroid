package app.junhyounglee.archroid.compiler.codegen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName


/**
 * @param targetTypeName annotated class type name
 * @param className virtual class name that is going to be created by annotation processor
 */
abstract class ClassArgument(val targetTypeName: TypeName, val className: ClassName)
