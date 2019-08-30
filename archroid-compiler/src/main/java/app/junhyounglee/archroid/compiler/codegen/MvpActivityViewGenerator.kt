package app.junhyounglee.archroid.compiler.codegen

import app.junhyounglee.archroid.compiler.Id
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.Filer

class MvpActivityViewGenerator(filer: Filer)
    : SourceFileGenerator<MvpActivityViewClassArgument>(filer) {

    override fun onGenerate(argument: MvpActivityViewClassArgument): TypeSpec {
        return argument.run {
            val builder = TypeSpec.classBuilder(argument.className.simpleName)
                .addKdoc(DOCUMENTATION)
                .addModifiers(KModifier.PUBLIC)
                .addModifiers(KModifier.ABSTRACT)
                .superclass(getSuperClass(this))
                .addSuperinterface(viewType)
                .addProperty(getPropertyImpl())
                .addProperty(getPropertyContext())
                .addProperty(getPropertyRootView())
                .addProperty(getPropertyIsRootViewAlive())
                .addFunction(getFunCreateMvpView(argument))
                .addFunction(getFunOnCreatePresenter(argument))

            layoutResId?.let {
                builder.addProperty(getPropertyLayoutResId(it)).build()
            } ?: builder.build()
        }
    }

    private fun getSuperClass(argument: MvpActivityViewClassArgument): ParameterizedTypeName {
        return argument.run {
            ClassName(CORE_PACKAGE, LIFECYCLE_CONTROLLER_CLASS).parameterizedBy(viewType, presenterType)
        }
    }

    private fun getPropertyImpl(): PropertySpec {
        return PropertySpec.builder("impl", ClassName(VIEW_PACKAGE, VIEW_CLASS))
            .addModifiers(KModifier.PRIVATE)
            .initializer("RootViewImpl()")
            .build()
    }

    private fun getPropertyContext(): PropertySpec {
        return PropertySpec.builder("context", ClassName(CONTEXT_PACKAGE, CONTEXT_CLASS).copy(nullable = true))
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .getter(FunSpec.getterBuilder().addStatement("return this").build())
            .build()
    }

    private fun getPropertyLayoutResId(resourceId: Id): PropertySpec {
        return PropertySpec.builder("layoutResId", Int::class)
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .getter(FunSpec.getterBuilder().addStatement("return %L", resourceId.code).build())
            .build()
    }

    private fun getPropertyRootView(): PropertySpec {
        val rootViewType = ClassName(ROOT_VIEW_PACKAGE, ROOT_VIEW_CLASS)
        return PropertySpec.builder("rootView", rootViewType)
            .mutable()
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .getter(FunSpec.getterBuilder().addStatement("return impl.container").build())
            .setter(FunSpec.setterBuilder().addParameter("value", rootViewType).addStatement("impl.container = value").build())
            .build()
    }

    private fun getPropertyIsRootViewAlive(): PropertySpec {
        return PropertySpec.builder("isRootViewAlive", Boolean::class)
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .getter(FunSpec.getterBuilder().addStatement("return impl.isViewAlive").build())
            .build()
    }

    private fun getFunCreateMvpView(argument: MvpActivityViewClassArgument): FunSpec {
        return FunSpec.builder("createMvpView")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .returns(argument.viewType)
            .addStatement("return this")
            .build()
    }

    private fun getFunOnCreatePresenter(argument: MvpActivityViewClassArgument): FunSpec {
        return FunSpec.builder("onCreatePresenter")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .returns(argument.presenterType)
            .addStatement("return %T(this)", argument.presenterType)
            .build()
    }


    companion object {
        private const val CORE_PACKAGE = "app.junhyounglee.archroid.runtime.core"
        private const val LIFECYCLE_CONTROLLER_CLASS = "MvpActivityLifecycleController"

        private const val VIEW_PACKAGE = "$CORE_PACKAGE.view"
        private const val VIEW_CLASS = "RootViewImpl"

        private const val CONTEXT_PACKAGE = "android.content"
        private const val CONTEXT_CLASS = "Context"

        private const val ROOT_VIEW_PACKAGE = "android.view"
        private const val ROOT_VIEW_CLASS = "ViewGroup"
    }
}
