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

After installing the app, to get the audio working you need to get 2 things:

You need to get the accentAudio folder from the MIA/Migaku japanese anki addon and copy it to the
Typhon folder on your android device.

You also need to get the json file in the ExtraJsonAndMp3 folder that is here in this repository:
https://downgit.github.io/#/home?url=https://github.com/huh88/typhon/tree/master/ExtraJsonAndMp3
The json file also goes in the Typhon folder on your android device.

When moving the accentAudio to the Typhon folder, if you just connect your phone via cable to your pc and drag the files over its
literally gonna take days. I would reccomend using ADB push. In windows, when have set up adb and have your android connected, 
navigate to the folder containing the accentAudio folder in cmd. Then run this command:

adb push accentAudio /sdcard/Typhon

make sure your pc doesnt go to sleep during the transfer. Maybe go into windows power and sleep settings and set sleep to never
if you are gonna leave your pc during the transfer.

If you want pitch accent information for words that doesnt have native audio then drag the soundfiles \_a.mp3 \_h.mp3 \_o.mp3 \_n.mp3
into the accentAudio folder. \_h.mp3 is a native speaker saying  へ meaning the word is  へいばん. For odaka words with native audio
\_o.mp3 will play after the word is pronounced letting you know it is odaka and not heiban.


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
This version plays native audio to let you know the pitch accent of the words you look up. It is also useful if you already know the word but not how
to read it, just hearing the word is less effort than having to look down at the definition.

The audio that plays is the audio for the first result in the Edict dictionary. If you tap the word again it will play audio for the next result.
Keep tapping the word to cycle through. You must use the Edict dictionary.

Since you cant tell odaka and heiban apart when the words are pronouonced in isolation, An audiofile will play after odaka words to let you know it was
odaka. The audiofile is just a native speaker saying o for odaka.

For words that doesnt have a native audio it can also play a sound telling you what pitch it is, similiar to the odaka example above.


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

## Install Java
*   On Ubuntu

        sudo apt-get install openjdk-8-jdk

*   On Windows install the JDK from http://www.oracle.com/technetwork/java/javase/downloads/index.html

Typhon uses Java 8 lambda's through usage of the RetroLambda library.

## Install the Android SDK 

Typhon改 was created and tested with Android Studio 4.0

1.   Download at http://developer.android.com/sdk/index.html
2.   Unzip
3.   On Ubuntu install lib32z2

  ```
  sudo apt-get install lib32z1
  ```
4. Open the project in Android Studio (do not let it upgrade the Gradle plugin). This will automatically setup the SDK environment.

## Install USB drivers for your device

*   Install adb

    ```
    sudo apt-get install adb
    ````

*   Make sure adb devices shows your device, for example

    ```
    $ adb devices
    List of devices attached 
    015d18ad5c14000c        device
    ```

## Gradle

Typhon is built using Gradle. If you want to use a local Gradle version, make sure it's at least version 2.9.
The preferred way is to run the Gradle wrapper. This will automatically download the correct version of gradle to your system.

Run the Gradle wrapper by running

```
chmod a+x gradlew
./gradlew
```

## Build Typhon
Once everything is in place you can build Typhon and install it on your device with 

```
./gradlew build
./gradlew installDebug
```
