**About application**

This application provides an ability to get latest GitHub events and shows the result in RecyclerView.

Event contains info about actor, who invoked event,event type, updated repository, event date.

As for portrait mode,after tap on specific event, it will be opened at new screen.
As for landscape mode, after tap on specific event, it will be appear in own fragment right to events fragment.

Events are synced by Workmanager with 5 minute interval. It's also possible to use pull-to-refresh feature.

As for images, Glide was used.
As for local storage, Room was used.

**About feature releases**
It's in plans to update app by fetching personalized events. So I am going to add user search screen to see user's feed.

**About architecture**

Clean Architecture [Robert C. Martin article](http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) approach is used as application basement.
Project structure is devided on 3 layers:

1. Data layer (data package) - This layer is responsible for "data" logic of application (contains code which communicates with database and remote API).
2. Business logic layer (domain package) - This layer is responsible for implementation of business use cases.
3. UI logic layer (presentation package) - This layer is responsible for end user application implementation. This layer contains another artitecture patterns for UI logic responsibility separation. It contains two approaches and you can check which one is more suited for you: **a)** MVP pattern **b)** MVVM pattern.

**Project details**

1. **Language** - Kotlin.
2. **Network stack** - Retrofit, OkHttp, GSON.
3. **Database** - SQLite + Room library.
4. **DI** - Dagger 2
5. **RxJava 2**
6. **MVVM implementation** - Google architecture components (ViewModel + LiveData).
