# FaberBabel Android SDK

Release 
``
1.0.0-SNAPSHOT
``

FaberBabel Android

[Changelog](CHANGELOG.md)

## Contribution
To contribute please read [the Contribution Guide](docs/CONTRIBUTING.md)

## Installation
Add to project build.gradle the following code:

* amazonaws classpath
```
dependencies {
        ...
        classpath 'com.amazonaws:aws-java-sdk-core:<Version>'
    }
```
* private aws repository 
```
allprojects {
    AWSCredentials awsCredentials = fetchAwsCredentials()
    repositories {
        ...
        maven { 
            url "s3://ft-maven-repo.s3.amazonaws.com/snapshot"
            credentials(AwsCredentials) {
                accessKey = awsCredentials.AWSAccessKeyId
                secretKey = awsCredentials.AWSSecretKey
                }
            }
        }
    }
}
```
* fetch aws credentials function
```
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.profile.ProfileCredentialsProvider

...

def fetchAwsCredentials = {
    try {
        return new ProfileCredentialsProvider("ft_maven_repo").credentials
    } catch (Exception e) {
        throw new GradleException(
            'Exception message'
        )
    }
}
```

Add the dependency:
```
dependencies {
  implementation 'com.github.fabernovel.faberbabel:faberbabel:<Version>'
}
```

## Usage
FaberBabel allows to update the application wording dynamically, without re-deploying a version of the application to the store, by overriding its resources. 
The wording is fetched from the server and saved as an application cache file.
The application that use FaberBabel must have a default wording xml file locally: if there is an error durring the fetching process and there is no
saved cache yet, the local wording will be used. If the remote wording don't have some specific key of a string resource, it's default value for that
key will be used. It is the same if some information of a specific key is missing, as text, plural resource or number of string format variables is not matching.

### Initialization

`FaberBabel` class is the single entry point of the SDK. It has two main tasks: 
- override application context to give the access to overrided resources with remote wording;
- fetch remote wording to update cache file and update dynamically the current wording. It can be fetch asynchronously or synchronously;

To use it, inject Faberbabel in the Application as a `Singleton` with application context as parameter. After that you can fetch the wording or get the overridden
context.

For example (using Dagger2 dependency injection):
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

To have access to the overridden resources, we must attach faberbabel context as base context of an activity. By doing this, the methods as "getString" or the view inflation will use the remote wording (saved on cache) instead of the deafult one. The fragments started from the activity will have the same behaviour.
To do this, it is necessary to override the 'attachBaseContext' as below (in the activity class): 

```
@Inject
private lateinit val faberBabelSdk: FaberBabel

override fun attachBaseContext(newBase: Context) {
    super.attachBaseContext(faberBabelSdk.provideFaberBabelContextWrapper(newBase))
}
```


#### Configuration
FaberBabel request 3 parameters:
- service url
    - develop: "https://faberbabel-develop.herokuapp.com/translations/projects"
- project id that looks like : "349b41aa-7813-4cd6-az54-2e5f2309bfe9"
- language code of wording that will be fetched, following the ISO 639-1 nomenclature (https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes): "en", "fr", "ua", "ru"...

The first 2 parameters should be placed in the config file of the application.

#### Inflation
Overridden context by FaberBabel provide also an overridden LayoutInflater that allows to inject remote wording during view inflation.

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
To fix this problem, you should add the bellow code to the main activity where the overrided context is attached.

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
A wording file can be retrieved both synchronously or asynchronously.

#### Asynchronously
If the wording is fetched asynchronously, there is a high probability that the network call will return after the first activity was inflated, so the the default wording will be visible on the first screen. In other case the cached wording will be used if the network call is too slow.

To perform an asynchronous fetch use the example code below
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
If the wording is fetched synchronously, the call will freeze the thread performing the call. To avoid block the main thread, it should be called from a background thread.

To perform a synchronous fetch use the example code below

```
//show loading state
coroutineScope.launch(Dispatchers.IO) {
    faberBabelSDK.syncFetchFaberBabelWording(
        Config(
            FABERBABEL_SERVICE_URL,
            PROJECT_ID,
            LANGUAGE_CODE
        )
    )
    // do something after wording fetch
}
```


## License

ADUtils is released under the Apache 2.0 license. [See LICENSE](LICENSE) for details.
