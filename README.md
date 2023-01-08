# ParkOver
An Android application that helps you find a parking spot in a crowded area. Powered by Geospatial API and AR.

**Why Geospatial API ?**   
The Geospatial API provides for an anchor's horizontal (latitude and longitude) and vertical (altitude) positions following the WGS84 specification. You can place a Geospatial anchor almost anywhere in the world at a given latitude, longitude and altitude without needing to manually map the space and it provides accurate location based on the spaces around you.   
The ARCore Geospatial API enables you to remotely attach content to any area covered by Google Street View and create AR experiences on a global scale. It uses device sensors and GPS data to detect the device's environment, then matches the recognizable parts of that environment to a localization model provided by Google’s Visual Positioning System (VPS) to determine the precise location of a user’s device. The API also takes care of merging the user’s local coordinates with the geographic coordinates from VPS so that you can work within a single coordinate system.

![](https://redirector.gvt1.com/edgedl/arcore/geospatial.gif)

**How the app works:**   
It has two layers a map layer to show the parking spaces available around the area and an AR layer that gives the pin pointed information about the location and realtime details on the parking spots nearby. The two layers communicate with each other through a database keeping the data consistent across traditional map and geo spatial api map.   

**Tech Stack :  Android With Kotlin,  DataBase : firebase realtime database, Google maps api, Geospatial api, ARcore**  

Reference Link : 
https://developers.google.com/ar/develop/geospatial  



