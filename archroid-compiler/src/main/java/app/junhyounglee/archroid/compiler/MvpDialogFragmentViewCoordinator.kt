package app.junhyounglee.archroid.compiler

import app.junhyounglee.archroid.annotations.BindMvpPresenter
import app.junhyounglee.archroid.annotations.MvpDialogFragmentView
import app.junhyounglee.archroid.compiler.codegen.ClassArgument
import app.junhyounglee.archroid.compiler.codegen.MvpDialogFragmentViewGenerator
import app.junhyounglee.archroid.compiler.codegen.MvpViewClassArgument
import com.squareup.kotlinpoet.ClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * @MvpDialogFragmentView(SampleView::class, R.layout.dialog_fragment_simple)
 *  Build step
 *  1. is SampleView an interface and a subclass of MvpView? if not error. only an interface can be used.
 *
 * @BindMvpPresenter(SamplePresenter::class)
 *  2. is SamplePresenter subclass of MvpPresenter which has SampleView as generic type?
 *     if not error. SamplePresenter should be a class which has a constructor containing SampleView
 *     parameter. If the presenter is an abstract class or an interface, it cannot be created from
 *     MVP view base class, MvpSampleDialogFragmentView.
 *
 *  3. create abstract mvp base class
 *     ex)
 *     abstract class MvpSampleDialogFragmentView
 *          : MvpDialogFragmentLifecycleController<SampleView, SamplePresenter>()
 *          , SampleView {
 *
 */
class MvpDialogFragmentViewCoordinator(processingEnv: ProcessingEnvironment)
    : MvpBaseCoordinator(processingEnv, MvpDialogFragmentView::class.java) {

    override fun onRoundProcess(roundEnv: RoundEnvironment, element: Element): ClassArgument? {
        val annotatedType = element as TypeElement

        // parse @MvpDialogFragmentView elements
        return parseMvpDialogFragmentView(annotatedType)
    }

    /**
     * Parse following annotations:
     *  @MvpDialogFragmentView(SampleView::class, R.layout.dialog_fragment_sample)
     *  @BindMvpPresenter(SamplePresenter::class)
     *  class SampleDialogFragmentView
     *
     * @param annotatedType annotated class type element
     * @return mvp view argument instance, null otherwise
     */
    private fun parseMvpDialogFragmentView(annotatedType: TypeElement): MvpViewClassArgument? {

        /*
        * Step 1. only class can be annotated with @MvpDialogFragmentView
        *  val elementType: TypeMirror = annotatedType.asType()
        */
        if (!annotatedType.kind.isClass) {
            error(annotatedType, "Only classes can be annotated with @MvpDialogFragmentView")
            return null
        }

        val builder = MvpViewClassArgument.builder()
            .setClassName(ClassName(getPackage(annotatedType).qualifiedName.toString(), createClassName(annotatedType)))
            .setTargetTypeName(getTargetTypeName(annotatedType))

        /*
         * Step 2. validate annotations for this class
         *  1. MvpDialogFragmentView
         *   @param view: interface that extends MvpView
         *  2. BindMvpPresenter
         *   @param presenter: should ba a class extending MvpPresenter that has a view extends
         *      MvpView
         */
        annotatedType.annotationMirrors.forEach { annotationMirror ->
            warning("Archroid> annotation : ${annotationMirror.annotationType.asElement().simpleName}")

            val annotationName = annotationMirror.annotationType.asElement().simpleName

            when {
                // MvpFragmentView annotation
                annotationName.contentEquals(MvpDialogFragmentView::class.simpleName) -> {
                    if (!parseMvpView(annotatedType, builder, annotationMirror)) {
                        // failed to parse annotation argument
                        return@parseMvpDialogFragmentView null
                    }
                }

                // BindMvpPresenter annotation
                annotationName.contentEquals(BindMvpPresenter::class.simpleName) -> {
                    if (!parseMvpPresenter(annotatedType, builder, annotationMirror)) {
                        // failed to parse annotation argument
                        return@parseMvpDialogFragmentView null
                    }
                }
            }
        }

        if (!builder.isValid()) {
            error(annotatedType, "${annotatedType.simpleName} class requires annotation @MvpDialogFragmentView and @BindMvpPresenter.")
            return null
        }

        return builder.build()
    }

    override fun onGenerateSourceFile(classArgument: ClassArgument) {
        val argument = classArgument as MvpViewClassArgument
        MvpDialogFragmentViewGenerator(processingEnv).apply { generate(argument) }
    }

}