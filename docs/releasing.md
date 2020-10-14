<!-- Thanks Square for providing great documentation that I only had to tweak -->
<!-- https://raw.githubusercontent.com/square/leakcanary/master/docs/releasing.md -->

# Releasing TorOnionProxyLibrary-Android

- Create a local release branch from `master`
```
git checkout master
git pull
git checkout -b release_{{ topl_android.next_release }}
```

- Update `VERSION_NAME` (remove `-SNAPSHOT`) in `gradle.properties`
```gradle
VERSION_NAME={{ topl_android.next_release }}
```

- Update the current version and next version in `mkdocs.yml`
```
extra:
  topl_android:
    release: '{{ topl_android.next_release }}'
    next_release: 'REPLACE_WITH_NEXT_VERSION_NUMBER'
```

- Perform a clean build
```
./gradlew clean
./gradlew build
```

- Generate the Dokka docs
```
rm -rf docs/topl-core docs/topl-core-base docs/topl-service
./gradlew topl-core-base:dokka topl-service-base:dokka topl-core:dokka topl-service:dokka
```

- Fix Dokka doc links
```
./gradlew fixDokkaDocLinks
```

- Update `docs/changelog.md` after checking out all changes:
    - <a href="https://github.com/05nelsonm/TorOnionProxyLibrary-Android/compare/{{ topl_android.release }}...master" target="_blank">compare changes</a>

- Update `docs/roadmap.md` with with new milestones:
    - <a href="https://topl-android.matthewnelson.io/roadmap" target="_blank">Project Roadmap</a>

- Take one last look
```
git diff
```

- Commit all local changes and PGP sign
```
git commit -S -am "Prepare {{ topl_android.next_release }} release"
```

- Create a PGP signed tag, and push it
```
git tag -s {{ topl_android.next_release }} -m "Release v{{ topl_android.next_release }}"
git push origin {{ topl_android.next_release }}
```

- Make sure you have valid credentials in `~/.gradle/gradle.properties` to sign and upload the artifacts
```
SONATYPE_NEXUS_USERNAME=<Your Username>
SONATYPE_NEXUS_PASSWORD=<Your Password>

signing.gnupg.homeDir=/home/matthew/.gnupg/
signing.gnupg.optionsFile=/home/matthew/.gnupg/gpg.conf
signing.gnupg.keyName=0x61471B8AB3890961
```

- Upload the artifacts to Sonatype OSS Nexus
```
./gradlew uploadArchives --no-daemon --no-parallel
```

- Release to Maven Central
    - Login to Sonatype OSS Nexus: <a href="https://oss.sonatype.org/#stagingRepositories" target="_blank">oss.sonatype.org</a>
    - Click on **Staging Repositories**
    - Scroll to the bottom, you should see an entry named `iomatthewnelson-XXXX`
    - Check the box next to the `iomatthewnelson-XXXX` entry, click **Close** then **Confirm**
    - Wait a bit, hit **Refresh**, until the *Status* for that column changes to *Closed*.
    - Check the box next to the `iomatthewnelson-XXXX` entry, click **Release** then **Confirm**

- Merge the release branch to master
```
git checkout master
git pull
git merge --no-ff -S release_{{ topl_android.next_release }}
```

- Update `VERSION_NAME` (increase version and add `-SNAPSHOT`)  and `VERSION_CODE` in `gradle.properties`
```gradle
VERSION_NAME=REPLACE_WITH_NEXT_VERSION_NUMBER-SNAPSHOT
VERSION_CODE=INCREMENT
```

- Commit your changes and sign with PGP keys
```
git commit -S -am "Prepare for next development iteration"
```

- Push your changes
```
git push
```

- Wait for the release to become available on <a href="https://repo1.maven.org/maven2/io/matthewnelson/topl-android/" target="_blank">Maven Central</a>, then:
    - Redeploy the docs:
        - `pipenv shell`
        - `mkdocs serve` to check locally
        - `mkdocs gh-deploy` to deploy
        - Check settings in GitHub that it did not revert the url back to use `github.io`
        - Ensure docs are served <a href="https://topl-android.matthewnelson.io" target="_blank">Here</a>
        - `exit`
    - Sign SampleApp release apks:
        - `scripts/sign_sampleapp_release_build.sh`
    - Go to the <a href="https://github.com/05nelsonm/TorOnionProxyLibrary-Android/releases/new" target="_blank">Draft a new release</a> 
    page
        - Enter the release name ({{ topl_android.next_release }}) as tag and title.
        - Have the description point to the changelog. You can find the direct anchor URL from the 
        <a href="https://topl-android.matthewnelson.io/changelog/" target="_blank">Change Log</a> 
        page on the doc site.
        - Upload the signed release apks.
