# Archroid
Android architecture library that generates boilerplate codes through annotation processing.<br/>
This project is mostly inspired by [SVC](https://github.com/BansookNam/svc) github library project, which provides different pattern called SVC (Screen, View, ControlTower). Those who are interested in the pattern, please visit the repository.
- This library provides simple lightweight framework for generateing architecture related codes with annotaion.
- and is designed to provide a convenient environment for managing or refactoring business logic by replacing repetitive codes with annotation.

## MVP implementation (building) 
### Prerequisites
To build the components, you will require two things
- View interface (MvpView)
- Presenter (MvpPresenter)
  
```kotlin
interface SampleView : MvpView {
    fun say(message: String)
}

class SamplePresenter(view: SampleView) : MvpPresenter<SampleView>(view) {

    override fun onResume() {
        super.onResume()
        view.say("Hello Archroid!")
    }
}
```

### MvpActivityView
```kotlin
@MvpActivityView(SampleView::class, layoutResId = R.layout.activity_sample)
@BindMvpPresenter(SamplePresenter::class)
class SampleActivityView : MvpSampleActivityView() {

    override fun say(message: String) {
        ...
    }

}
```

## Download
## Reference
- https://github.com/BansookNam/svc
- https://jakewharton.github.io/butterknife/
- https://docs.oracle.com/javase/tutorial/java/annotations/index.html
- http://hannesdorfmann.com/annotation-processing/annotationprocessing101
- http://hauchee.blogspot.com/2016/01/compile-time-annotation-processing-code-generation.html
- http://hauchee.blogspot.com/2015/12/compile-time-annotation-processing-getting-class-value.html
- https://www.baeldung.com/kotlin-annotations

## License
Copyright 2019 Jun Hyoung Lee

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
