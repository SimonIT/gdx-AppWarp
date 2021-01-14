# gdx-AppWarp
[![](https://jitpack.io/v/SimonIT/gdx-AppWarp.svg)](https://jitpack.io/#SimonIT/gdx-AppWarp)
[![GitHub](https://img.shields.io/github/license/SimonIT/gdx-AppWarp)](https://github.com/SimonIT/gdx-AppWarp/blob/master/LICENSE)

The goal of this project is to provide a sdk for all platforms available with libgdx. For this, I'm using the version 2.3 of the [AppWarp Java SDK](https://github.com/shephertz/AppWarp_JAVA_SDK_JAR) and for gwt version 2.1 of the [AppWarp JS SDK](https://github.com/shephertz/AppWarp_JS_HTML5_SDK), for which I created the bindings to the listeners and events.

## Installation

You can replace _master-SNAPSHOT_ with any tag from the release section to use a stable version.

desktop, ios:

```groovy
api 'com.github.SimonIT.gdx-AppWarp:java:master-SNAPSHOT:all'
```


android:
Android contains already the json dependency, so we exclude it
```groovy
api('com.github.SimonIT.gdx-AppWarp:java:master-SNAPSHOT:all') {
    exclude group: 'org.json', module: 'json'
}
```

html:
GWT needs also th sources to compile
```groovy
api 'com.github.SimonIT.gdx-AppWarp:core:master-SNAPSHOT:sources'
api 'com.github.SimonIT.gdx-AppWarp:gwt:master-SNAPSHOT'
api 'com.github.SimonIT.gdx-AppWarp:gwt:master-SNAPSHOT:sources'
```
and add

```xml
<inherits name='de.SimonIT.gdxAppWarp'/>
```
to your _GdxDefinition.gwt.xml_.

core:
Core needs only the interface
```groovy
api 'com.github.SimonIT.gdx-AppWarp:core:master-SNAPSHOT'
```
