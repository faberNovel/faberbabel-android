# FaberBabel Android SDK

[![Release](https://jitpack.io/v/fabernovel/faberbabel-android.svg)](https://jitpack.io/#fabernovel/faberbabel-android)

FaberBabel Android

[Changelog](CHANGELOG.md)

## Contribution
To contribute please read [the Contribution Guide](docs/CONTRIBUTING.md)

## Installation
Add Jitpack:
```
repositories {
  ...
  maven { url 'https://jitpack.io' }
}
```

Add the dependency:
```
dependencies {
  implementation 'com.github.fabernovel:faberbabel-android:<Version>'
}
```

## Usage
FaberBabel allows to update application wording dynamicaly, without re-deploying, by overriding its resources. 
The wording is fetch from back and saved as an application cache file.
The application that use FaberBabel must have the local default wording. Because, if there is un error durring fetching the wording from the back, and there is no
saved cach yet, so the local wording will be used. If the remote wording don't have some specific key of used string resource, it's default string resource for that
key that will be used. It is the same if some information of a specific key is missing, as text, plural resource or number of string format variables is not matching.

### Initialisation

`FaberBabel` is a class that allow to exploide all fonctionnalities of the SDK. It has two main tasks: 
- override application context to give the access to overrided resources with remote wording;
- fetch remote wording to update cache file and update dynamically the current wording. It can be fetch asynchronously or synchronously;

To use it, inject Faberbabel in the Application as a `Singleton` with application context as parameter. After that you can fetch the wording or get the overrited
context.

For example
```
@Module
class FaberbabelModule {

  @Provides
  @Singleton
  fun provideFaberBabelSdk(application: Application): FaberBabel {
    return Faberbabel(application)
  }
}

```

### Attach context to activity:

To have access to overrited resources, we must attach faberbabel context as base context of an activity. By doing this, the methods as "getString" or the view inflation
will use the remote wording instead off deafult one. The fragments started from the activity will have the same behaviour.
To do this, it is necessary to override the 'attachBaseContext' as below

```
@Inject
private lateinit val faberBabelSdk: FaberBabel

override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(faberBabelSdk.provideFaberBabelContextWrapper(newBase))
    }
```


#### Configuration
FaberBabel request 3 parameter:
- service url which is : "https://faberbabel-develop.herokuapp.com/translations/projects"
- project id that looks like : "349a41ab-7815-4cf6-ae54-2e5f2304bfe9"
- language code of wording that will be fetched : "en", "fr", "ua", "ru"...

The first 2 parameters should be placed in the config file of the application.

#### Inflation
Overrided context by FaberBabel provide also an overrided LayoutInflater that allows to inject remote wording during inflation of the view.

##### Menu inflatation
During menu inflation, the component that containt a menu create its own MenuInflater that is not part of LayoutInflater and was not overrided.
To perform a correct menu inflation with remote wording injection, all menu should be declared directly in the xml file of component that contains the menu.
By doing that, the menu can be detected by overrided LayoutInflater, that allows to pull the strings keys that are present in the menu xml file and inject
the remote wording instead of default one.

Example with menu (R.menu.example_menu)
```
DON'T

exampleToolbar.inflateMenu(R.menu.example_menu)


DO

<androidx.appcompat.widget.Toolbar
    android:id="@+id/exampleToolbar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:menu="@menu/example_menu"
    app:title="@string/example_wording"
    />

```

#### Api <26 precaution
For applications on device api 25 or bellow, the returned resources is not the one that is overrided by FaberBabel and that attached to base context.
To fix this problem, we should add the bellow code to the main activity where the overrided context is attached.

```
override fun getResources(): Resources {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            super.getResources()
        } else {
            baseContext.resources
        }
    }
```

### Wording fetch
For the wording fetch there is two possibilities: fetch wording asynchronously and synchronously.

#### Asynchronously
If the wording is fetched asynchronously, there is a high possibility that the network call will be too slow and the first application start, when there is no cach yet,
will display the default wording on the first screen. In other case is the cached wording that will be used if the network call is too slow.

To do the asynchronous fetch use the example code below

```
faberBabelSDK.asyncFetchFaberBabelWording(
            Config(
                FABERBABEL_SERVICE_URL,
                PROJECT_ID,
                LANGUAGE_CODE
            )
        )
```

#### Synchronously
If the wording is fetched synchronously, the network call run in the runBlocking coroutine. To avoid block the main thread, it should be called from a coroutine scope.

To do the synchronous fetch with action post execution use the example code below

```
  //show loading state
  GlobalScope.launch {
      faberBabelSDK.syncFetchFaberBabelWording(
          Config(
              FABERBABEL_SERVICE_URL,
              PROJECT_ID,
              LANGUAGE_CODE
          )
      )
      // do action after wording fetch
  }
```


## License

ADUtils is released under the Apache 2.0 license. [See LICENSE](LICENSE) for details.
