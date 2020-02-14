# App42MultiPlayerGamingSDK for libgdx
[![Build Status](https://travis-ci.com/SimonIT/App42MultiPlayerGamingSDK.svg?branch=master)](https://travis-ci.com/SimonIT/App42MultiPlayerGamingSDK)
[![](https://jitpack.io/v/SimonIT/App42MultiPlayerGamingSDK.svg)](https://jitpack.io/#SimonIT/App42MultiPlayerGamingSDK)
[![GitHub](https://img.shields.io/github/license/SimonIT/App42MultiPlayerGamingSDK)](https://github.com/SimonIT/App42MultiPlayerGamingSDK/blob/master/LICENSE)

The goal of this project is to provide a sdk for all platforms available with libgdx. For this, I'm using a a decompiled version of the [AppWarp Java SDK](https://github.com/shephertz/AppWarp_JAVA_SDK_JAR) and for gwt the [AppWarp JS SDK](https://github.com/shephertz/AppWarp_JS_HTML5_SDK), for which I created the bindings to the listeners and events.

## Installation

You can replace _master-SNAPSHOT_ with any tag from the release section to use a stable version.

desktop, ios:

```groovy
api 'com.github.SimonIT.App42MultiPlayerGamingSDK:java:master-SNAPSHOT:all'
```


android:
Android contains already the json dependency, so we exclude it
```groovy
api('com.github.SimonIT.App42MultiPlayerGamingSDK:java:master-SNAPSHOT:all') {
    exclude group: 'org.json', module: 'json'
}
```

html:
GWT needs also th sources to compile
```groovy
api 'com.github.SimonIT.App42MultiPlayerGamingSDK:core:master-SNAPSHOT:sources'
api 'com.github.SimonIT.App42MultiPlayerGamingSDK:gwt:master-SNAPSHOT'
api 'com.github.SimonIT.App42MultiPlayerGamingSDK:gwt:master-SNAPSHOT:sources'
```
and add

```xml
<inherits name='de.SimonIT.App42MultiPlayerGamingGwtSDK'/>
```
to your _GdxDefinition.gwt.xml_.

core:
Core needs only the interface
```groovy
api 'com.github.SimonIT.App42MultiPlayerGamingSDK:core:master-SNAPSHOT'
```
