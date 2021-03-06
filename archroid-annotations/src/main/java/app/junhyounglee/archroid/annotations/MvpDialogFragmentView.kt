package app.junhyounglee.archroid.annotations

import androidx.annotation.LayoutRes
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class MvpDialogFragmentView(val view: KClass<*>, @LayoutRes val layoutResId: Int)