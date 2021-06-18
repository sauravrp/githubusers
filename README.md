# Overview
Build a github users directory app that shows a list of users parsed from  
https://api.github.com/users?per_page=20&page=1

# Focus Area
My primary focus was on the architecture and the data flow. 
For UI, my focus was on the phone

# Architecture
The architecture utilizes Model-View-Intent pattern. Dagger is used for dependency injection 
to separate out the components into the application component and feature component. 
This enables scaling of the feature components, allows testing as well as resuability of 
lower level components.
The project takes advantage of LiveData for binding presentation layer with the view model 
and RxJava for fetching data from the network.

# Data Flow
Data is fetched from the network using Retrofit/OkHttp using network data models. These network models
are then converted to data models for easy consumption by the presentation layer with the added
benefit of presentation layer being decoupled from the network data models.

# Copied in Code or Copied in Dependencies
The project utilizes various 3rd party libraries such as Retrofit, Stetho, Timber, Picasso etc.
I've added stack over flow links to a few places in the test code where a few lines of code were copied.

# Areas for Improvement
- Code could use more comments.
- On initial load, data is fetched from the network, inserted into the database and then only
returned to the caller. This can be optimized to return first and insert into the datbase after.
- More unit tests, instrumented tests, db test, UI tests and integration tests could be implemented.
- Using an old version of Roboelectric due to issues with Mockk.
- Support infinite scrolling with caching by using Remote Mediator and Paging from Paging 3 library.

# Tools Used
Android Studio 4.1.1, used Pixel 2 API 27 emulator for all of testing