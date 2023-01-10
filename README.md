
## General Notes:
<b>Important:</b> For the app to successfully compile please add the openweather app id to `local.properties` file with key `weather.appid` or replace line `54` in the app's `build.gradle.kts ` with the API id.

<b>Current state of the app:</b>
1. Fetches current user Location City.
2. Calls OpenWeatherMap.Org's API to fetch the weather forecast for the next 16 days which are displayed in a list.
3. When the user selects a day they see a dialog with more detailed view of that day. displaying more data like real feel temperatures throughout the day and more.
4. When the user puts the app in the background, a work manager starts running periodically every 15 minutes to fetch the current weather forecast and create a notification with the forecast.

#### Technical description:
* The app is structured in clean architecture + MVVM.
* Written in Kotlin, coroutines for threading and reactive code, used Jetpack compose for UI.
* Used gradle's [version catalogs](https://docs.gradle.org/current/userguide/platforms.html) to organize dependencies.
* The following libraries are used:
  * Used [Ktlint Gradle plugin](https://github.com/JLLeitschuh/ktlint-gradle) for linting and formatting.
  * [Koin](https://insert-koin.io/) for DI
  * [Locus android](https://github.com/BirjuVachhani/locus-android) For location permission and fetching current location.
  * Retrofit2 with kotlinx serialization for networking.
  * [Mockk](https://mockk.io/) and google [Truth](https://github.com/google/truth) for testing.
  * [Turbin](https://github.com/cashapp/turbine) for flows testing.
* There are 2 tests for demonstration:
  * `DailyForecastViewModelTest` Junit test.
  * `DayListItemUiTest` Android test.



#### Further ideas/enhancements
1. Properly handle Notification permission with rationales.
2. Make the design a bit more fancy by adding more drawables.
3. Adding a UI models layer to avoid formatting dates and strings in Repository layer.
4. Maybe using NavHost for list and details views and using separate ViewModels.
5. Extracting the mapping of API models to Domain Models to a mapper for ease of testing.
6. Writing more tests.