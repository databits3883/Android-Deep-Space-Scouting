# FRC Scouting App

This was the original app we used for our 2 Deep Space Competition regionals, we used the Future Scouting repo for our State Competition

This is the Team 3883 Pit and Match Scouting android app

This app was built for the Lenovo Tab 4 8, but can easily be modifed for other screen sizes. We plan to redo this app with a dyamically generated UI in the off season.

Everything needed to generate the apk is in the repo, I have included a fork of a quantity picker library if you want to see the repo and what changes i have done to it please see [here](https://github.com/deadman96385/QuantityPicker)

The only setup you will need to do on the device is to create an FRC folder on the root of the device normally /sdcard/ and then put a teams.csv in there file which will be explained shortly.

###This app is functional but has its quircks

## Pit Scouting
The pit scouting portion of the app is self contained and will save all the entered data to a csv in /sdcard/FRC/pit_data.csv which can then be imported into excel. 
The picture button will open the camera and allow you to take a picture, that picture will be named whatever team number is entered at the top of the page and be located at /sdcard/FRC/Robots/

## Crowd Scouting
Crowd scouting is designed to be done with 6 collection tablets and 1 master tablet. Each of the 6 collection tablets has their position set in settings to Red1, Red2, etc which selects the team number they are watching based off of the match number and the data in the Teams.csv that was created.
Each person fills out the form per match, and the end of the match they click the export button which opens up a QR code which includes all of the data to be scanned by the Master device. The master device takes that data and saves it to a new row on the csv file. By default crowdscouting is in practice mode which is designed for pre-qualifcation matches where the team in play is not known ahead of time. In that mode you must manually enter the team number you are watching.

## Data Generated
This is the North Star Qualification match data spreadsheet we made (Read-Only, make a copy of it if you want to edit it).
* StatsRaw = Raw data imported into the sheet from the Master device
* Trending = Data fields over multiple matches graphed in a line graph
* Query = All teams listed with configurable columns to sort the data
* Team Lookup = User enterable team numbers to figure out team data
* Qual Match Info = Lists all of Team 3883's matches with their alliance mates and opponents so its easy to see in general how the match will go
* Pit Scouting = Has all the pit scouting data collected in its raw form

[Link](https://docs.google.com/spreadsheets/d/1GUqZBtCIhQMSCdasIogJNdImYHpLEd3w5SxdPndWn8U/edit?usp=sharing) for the sheet

## Team CSV
This CSV is created by entering in the compettion info once you recieve it on the first day. [Here](https://goo.gl/dKqBr7) is a link to one with random vaild team numbers for testing.
This file must go into /sdcard/FRC/ for crowd scouting to work outside of practice mode.
<details><summary>Hidden (Click to see it)</summary>
<img src="https://i.imgur.com/k3YKSTq.png">
</details>
