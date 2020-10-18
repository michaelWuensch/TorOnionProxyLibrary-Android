Get Started
===

## Implementation - Step 1: Assets/Binaries
 - **GeoIP files**:
    - Your application will need geoip and geoip6 files. Due to
    <a href="https://lists.torproject.org/pipermail/tor-dev/2020-January/014117.html" target="_blank">this issue</a>
    they currently cannot be provided via a dependency that can be easily updated. 
    Until then, you'll have to provide them.
    - Steps:
        - Download `geoip` and `geoip6` files from https://github.com/torproject/tor/tree/master/src/config
            - If you prefer the non-mirror repository, you can also obtain them from
            https://gitweb.torproject.org/tor.git/tree/src/config
        - In your Application module's `src/main/` directory, create a new `Directory` named `assets`
        - Copy `geoip` and `geoip6` files into the `assets` directory (or a subdirectory within 
        `assets`, such as `assets/common/`. File extensions not necessary).
 - **torrc file**:
    - Not needed
    - Your application's `torrc` file gets created for you based on what you have stored in 
    [TorServicePrefs](./topl-service-base/io.matthewnelson.topl_service_base/-tor-service-prefs/index.md). 
    If nothing is in `TorServicePrefs` for that particular setting, then it will fall back on 
    your static/default
    [ApplicationDefaultTorSettings](./topl-service-base/io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md)
    that you supply upon initialization of `TorServiceController.Builder`.  
 - **Tor Binaries**:        
    - I use The GuardianProject's <a href="https://github.com/guardianproject/tor-android" target="_blank">tor-android</a>
    project to re-package and provide *only* the binaries, as that's all which is needed by
    TOPL-Android. As of version 0.4.4.0, the binaries are simply copied instead of being
    re-built (prior versions I was building, but build reproducability is problematic...) which can
    be verified by checking the sha256sums (see the repo's README for instructions on how to do that).
        - They can be found <a href="https://github.com/05nelsonm/TOPL-Android-TorBinary" target="_blank">here</a>.
        - The only difference is the contents of what you are importing as a dependency. I package
        them in the `jniLibs` directory so that the Android OS will automatically extract them to
        your application's `/data/app/...` directory, and include no unnecessary classes or
        resources; just the binaries.
        - Android API 29+ no longer supports execution of executable files from your application's
        `/data/data/` directory, and must now be installed in the
        `context.applicationInfo.nativeLibraryDir` directory (aka, `/data/app/...`) to execute.
        - Nothing more is needed in terms of configuring initialization via the 
        `TorServiceController.Builder.useCustomTorConfigFiles`, as files will be installed in the
        correct directory, and named to match what `topl-service` looks for.
    - If you wish to use GuardianProject's dependency, see
    <a href="https://github.com/guardianproject/tor-android" target="_blank">tor-android</a>.
        - You'll need to use their `NativeResourceInstaller` to install the binaries.
        - You'll need to also implement `TorServiceController.Builder.useCustomTorConfigFiles` 
        method when initializing `topl-service` and provide it with your own 
        [TorConfigFiles](./topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md).
            - See the sample provided in the 
            [TorConfigFiles.Builder](./topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/-builder/index.md) 
            documentation.
 - **Transport Plugin Binaries**:
    - Currently unsupported. Working on it!!!  


!!! Info
    Tor Binaries are ~8MB for each ABI, so it's advised that builds are split to keep apk sizes 
    down. See the `sampleapp`'s `build.gradle` file for more details. 
    <a href="https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/sampleapp/build.gradle" target="_blank">here</a>  

## Implementation - Step 2: topl-service
 - In your Application module's `build.gradle` file, add the following to the `dependencies` block:
     ```groovy
     implementation "io.matthewnelson.topl-android:topl-service:{{ topl_android.release }}"
     ```
 
 - Create a new class which extends [ApplicationDefaultTorSettings](./topl-service-base/io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md)
   and apply your own default settings.
    - See the SampleApp's 
    <a href="https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/sampleapp/src/main/java/io/matthewnelson/sampleapp/topl_android/MyTorSettings.kt" target="_blank">MyTorSettings</a>
    class for help.  
    - Also checkout the documentation in the `TorSettings` class for more of a breakdown and help.

 - Optional: If you wish to receive broadcasts (TorState/NetworkState, Port Information, Logs, etc.),
 Create a new class which extends [TorServiceEventBroadcaster](./topl-service-base/io.matthewnelson.topl_service_base/-tor-service-event-broadcaster/index.md)
 and implement the abstract methods.
    - See the SampleApp's
    <a href="https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/sampleapp/src/main/java/io/matthewnelson/sampleapp/topl_android/MyEventBroadcaster.kt" target="_blank">MyEventBroadcaster</a>
    class for help.
    - Use the [TorServiceController.Builder.setEventBroadcaster](./topl-service/io.matthewnelson.topl_service/-tor-service-controller/-builder/set-event-broadcaster.md)
    and provide your implementation when initializing `topl-service`.
   
 - In your Application class' `onCreate` implement, and customize as desired, the
   [TorServiceController.Builder](./topl-service/io.matthewnelson.topl_service/-tor-service-controller/-builder/index.md)
   
 - Call APIs provided from
   [TorServiceController.Companion](./topl-service/io.matthewnelson.topl_service/-tor-service-controller/index.md)

### Multi-Module projects
 - If you have a `tor` module in your project that extends the api's in `TorServiceController` to centralize
 control of `topl-service`, other modules depending on it need only import the `topl-service-base`
 module which provides all of the necessary public classes/abstractions.

 - In your `tor` module's `build.gradle` file, add the following to the `dependencies` block:
     ```groovy
     api "io.matthewnelson.topl-android:topl-service-base:{{ topl_android.release }}"
     ```
 
### Using the SNAPSHOT version of topl-service

 - In your Application module's `build.gradle` file, add the following (*outside* the `android` block):
     ```groovy
     repositories {
         maven {
             url 'https://oss.sonatype.org/content/repositories/snapshots/'
         }
     }
     ```
   
 - In your Application module's `build.gradle` file, add (or modify) the following in the `dependencies` block:
     ```groovy
     implementation 'io.matthewnelson.topl-android:topl-service:{{ topl_android.next_release }}-SNAPSHOT'
     ```

    !!! Warning
        SNAPSHOT versions are ever changing and may contain not yet fully fleshed out features. Do **not** ship a release.
