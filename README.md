TorOnionProxyLibrary-Android
===
**NOTE: This project exists independently of the Tor Project, & Guardian Project.**

This is a Fork of the <a href="https://github.com/thaliproject/Tor_Onion_Proxy_Library" target="_blank">Tor_Onion_Proxy_Library</a>

**What is different?**  
It's been rebuilt from the ground up and engineered specifically for Android, where it was
once multiplatform. It's been re-written in Kotlin, refactored to solve its concurrency
issues (among many others), and documented (still a work in progress). There are still
`Exceptions` that I'm sure I've missed annotating as there were little to none documented
pre-refactor, but it's an ongoing process to improve reliability/usability.

**What does it do?**  
The `topl-core` module contains the guts of operations for setting up a file system
in your application's data directory which Tor needs to run. It is the medium for which
other libraries built atop it access the TorProcess. The `topl-core-base` module contains
the base classes needed so that a library user of the `topl-service` module (or any other
library built on top of `topl-core`), won't need to rely directly on core and only needs to
import `topl-core-base` to get things set up.

The `topl-service` module is built atop the core modules and provide a simple way to embed
Tor as a foreground service into your application. It abstracts the technical &
difficult to understand things from `topl-core` away, and boils them down to an easy
to implement/use Library. Documentation on this will be provided upon release (it's
suuuuuuuper simple to implement and customizable to your liking ;-D!).

**What's the current Progress?**  
Tor is running very reliably. Start/stop works great on API 16-29 (Still needs much
much more testing). There is an immense amount of work that has yet to be done to get it
into the hands of developers, though. I hope to have an alpha release soon so others can
play around with it.

**How easy will it be to use when it's published?**  
 - Import dependenies into your Android App.
 - Extend the `TorSettings` class (used to build the `torrc` file) and configure it to your
 liking (I've made it as easy as I could to understand and provide default values to use. See
 the `sampleapp: MyTorSettings` class, or the documentation in the `topl-core-base: TorSettings` class).
 - Initialize things by implementing the `TorServiceController.Builder` in your
 `Application` class' `onCreate` and customize it as desired.
 - Call `TorServiceController.startTor`/`stopTor`/`restartTor`/`newIdentity`
 - That's it...
 
**Plans for more**  
 - Enable the passing of commands directly to the TorProcess which gives you full control
 over it.
 - An `onSharedPreferenceChangedListener` so that when your users modify settings and you
 save them to SharedPreferences via the `topl-service: TorServicePrefs` class, those settings
 will be applied immediately (if not requiring a restart of Tor, ofc).
 - Making Bridge support easier to use. Things are not yet hooked up for it in the `topl-service`
 module, but are working in `topl-core`.
 - Access control for authenticating to v3 Hidden Services.
 - Documentation documentation and more documentation to make this easy to understand and use.
 - more...
 
 **How do I play with it now?**  
 - Clone the repo
 - Initilize the git submodules
 - If you cannot run Bash scripts:
     - In the `topl-core` module's `build.gradle` file, comment out the `gradle.projectsEvaluated`
     block located at the bottom of the file.
 - Import to AndroidStudio
 - Run the `SampleApp` and look at Logcat.
