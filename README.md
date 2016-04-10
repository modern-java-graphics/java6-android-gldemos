
![Logo](http://i.imgur.com/AapyXg5.png "Logo")
============================
Modern OpenGL/ES examples in Java are few and far between and this collection of demos showcases what is possible with OpenGL ES 3.0 / 3.1 / 3.2 (forthcoming). The purpose is to inform developers, but also provide a testbed for cross-device testing between various mobile GPUs. This repo contains only demo code and depends on <a href="https://github.com/modern-java-graphics/java6-android-glframework" target="_blank">java6-android-glframework</a> and <a href="https://github.com/modern-java-graphics/java6-android-demoframework" target="_blank">java6-android-demoframework</a>. By separating the GL and demo framework code from the demos this allows you to use it separately as you see fit and also allow for a forthcoming port of the Nvidia Gameworks demos to a separate repo which will carry a different license than MPL v2 or Apache 2. The GL framework code provides concise utility code that makes working with OpenGL with Java easier and updates several utility classes like GLSurfaceView which has been "long in the tooth" in regard to the Android SDK utility code for OpenGL. The demoframework code contains basic utilities for providing an Android GUI for the demos. 

Device requirements: Android 5.0+ / OpenGL ES 3.0+ 

IDE requirements: Android Studio 2.x+ / build tools: 23.0.3

Latest Updates
========
(4.10.16): Update for Android Studio 2.x+ / moved repos to [modern-java-graphics](https://github.com/modern-java-graphics) organization. Moved Google Groups forum to <a href="https://groups.google.com/forum/#!forum/modern-java-graphics" target="_blank">modern-java-graphics</a>. Changed licenses for [java6-android-glframework](https://github.com/modern-java-graphics/java6-android-glframework) and [java6-android-demoframework](https://github.com/modern-java-graphics/java6-android-demoframework) to `MPL v2.0`. This repo (java6-android-gldemos) remains available via `Apache 2`.

(4.15.15): I finally had the chance to take a look at the Android 5.1 update for the Nexus 6 which brought a GPU driver update from Qualcomm, but it has flaws that prevent the compute shader examples from running which I 
<a href="https://groups.google.com/forum/#!topic/modern-java-graphics/e28OM-h7Kpw" target="_blank">discuss in this post on the forums.</a>

(1.1.15): I finally had a chance to do some refactoring this afternoon, so 
<a href="https://groups.google.com/forum/#!topic/modern-java-graphics/cKGEl9X-cpU" target="_blank">check out this post for more information.</a>

A <a href="https://groups.google.com/forum/#!forum/modern-java-graphics" target="_blank">Google web forum</a> is now available to discuss anything related to this modern OpenGL utility framework. If you have questions or comments about modern OpenGL with Java for Android check out the web forum and post your question. 

These samples require the latest Android Studio 2.x+ developer setup. To install Android Studio please <a href="http://developer.android.com/sdk/index.html" target="_blank">download it here</a>

There is a prebuilt apk located which can be [downloaded directly](https://github.com/modern-java-graphics/java6-android-gldemos/raw/master/prebuilt-apk/ModernGL.apk). With the Android developers tools installed simply run "adb install ModernGL.apk". An even easier way to install the GL demos app is to view this web page from your Android device after enabling installation of apps from unknown sources. To do this turn on "unknown sources" in the Settings->Security section of your device then click the download link above. When it finishes downloading you should be able to install it directly on your device without needing ADB.

To navigate in the GL demos app select a demo by a single tap and long press to open a web page to a wiki page. Since the demos just launched the wiki info for each demo will be updated eventually with more details. 

For full source code installation instructions refer to <a href="https://github.com/modern-java-graphics/java6-android-gldemos/wiki/installation" target="_blank">the wiki / install guide</a>. In time an expanded wiki will illuminate the full power of modern OpenGL with Java for Android. Please stay tuned as this effort is taken on by Michael Leahy, the author, in his spare time and more demos are forthcoming.

If you found these demos useful consider checking out the TyphonRT Video Suite which is his next-gen photo / video engine technology for Android using OpenGL ES 3.0+ and hardware accelerated media encoding / decoding. Click the image below to check it out:
<a href="http://www.typhonvideo.com/" target="_blank"><img src="http://i.imgur.com/gWh4A8M.png" 
alt="TyphonRT Video Suite" width="850" height="480" border="10" /></a>
