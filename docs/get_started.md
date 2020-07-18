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
    [TorServicePrefs](./topl-service/io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md). 
    If nothing is in `TorServicePrefs` for that particular setting, then it will fall back on 
    your static/default [TorSettings](./topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) 
    that you supply upon initialization of `TorServiceController.Builder`.  
 - **Tor Binaries**:        
    - I use The GuardianProject's <a href="https://github.com/guardianproject/tor-android" target="_blank">tor-android</a>
        project to build binaries, and provided them 
        <a href="https://github.com/05nelsonm/TOPL-Android-TorBinary" target="_blank">here</a>. The 
        difference is in how they are packaged as a dependency, and the contents of what you are
        importing as a dependency. I package them in the `jniLibs` directory so that the Android OS
        will automatically install them into your application's `/data/app/...` directory, and include
        no unnecessary classes or resources; just the binaries. Android API 29+ no longer supports 
        execution of executable files from your application's `/data/data/` directory, and must now be
        installed in the `context.applicationInfo.nativeLibraryDir` directory (aka, `/data/app/...`) 
        to execute.
            - Nothing more is needed in terms of configuring initialization via the 
            `TorServiceController.Builder`, as files will be installed in the correct directory, and 
            named to match what `topl-service` looks for.
    - If you wish to use GuardianProject's binaries, see 
    <a href="https://github.com/guardianproject/tor-android" target="_blank">tor-android</a>.
        - You'll need to use their `NativeResouceInstaller` to install the binaries.
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
     def topl_android_version = "{{ topl_android.release }}"
     implementation "io.matthewnelson.topl-android:topl-core-base:$topl_android_version"
     implementation "io.matthewnelson.topl-android:topl-service:$topl_android_version"
     ```
 
 - Create a new class which extends [TorSettings](./topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md)
   and apply your own default settings.
    - See the SampleApp's 
    <a href="https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/sampleapp/src/main/java/io/matthewnelson/sampleapp/MyTorSettings.kt">MyTorSettings</a> 
    class for help.
    - Also checkout the documentation in the `TorSettings` class for more of a breakdown and help.
   
 - In your Application class' `onCreate` implement, and customize as desired, the
   [TorServiceController.Builder](./topl-service/io.matthewnelson.topl_service/-tor-service-controller/-builder/index.md)
   
 - Call APIs provided from
   [TorServiceController.Companion](./topl-service/io.matthewnelson.topl_service/-tor-service-controller/index.md)
 
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
     def topl_android_version = "{{ topl_android.next_release }}-SNAPSHOT"
     implementation 'io.matthewnelson.topl-android:topl-core-base:$topl_android_version'
     implementation 'io.matthewnelson.topl-android:topl-service:$topl_android_version'
     ```

    !!! Warning
        SNAPSHOT versions are ever changing and may contain not yet fully fleshed out features. Do **not** ship a release.
