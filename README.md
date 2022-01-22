# letsFindRestaurant
A basic example of four square SDK to find the restaurants near the user's current location. While developing, I did focus on the Kotlin coroutines/folw , MVVM with UseCase and the repository layer. Used mockk and Junit 4 for the testing purpose.

- **Language**
  • Used **Kotlin** to develop the application with the power of **kotlin coroutines** for the task management,
  for more please visit https://developer.android.com/courses/pathways/android-coroutines

- **Architecture**
  • Used **MVVM + UseCase + Repository pattern** for design the architectural pattern,
  for more about MVVM visit https://medium.com/hongbeomi-dev/create-android-app-with-mvvm-pattern-simply-using-android-architecture-component-529d983eaabe
  & https://www.toptal.com/android/android-apps-mvvm-with-clean-architecture

- **Architecture Flow**
  • **Request/Update from/to view(Fragment/Activity) <--> ViewModel(Controller/request manipulation and business layer)
  <--> UseCases (Handling the request/last layer on the view side or you can say on the business layer side)
  <--> Repository(the end point before the dats source that could be API)  <-->  DataSource(API/Remote service) **
  please refer to the link available in the last point to know more

- **DI Framework**
  • Used Hilt for as an dependency injection frame work, for more https://developer.android.com/training/dependency-injection/hilt-android

- **How to run the app**
  • Please make sure that you are using the latest version of android studio along with the minimum support of Kotlin-1.4.20 and gradle-6.7.1


Please feel free to write me at malikdawar@hotmail.com in case of any further queries, cheers!
