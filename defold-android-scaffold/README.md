# Defold Android scaffold (reconstructed)

Extracted from `Perspective Parallax.apk` built by Defold 1.14.0. Defold has no Android project on disk; this is what it assembles at build time.

```
PerspectiveParallax.apk
├── AndroidManifest.xml              generated from a template + game.project (copy in ./AndroidManifest.xml)
├── classes.dex                      FIXED, prebuilt (23 KB)
│   ├── com.dynamo.android.DefoldActivity   extends NativeActivity
│   ├── com.defold.window.WindowJNI         keyboard/IME, screen dimming, network state
│   ├── com.defold.sound.Sound              sample rate / frames per buffer
│   └── com.defold.sound.SoundManager       phone-call and music-playing state
├── lib/arm64-v8a/
│   ├── libPerspectiveParallax.so    FIXED engine (libdmengine.so renamed after the game)
│   └── libvkquality.so              Vulkan quality data
├── assets/
│   ├── game.arcd                    GENERATED: packed game data
│   ├── game.arci                    GENERATED: archive index
│   ├── game.dmanifest               GENERATED: resource manifest
│   ├── game.projectc                GENERATED: compiled game.project
│   └── vkqualitydata.vkq            FIXED
├── res/drawable-*/icon.png          GENERATED: icon scaled per density
├── res/xml/splits0.xml, resources.arsc   produced by aapt2/bundletool
└── META-INF/BNDLTOOL.*              signed by bundletool
```

Link between the pieces, in the manifest:

```xml
<activity android:name="com.dynamo.android.DefoldActivity" ...>
    <meta-data android:name="android.app.lib_name" android:value="PerspectiveParallax" />
```

`NativeActivity` loads `libPerspectiveParallax.so` and calls its `android_main()`. Rendering and touch go through the NDK; audio, IME and window state go back to Java through JNI.

Equivalent Gradle-style project (what you would write by hand):

```
app/
├── build.gradle
├── src/main/
│   ├── AndroidManifest.xml
│   ├── java/com/dynamo/android/DefoldActivity.java
│   ├── java/com/defold/window/WindowJNI.java
│   ├── java/com/defold/sound/{Sound,SoundManager}.java
│   ├── jniLibs/arm64-v8a/libPerspectiveParallax.so
│   ├── assets/game.*
│   └── res/drawable-*/icon.png
```

Files here:
- `AndroidManifest.xml`: the manifest Defold generated.
- `classes.txt`: classes in `classes.dex`.
- `apk/`: the unzipped APK (git-ignored).
