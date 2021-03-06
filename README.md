<p align="center"><img src="https://github.com/zack-not-zac/PeekingDuck/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png" width="80px" height="80px" /> </p>

# PeekingDuck

## About
The PeekingDuck app allows you to inject your own keystrokes into a computer. It does this with the help of the [Android Keyboard Gadget](https://github.com/pelya/android-keyboard-gadget), which allows us to turn the phone into a keyboard. This application is meant to emulate and build upon the functionality of the [USB Rubber Ducky](https://shop.hak5.org/products/usb-rubber-ducky-deluxe). The app supports [DuckyScript](https://github.com/hak5darren/USB-Rubber-Ducky/wiki/Duckyscript), the language used by the USB Rubber Ducky to interact with the keyboard.

The application features:
* Ability to load, edit, and create DuckyScripts
* Add script instances to the queue (meaning you can queue multiple instances of the same script!)
* A simple user interface (No need for terminal emulators!)
* Run scripts when the phone is connected to a PC (currently requires the application to be in the foreground when the phone is connected)
* Use the phone storage to copy or save files from the connected computer when using MTP mode.

Please note that this application requires root permissions to execute scripts, and a phone with a kernel that supports HID. This application was developed and tested on a Nexus 5 running Android 6.0.1 with Kali NetHunter software installed.

## Creators
This application was developed as a group university project.

Contributors:
* [Zack Anderson](https://github.com/zack-not-zac) - Team Leader
* [Cameron McCallion](https://github.com/Cameron219) - Developer
* [Cameron Morrison](https://github.com/CameronMorrison97) - Secretary

## How to use

* [Creating a new script](#creating-a-new-script)
* [Loading a new script](#loading-a-new-script)
* [Delete a script](#delete-a-script)
* [Add script to the Queue](#add-script-to-the-queue)
* [Run the Queue](#run-the-queue)
* [Run the Queue on connection](#run-the-queue-on-connection)
* [Re-order the queue](#re-order-the-queue)
* [Delete a script from the queue](#delete-a-script-from-the-queue)
* [Clear the Queue](#clear-the-queue)
* [Transferring Files](#transfer-files)

Our example scripts can be found [here](https://github.com/zack-not-zac/PeekingDuck/tree/master/scripts).


### Creating a new script
The app allows a script to be added by typing it out within the app. This can be done by open the navigation bar and selecting the option "Create New Script"

<img src="https://i.imgur.com/U2LcP8V.png" width="720px" />

You then will be brought to a new screen with two text boxes. The first text box is for the name of the script, the second is for the script itself. For a full tutorial on the syntax and functions available in this scripting language, visit [the DuckyScript wiki page](https://github.com/hak5darren/USB-Rubber-Ducky/wiki/Duckyscript).

Once you're done creating your script, it can be added to the app with the + icon at the top right of the screen (in the toolbar).

<img src="https://i.imgur.com/h8Eic9W.png" width="720px" />

### Loading a new script
The application supports loading a script from internal storage. This is done by opening the navigation menu and selecting "Load New Script"

<img src="https://i.imgur.com/PkxyAij.png" width="720px" />

The phones file explorer should then appear, prompting the user to select a file. Once a payload has been selected, it should be loaded into the new script screen, allowing the user to enter a name for the script and add it (via the + icon in the toolbar).

<img src="https://i.imgur.com/doM90lh.png" width="720px" />


### Delete a script
To delete a specific script, select the trash can icon within that item.

<img src="https://i.imgur.com/Z8R7NLN.png" width="720px" />

You will be prompted to confirm, click yes to delete.

<img src="https://i.imgur.com/Aslsy7L.png" width="720px" />



### Add script to the Queue
Once a script has been added to the app, it can be placed in the queue. Navigate to the scripts page and click a script. This will open up the script editor. This will allow you to edit the script before adding it to the queue. This is necessary as some scripts hold variables (such as IP / Port), which need to be entered before the script is ran. Once the script is ready to be added to the queue, it can be done so via the + icon at the top right of the screen.

<img src="https://i.imgur.com/fKH7cE6.png" width="720px" />

Once this has been done, the queue can be viewed by open the navigation bar and selecting "Queue". Multiple instances of the script can be added to the queue.

### Run the Queue
The queue can be ran once it has at least one item in it. This can be done by pressing the play icon at the top right of the queue screen within the toolbar.

<img src="https://i.imgur.com/gtTJml7.png" width="720px" />

### Run the Queue on connection
The queue doesn't have to be run immediately, it can wait to be ran when there is a connection made to a PC. This can be done by simply pressing the play button while the phone is not connected to a computer. The app will detect it's not currently connected and prompt you to run when it is. Select yes on this prompt.

<img src="https://i.imgur.com/Hm9GgEv.png" width="720px" />


### Re-order the queue
Queue items can be re-ordered by simply long pressing on a queue item and dragging it up or down. The item must be held for 1+ second, the phone will vibrate to confirm you've picked up the item.


### Delete a script from the queue
Queue items can be deleted by pressing the - icon on the queue item.

<img src="https://i.imgur.com/tNAAn3p.png" width="720px" />


### Clear the Queue
While on the queue screen, press the trash can icon at the top right of the screen, in the toolbar. This will prompt you to confirm before clearing the queue.

<img src="https://i.imgur.com/lPQ3xhx.png" width="720px" />

### Transfer Files
To mount the phone as a storage device, navigate to Developer Settings (if you don't know how to do this, [here](https://developer.android.com/studio/debug/dev-options) is a quick tutorial).

Then find the "Select USB Configuration" option and choose MTP. Please note that in some versions of Android this feature is bugged and will only work on the first connection, and reset to charging only afterwards - even though the option will still say MTP is the default configuration. There is no permanent fix for this, meaning you will have to complete these steps every time you connect the device if you wish to use a script which can transfer files. 

Since phones do not mount as conventional storage devices on Windows, there is an example script outlining how to do this [here](https://github.com/zack-not-zac/PeekingDuck/blob/master/scripts/filecopy.txt).
