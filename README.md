
# = Introduction =

This is an Android client to be used together with the [p3Server](https://gitlab.com/kodeah/p3Server).

**It offers a simple graphical user interface to select [YouTube](https://www.youtube.com) videos, which you want to add to your current [MPD](https://www.musicpd.org/) play queue**. In addition, it provides controls to toggle playback, as well as skip or pull up a song.

This client is intended not to be used as the only client for MPD, but in addition to other [MPD clients](https://www.musicpd.org/clients/) that you like, since it does only offer minimal controls for MPD.

Note: This app uses plain HTTP to talk to the p3Server's web interface. So do not use either in untrusted networks.

![p3AndroidClient_screenshot](img/p3AndroidClient_screenshot.png "Screenshot")

# = Build / Install / Use =

At the moment, this application is only provided as source code from this repository. To install it, perform the following steps:

1) [Clone](https://www.howtogeek.com/451360/how-to-clone-a-github-repository/) or [download](https://gitlab.com/kodeah/p3Server/-/archive/master/p3Server-master.zip) this repository from this site.
2) Open it as a project inside [Android Studio](https://developer.android.com/studio/install).
3) Connect your android phone to your computer and enable [USB debugging](https://www.howtogeek.com/129728/how-to-access-the-developer-options-menu-and-enable-usb-debugging-on-android-4.2/) in your phone's settings.
4) [Run](https://developer.android.com/studio/run/) the application from Android Studio. It remains installed on your phone afterwards.

For this app to work as intended, it has to be able to connect to a host in your network that is running MPD as well as the p3Server.

Note: The graphical youtube selection interface is a bit glitchy in some aspects. You get the best user experience out of it by using the search bar to find songs, and waiting 1-2 seconds for the graphical interface to finish building up after performing a search. 

# = Configuration =

In the running app, a button on the top-right offers access to the following settings:

* **The server ip address or hostname (this has to be set to the address or network name of the host running the p3Server).**
* The server port (already configured to match the p3Server's default, don't change).
* A timeout in milliseconds for network calls (it is set to a low value by default; increase, if that makes problems).
