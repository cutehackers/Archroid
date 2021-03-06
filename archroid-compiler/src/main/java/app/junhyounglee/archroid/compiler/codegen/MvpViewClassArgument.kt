package app.junhyounglee.archroid.compiler.codegen

import app.junhyounglee.archroid.compiler.Id
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

class MvpViewClassArgument(
    targetTypeName: TypeName,
    className: ClassName,
    val viewType: ClassName,
    val layoutResId: Id,
    val presenterType: ClassName,
    val bindingNeeded: Boolean
) : ClassArgument(targetTypeName, className) {

    class Builder {
        private lateinit var targetTypeName: TypeName
        private lateinit var className: ClassName
        private lateinit var viewType: ClassName
        private lateinit var layoutResId: Id
        private lateinit var presenterType: ClassName
        private var bindingNeeded: Boolean = true

        fun setTargetTypeName(targetTypeName: TypeName) = apply { this.targetTypeName = targetTypeName }

        fun setClassName(className: ClassName) = apply { this.className = className }

        fun setViewType(viewType: ClassName) = apply { this.viewType = viewType }

        fun setContentView(layoutResId: Id) = apply { this.layoutResId = layoutResId }

        fun setPresenterType(presenterType: ClassName) = apply { this.presenterType = presenterType }

        fun setPresenterBindingNeeded(bindingNeeded: Boolean) = apply {
            this.bindingNeeded = bindingNeeded
        }

        fun isValid(): Boolean {
            return this::targetTypeName.isInitialized
                    && this::className.isInitialized
                    && this::viewType.isInitialized
                    && this::layoutResId.isInitialized
                    && this::presenterType.isInitialized
        }

        fun build() = MvpViewClassArgument(
            targetTypeName,
            className,
            viewType,
            layoutResId,
            presenterType,
            bindingNeeded
        )
    }


    companion object {
        fun builder() = Builder()
    }
}