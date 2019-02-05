# FRC Scouting App

This is the Team 3883 Pit and Match Scouting android app

This app was built for the Lenovo Tab 4 8, but can easily be modifed for other screen sizes. We plan to redo this app with a dyamically generated UI and UI options in the off season.

Everything needed to generate the apk is in the repo, I have included a fork of a quanity picker library if you want to see the repo and what changes i have done to it please see [here](https://github.com/deadman96385/QuantityPicker)

The only setup you will need to do on the device is to create an FRC folder on the root of the device normally /sdcard/ and then put a teams.csv in there file which will be explained shortly.

###This is still very WIP and still being optimized

## Pit Scouting
The pit scouting portion of the app is self contained and will save all the entered data to a csv in /sdcard/FRC/pit_data.csv which can then be imported into excel. 
The picture button will open the camera and allow you to take a picture, that picture will be named whatever team number is entered at the top of the page and be located at /sdcard/FRC/Robots/

Screenshot of the Pit Scouting app section:

<details><summary>Hidden (Click to see it)</summary>
<img src="https://i.imgur.com/dd7BHim.png">
</details>  
&nbsp;

Example of the CSV format:

<details><summary>Hidden (Click to see it)</summary>
<img src="https://i.imgur.com/ot0qqKI.png">
</details>
<br/>

## Crowd Scouting
Crowd scouting is designed to be done with 6 collection tablets and 1 master tablet. Each of the 6 collection tablets has their position set in settings to Red1, Red2, etc which selects the team number they are watching based off of the match number and the data in the Teams.csv that was created.
Each person fills out the form per match, and the end of the match they click the export button which opens up a QR code which includes all of the data to be scanned by the Master device. The master device takes that data and saves it to a new row on the csv file. By default crowdscouting is in practice mode which is designed for pre-qualifcation matches where the team in play is not known ahead of time. In that mode you must manually enter the team number you are watching.

Screenshot of the Crowd Scouting app section:
<details><summary>Hidden (Click to see it)</summary>
<img src="https://i.imgur.com/Zqnva4l.png">
</details>
&nbsp;

Screenshot of the Master Device app section:
<details><summary>Hidden (Click to see it)</summary>
<img src="https://i.imgur.com/IEbni2A.png">
</details>
&nbsp;

Example of the CSV format:
Coming soon

## Team CSV
This CSV is created by entering in the compettion info once you recieve it on the first day. [Here](https://goo.gl/dKqBr7) is a link to one with random vaild team numbers for testing.
This file must go into /sdcard/FRC/ for crowd scouting to work outside of practice mode.
<details><summary>Hidden (Click to see it)</summary>
<img src="https://i.imgur.com/k3YKSTq.png">
</details>