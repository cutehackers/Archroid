package app.junhyounglee.archroid.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class BindMvpPresenterFactory(val presenter: KClass<*>)