
![Logo](http://i.imgur.com/UhCv44F.png "Logo")
============================
Modern OpenGL/ES examples in Java are few and far between and this collection of demos showcases what is possible with OpenGL ES 3.0 / 3.1 + AEP. The purpose is to inform developers, but also provide a testbed for cross-device testing between various mobile GPUs. This repo contains only demo code and depends on <a href="https://github.com/typhonrt/modern-java6-android-glframework" target="_blank">modern-java6-android-glframework</a>. By separating the GL framework code from the demos this allows you to use it separately as you see fit. The framework code provides concise utility code that makes working with OpenGL with Java easier and updates several utility classes like GLSurfaceView which has been "long in the tooth" in regard to the Android SDK utility code for OpenGL.  

Device requirements: Android 5.0+ / OpenGL ES 3.0+ 

IDE requirements: Android Studio 1.0.1 / build tools: 21.1.2

These samples require the latest Android Studio developer setup. To install Android Studio please <a href="http://developer.android.com/sdk/index.html" target="_blank">download it here</a>

There is a prebuilt apk located which can be [downloaded directly](https://github.com/typhonrt/modern-java6-android-gldemos/raw/master/prebuilt-apk/ModernGL.apk). With the Android developers tools installed simply run "adb install ModernGL.apk". Select a demo by a single tap and long press to open a web page to a wiki page. Since the demos just launched the wiki info for each demo will be updated over the next week or two. 

For full source code installation instructions refer to <a href="https://github.com/typhonrt/modern-java6-android-gldemos/wiki/installation" target="_blank">the wiki / install guide</a>. In time an expanded wiki will illuminate the full power of modern OpenGL with Java for Android. Please stay tuned as this effort is taken on by Michael Leahy, the author, in his spare time. 

If you found these demos useful consider backing his Kickstarter effort (closes in 12 hours!!) to launch a next generation video engine for Android which uses OpenGL ES 3.0+ and hardware accelerated media encoding / decoding.  Click the image below to check it out:
<a href="https://www.kickstarter.com/projects/85808410/typhonrt-video-suite-next-gen-video-apps-for-andro-0" target="_blank"><img src="http://i.imgur.com/gWh4A8M.png" 
alt="TyphonRT Video Suite" width="850" height="480" border="10" /></a>
