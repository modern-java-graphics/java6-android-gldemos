
![Logo](http://i.imgur.com/UhCv44F.png "Logo")
============================
Modern OpenGL/ES examples in Java are few and far between and this collection of demos showcases what is possible with OpenGL ES 3.0 / 3.1 + AEP. The purpose is to inform developers, but also provide a testbed for cross-device testing between various mobile GPUs. This repo contains only demo code and depends on <a href="https://github.com/typhonrt/modern-java6-android-glframework" target="_blank">modern-java6-android-glframework</a> and <a href="https://github.com/typhonrt/modern-java6-android-demoframework" target="_blank">modern-java6-android-demoframework</a>. By separating the GL and demo framework code from the demos this allows you to use it separately as you see fit and also allow for a forthcoming port of the Nvidia Gameworks demos to a separate repo which will carry a different license than Apache 2. The GL framework code provides concise utility code that makes working with OpenGL with Java easier and updates several utility classes like GLSurfaceView which has been "long in the tooth" in regard to the Android SDK utility code for OpenGL. The demoframework code contains basic utilities for providing an Android GUI for the demos. 

Device requirements: Android 5.0+ / OpenGL ES 3.0+ 

IDE requirements: Android Studio 1.0.1 / build tools: 21.1.2

Latest Update (1.1.15): I finally had a chance to do some refactoring this afternoon, so 
<a href="https://groups.google.com/forum/#!topic/modern-java6-android-gldemos/cKGEl9X-cpU" target="_blank">check out this post for more information.</a>

New: A <a href="https://groups.google.com/forum/#!forum/modern-java6-android-gldemos" target="_blank">Google web forum</a> is now available to discuss anything related to this modern OpenGL utility framework. If you have questions or comments about modern OpenGL with Java for Android check out the web forum and post your question. 

Right now only NVidia Tegra K1 based devices support OpenGL ES 3.1 properly. As things go Qualcomm / Motorola only shipped OpenGL ES 3.0 drivers for the Nexus 6; a firmware update with a proper 3.1 driver is necessary. 

These samples require the latest Android Studio developer setup. To install Android Studio please <a href="http://developer.android.com/sdk/index.html" target="_blank">download it here</a>

There is a prebuilt apk located which can be [downloaded directly](https://github.com/typhonrt/modern-java6-android-gldemos/raw/master/prebuilt-apk/ModernGL.apk). With the Android developers tools installed simply run "adb install ModernGL.apk". An even easier way to install the GL demos app is to view this web page from your Android device after enabling installation of apps from unknown sources. To do this turn on "unknown sources" in the Settings->Security section of your device then click the link above. When it finishes downloading you should be able to install it directly on your device without needing ADB.

To navigate in the GL demos app select a demo by a single tap and long press to open a web page to a wiki page. Since the demos just launched the wiki info for each demo will be updated eventually with more details. 

For full source code installation instructions refer to <a href="https://github.com/typhonrt/modern-java6-android-gldemos/wiki/installation" target="_blank">the wiki / install guide</a>. In time an expanded wiki will illuminate the full power of modern OpenGL with Java for Android. Please stay tuned as this effort is taken on by Michael Leahy, the author, in his spare time and more demos are forthcoming.

If you found these demos useful consider checking out the TyphonRT Video Suite which is his next-gen photo / video engine technology for Android using OpenGL ES 3.0+ and hardware accelerated media encoding / decoding. Click the image below to check it out:
<a href="http://www.typhonvideo.com/" target="_blank"><img src="http://i.imgur.com/gWh4A8M.png" 
alt="TyphonRT Video Suite" width="850" height="480" border="10" /></a>
