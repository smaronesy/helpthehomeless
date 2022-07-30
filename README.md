Help the Homeless (HH) Design Document

This Design doc can be found at:
https://docs.google.com/document/d/1DDqM4osVq9DwmzjZDU4T4o-QaTGO6dInIGO7a7dBSxY/edit#heading=h.d2vdwhod5qrg

Project Description

Help the Homeless is an app that informs users about the homeless living around their current location in case they feel charitable and want to make someone’s day. The app will rely on users to enter the information of the homeless in their area. With this information other app users will know about the homeless person and either help them financially or even better hire them based on their education and work experience. The App will serve as the LinkedIn for the homeless.

Once the user is logged in they can start adding homeless profiles by clicking on the plus button. Once the button is clicked the user will be asked to enter the required info by going through a few fragments. At the end of the flow, they will be shown a summary of what they have chosen and if everything looks good they can save the profile. Once the profile is saved if a user goes near the area of the added homeless individual, they will be notified by Android notifications. The app will be further improved, however, for the purposes of this project the requirements have been met.
Similar Apps

INSTRUCTIONS:

REQUIRED API KEYS:

1) Insert your Google maps API Key in the strings.xml file line 11:
   <string name="google_maps_key">"INSERT_YOUR_GOOGLE_MAPS_KEY_HERE"</string>
   -For instruction on how to obtain a google maps API key please visit:
   https://developers.google.com/maps/documentation/javascript/get-api-key

2) Insert your walk score api key in Constants.kt line 7:
   const val WALK_SCORE_API_KEY = "INSERT_YOUR_WALK_SCORE_API_KEY_HERE"
   -To get a walk score API key please visit:
   https://www.walkscore.com/professional/api-sign-up.php

Based on my research I was not able to find any similar Apps.
SW Development Process

XP: Planning, User Stories, Refactoring
Frameworks, Language, Libraries

The project will first be written for Android using Kotlin. The second stage of this project will be to build the same App for iOS and then Web. The main libraries which will be used are:

Androidx
Firebase
Room
Retrofit
Moshi
Glide
Google Maps
Jetbrains
Junit
Koin
Espresso
Mockito
Robolectric
Risks/Challenges

I do not foresee any major risks or challenges for this project that need to be addressed now
Non-Functional Requirements
Android UI/UX
Build a navigable interface consisting of multiple screens of functionality and data.

Application includes at least three screens with distinct features using either the Android Navigation Controller or Explicit Intents.
The Navigation Controller is used for Fragment-based navigation and intents are utilized for Activity-based navigation.
An application bundle is built to store data passed between Fragments and Activities.

Construct interfaces that adhere to Android standards and display appropriately on screens of different size and resolution.

Application UI effectively utilizes ConstraintLayout to arrange UI elements effectively and efficiently across application features, avoiding nesting layouts and maintaining a flat UI structure where possible.
Data collections are displayed effectively, taking advantage of visual hierarchy and arrangement to display data in an easily consumable format.
Resources are stored appropriately using the internal res directory to store data in appropriate locations including string* values, drawables, colors, dimensions, and more.
Every element within ConstraintLayout should include the id field and at least 1 vertical constraint.
Data collections should be loaded into the application using ViewHolder pattern and appropriate View, such as RecyclerView.

Animate UI components to better utilize screen real estate and create engaging content.

Application contains at least 1 feature utilizing MotionLayout to adapt UI elements to a given function. This could include animating control elements onto and off screen, displaying and hiding a form, or animation of complex UI transitions.
MotionLayout behaviors are defined in a MotionScene using one or more Transition nodes and ConstraintSet blocks.
Constraints are defined within the scenes and house all layout params for the animation.
Local and Network data
Connect to and consume data from a remote data source such as a RESTful API.

The Application connects to at least 1 external data source using Retrofit or other appropriate library/component and retrieves data for use within the application.
Data retrieved from the remote source is held in local models with appropriate data types that are readily handled and manipulated within the application source. Helper libraries such as Moshi may be used to assist with this requirement.
The application performs work and handles network requests on the appropriate threads to avoid stalling the UI.

Load network resources, such as Bitmap Images, dynamically and on-demand.

The Application loads remote resources asynchronously using an appropriate library such as Glide or other library/component when needed.
Images display placeholder images while being loaded and handle failed network requests gracefully.
All requests are performed asynchronously and handled on the appropriate threads.

Store data locally on the device for use between application sessions and/or offline use.

