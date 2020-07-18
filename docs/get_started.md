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
    - For now, you'll have to copy the binaries into your application's `src/main/jniLibs/<abi>/` 
    directory so that they are packaged in your APK **uncompressed** (which Android will automatically 
    extract to your application's native directory on the device (`/data/app/<your app>/<abi>/libTor.so`))
        - Tor v0.4.3.5 binaries can be found in the `sampleapp` which you can copy over (for the 
        time being).
    - I am currently building a gradle plugin to streamline binary distribution, so stay tuned!
        - The issue: Providing Tor Binaries packaged via an `aar` presents problems, as the 
        binaries are compressed which results in the Android OS not extracting them automatically. 
        This requires extraction of the Shared Native Library from the APK to be executed via 
        inclusion of `extractNativeLibs="true"` in the app's Manifest (min API 23), or code to 
        unzip the APK and extract the tor binaries to the application's native directory at runtime. 
        There are many bugs/limitations across the various Android APIs (16-29) that makes this 
        incredibly inefficient, not UX friendly for library users, error prone, and makes 
        versioning somewhat complicated as devs have no control over the Android OS' symlink
        creation process. API 29 also presents problems with extracting the binaries 
        to the application's `/data/data/` directory as it 
        <a href="https://developer.android.com/about/versions/10/behavior-changes-10" target="_blank">
        no longer supports running of executables from that location</a>. Because of the plethora 
        of issues across APIs, distribution of *.so files would be best done as a gradle plugin 
        such that upon building of your application, the *.so files included in the plugin can be 
        extracted to your application module's `src/main/jniLibs/<abi>/` directory. This ensures 
        that when your application is built and zipaligned, everything will simply work.
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
