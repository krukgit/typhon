Typhon, the synchronizing ebook reader for Android
========================================================

Typhon is a free, open-source ebook reader that allows you to keep your reading progress synchronized across multiple devices.
This means you can read a few pages on your phone, than grab your tablet continuing where you left off.

It is a fork of PageTurner ( http://www.pageturner-reader.org/ ) and it adds a feature for Japanese dictionary lookup.

The lookup code takes its root on JadeReader. I recycled the Android specific code and made another 
project for the dictionary lookup called JRikai.

Note that Typhon does not require Google Play. I tested this to work on Android 9 and 10.

Typhon is licensed under the GPL-V3 license.

- Macoy Madson macoy@macoy.me (makuto fork's author;)
- Benjamin Marlé benjamarle@gmail.com (original author; hasn't worked on Typhon in years, unfortunately)

## huh88 Fork, Getting soundfiles
you need to get the accentAudio folder from the MIA/Migaku japanese anki addon and copy it to the Typhon folder on your android device.
If you connect your phone via cable to your pc and just drag the files over its literally gonna take days. I would reccomend using
ADB push. In windows, just open cmd and navigate to the folder containing the accentAudio folder. Then do:

adb push accentAudio /sdcard/Typhon

make sure your pc doesnt go to sleep during the transfer. Maybe go into windows power and sleep settings and set sleep to never
if you are gonna leave your pc during the transfer.

You also need to get the json file fullAccDict_.json and drag it into the typhon folder.

If you want pitch accent information for words that dont have native audio then drag the soundfiles \_a.mp3 \_h.mp3 \_o.mp3 \_n.mp3
into the accentAudio folder. \_h.mp3 is a native speaker saying  へ meaning the word is  へいばん. For odaka words with native audio
\_o.mp3 will play after the word is pronounced letting you know it is odaka and not heiban.


## Warning!

There is a bug in this fork, makuto's fork and dajimenezja's fork, where if you leave the library page and then come back, for example after
reading something, then the app will crash when you scroll. Books will also disappear from your library, and redoing the import of those books
will not make them show up. I reccomend only keeping the books you are currently reading in your library. That way there probably isnt enough
books to make scrolling possible.


## Definition Tips

Typhon should make reading EPUB files with Japanese text for Japanese learners a breeze. Simply click on a word to see its definition.

- You may need to click on adjacent characters to "feel around" for the longest, best-match phrase
- The correct definition may not be the first result; you should context clues to pick the best definition
- If you don't get any useful results, check ENamDict by scrolling the dictionary view over to the final column

...In other words, you need to use your brain, and that's a good thing!

If you want this functionality on your desktop, use Rikaichamp or other Rikai* plugins for your browser of choice.
On Android, Firefox has an addon, [nazeka](https://github.com/wareya/nazeka), that works with touchscreens.


huh88s fork
=================
This version plays native audio to let you know the pitch accent of the words you look up. It is also useful if you know the word just not how to read it
as just hearing the word is less effort than reading the definition.

After playing audio for odaka words will play audio of a native speaker saying o for odaka. This is because in isolation odaka and heiban are the same.

For words that doesnt have a native audio it can also play a sound telling you what pitch it is similiar to the odaka example above.


makuto's fork
=================
This is a version of Typhon with good E-Ink support, meaning text should be readable in most cases and there shouldn't be any white-on-black text.

This fork uses the below [dajimenezja fork](https://github.com/dajimenezja/typhon) for its modern Android version and inclusion of JRikai.jar by default.

I then made modifications to make Typhon suitable for e-Ink / e-Paper displays. In my case, it's for an Onyx Boox Nova 2.

I made the following modifications:
- Got rid of bookshelf image and replaced with black line on white background. Images don't look good in e-Ink, and this one serves little purpose anyways
- Made definition popup black text on white background (previously, it was hard-coded white/colored text on black)
- Made definition popup not show loading indicator. This causes unnecessary flashing of the e-ink display, and loading is very fast anyways
- Changed controls to be inverted from black-on-white to white-on-black, when appropriate

Typhon改 (dajimenezja fork)
=================
([dajimenezja fork](https://github.com/dajimenezja/typhon))

dajimenezja's modifications:
- Revise code base to androidx api29, fixing some UI bugs (Notably fixes unused space at the bottom of tall displays)
- Update anki integration so it doesn't crash on android 26+
- Add storage permission requests on android 26+

Building Typhon
=================
*The following may be out of date.* I personally just opened up the project in Android Studio and let it work its magic.

## Install Java
*   On Ubuntu

        sudo apt-get install openjdk-8-jdk

*   On Windows install the JDK from http://www.oracle.com/technetwork/java/javase/downloads/index.html

Typhon uses Java 8 lambda's through usage of the RetroLambda library.

## Install the Android SDK 

Typhon改 was created and tested with Android Studio 4.0

1.   Download at http://developer.android.com/sdk/index.html
2.   Unzip
3.   Update 

        sdk/tools/android update sdk --no-ui
4. On Ubuntu install ia32-libs

        apt-get install ia32-libs
5. Add sdk/tools/ and sdk/platform-tools to your PATH

## Install USB drivers for your device

*   Make sure adb devices shows your device, for example

        $ adb devices
        List of devices attached 
        015d18ad5c14000c        device

## Example PATH setup in .bashrc

    export ANDROID_HOME=$HOME/projects/adt-bundle-linux/sdk/
    if [ $(uname -m) == 'x86_64' ]; then
        export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64/jre
    else
        export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-i386/jre
    fi

    PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools


## Gradle

Typhon is built using Gradle. If you want to use a local Gradle version, make sure it's at least version 2.9.
The preferred way is to run the Gradle wrapper. This will automatically download the correct version of gradle to your system.

Run the Gradle wrapper by running

    gradlew

## Build Typhon
Once everything is in place you can build Typhon and install it on your device with 

    gradlew build
    gradlew installDebug
