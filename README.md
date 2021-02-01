# Political Preparedness App

Political Preparedness App is an Android App that provides civic data intended to provide educational opportunities to the U.S. electorate using data provided by the Google Civic Information API. The data from this API is used to allow users to track information on target representatives and voting initiatives where applicable. 

## Getting Started

1. Clone the project to your local machine.
2. Open the project using Android Studio.
3. Add a Google Civic Information API Key to the CivicsHttpClient.kt class, in the network package:

private const val API_KEY = GOOGLE_CIVIC_API_KEY

You can find instructions how to request the API Key here:

https://developers.google.com/civic-information/docs/using_api

4. Run the app on your mobile phone or emulator with Google Play Services in it.

## Built With

* [Android Studio](https://developer.android.com/studio) - Default IDE used to build android apps
* [Kotlin](https://kotlinlang.org/) - Default language used to build this project
* [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) - Android Jetpack's Navigation component, used for Fragment-based navigation 
* [Motion Layout](https://developer.android.com/training/constraint-layout/motionlayout) - MotionLayout, a layout type that helps to manage motion and widget animation, and a subclass of ConstraintLayout
* [Retrofit](https://github.com/square/retrofit) - a type-safe HTTP client for Android and Java
* [Moshi](https://github.com/square/moshi) - a modern JSON library for Android and Java, that makes it easy to parse JSON into Java or Kotlin objects
* [Glide](https://github.com/bumptech/glide) - a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling into a simple and easy to use interface
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - a collection of libraries that help design robust, testable, and maintainable apps: Room (a SQLite object mapping library), LiveData (builds data objects that notify views when the underlying database changes), ViewModel (stores UI-related data that isn't destroyed on app rotations)
* [MVVM](https://developer.android.com/jetpack/guide) - the architecture pattern used in the app (Model-View-ViewModel), that incorporates the Android Architecture Components

## App Screenshots

<img src="https://user-images.githubusercontent.com/33599053/106476485-66af1b80-64a7-11eb-8d8e-f74bd6ba716e.png" width=30% height=30%> 

<img src="https://user-images.githubusercontent.com/33599053/106476452-60b93a80-64a7-11eb-9aae-0574cf90fba3.png" width=30% height=30%> 

<img src="https://user-images.githubusercontent.com/33599053/106476476-64e55800-64a7-11eb-81f5-da3928eb9d56.png" width=30% height=30%> 

<img src="https://user-images.githubusercontent.com/33599053/106476491-67e04880-64a7-11eb-9efd-058605e17960.png" width=30% height=30%> 

## License
Please review the following [license agreement](https://bumptech.github.io/glide/dev/open-source-licenses.html)