The application utilizes storage mechanisms that best fit the data stored to store data locally on the device. Example: SharedPreferences for user settings or an internal database for data persistence for application data. Libraries such as Room may be utilized to achieve this functionality.
Data stored is accessible across user sessions.
Data storage operations are performed on the appropriate threads as to not stall the UI thread.
Data is structured with appropriate data types and scope as required by application functionality.


Android system and hardware integration

Architect application functionality using MVVM.

Application separates responsibilities amongst classes and structures using the MVVM Pattern:
Fragments/Activities control the Views
Models houses the data structures,
ViewModel controls business logic.
Application adheres to architecture best practices, such as the observer pattern, to prevent leaking components, such as Activity Contexts, and efficiently utilize system resources.

Implement logic to handle and respond to hardware and system events that impact the Android Lifecycle.

Beyond MVVM, the application handles system events, such as orientation changes, application switching, notifications, and similar events gracefully including, but not limited to:
Storing and restoring state and information
Properly handling lifecycle events in regards to behavior and functionality
Implement bundles to restore and save data
Handling interaction to and from the application via Intents (ex: PendingIntent)
Handling Android Permissions

Utilize system hardware to provide the user with advanced functionality and features.

Application utilizes at least 1 hardware component to provide meaningful functionality to the application as a whole. Suggestion options include:
Camera
Location
Accelerometer
Microphone
Gesture Capture
Notifications
Permissions to access hardware features are requested at the time of use for the feature.
Behaviors are accessed only after permissions are granted.


Functional Requirements, User Stories

Actors: Helper
As a Helper I want to create an account so that I can login
As a Helper I want to login so I that I can use the app
As a Helper I want to be able to start a profile on behalf of a Homeless person so that others can find them
As a Helper, I want to add a homeless person’s contact info (if any) so that others can contact them for help or hiring
As a Helper I want to be able to add a homeless person’s location on a map so that they can be found by other helpers
As a Helper I would like to be prompted to grant foreground location permission if not already granted so that I can grant permissions
As a Helper I want the app to adjust the camera and zoom into my location so that I can see my exact location.
As a Helper I want to see my exact location on the Map when I am adding a Homeless person’s location so that I know the homeless person’s location relative to mine
As a Helper I would like to be able to re-center to my location so that I don’t have to look for my location on the map when I navigate away
As a Helper I want to be able to add a homeless person’s photo to their profile so that Helpers know how they look like when looking for them
As a Helper I want to be prompted to grant device and background location permissions to the app upon saving a profile so that I am notified when I am close to a homeless individual
As a Helper I would like to see a list of all Homeless people profiles so that I can check their profile
As a Helper I would like to be able to click on profile list items so that I can see the profile details
As a Helper I would like to see the walkscore of the are of the Homeless so that I can take proper precautions.

More user stories if time allows

As a Helper, I want to be able to edit the profiles of the homeless people I add so that I can change/update their info
As a Helper, I want to be able to create a profile for myself so that people can contact me regarding the homeless people I add
As a Helper I want to be able to edit my profile so that I can change/update my info

As a Helper I want to have the option to hide my contact info so that others do not contact me
As a Helper, I want add a homeless person’s background story to their profile so that Helpers learn about their pain
As a Helper, I want to add a homeless person’s degree to their profiles so that Helpers who want to hire them know their education
As a Helper, I want to add a homeless person’s experience to their profile so that Helpers who want to hire them know if their skills
Iteration
Iteration 1: user story 1 and 2
Estimated time: 3 days

Iteration 2: user story 3 and 4
Estimated time: 4 days

Iteration 3: user story 5, 6, 7, 8,  9
Estimated time: 7 days

Iteration 4: user story 10
Estimated time: 7 day

Iteration 5: user story 11, 12,
Estimated time: 7 days

Iteration 6: user story 13 and 14
Estimated time: 7 days
Design Pattern

Structures using the MVVM Pattern:

Fragments/Activities control the Views
Models houses the data structures,
ViewModel controls business logic.

The app will have the following Activities:

AuthenticationActivity
HomelessActivity
HomelessProfileActivity

The AuthenticationActivity is in charge of sign ins and sign ups and once the user and logged in AuthenticationActivity will start the main Activity using an Intent

The MainActivity will host the following fragments.

HomelessListFragment
AddHomelessFragment
SelectHomelessLocationFragment
UploadHomelessPhotoFragment
ReviewAndSaveProfileFragment

Each Fragment will have a ViewModel which will be connected to a Repository class. All classes will be in the same Module
