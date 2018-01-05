# Home

A home assistant that allows you to control appliances connected to ESP8266
microcontrollers through an Android app connected through Firebase and DialogFlow. Appliances can be individually controlled through the app in various ways such as speech and chat using natural language processing through DialogFlow.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

```
ArduinoIDE
Firebase
DialogFlow
AndroidStudio
```

### Installing

A step by step series of examples that tell you have to get a development env running

Add ESP8266 chip to the Arduino environment

```
Follow the instructions provided here: https://github.com/esp8266/Arduino
```

Run Android application on your device

```
git clone https://github.com/joshvocal/Home

Run it on your device
```

DialogFlow

```
Go to console

Click on the gear icon next to your project name

Go to the Export and Import tab

Choose RESTORE FROM ZIP
```

Firebase

```
Go to console

Create a new project and follow the instructions for Android

Replace the google-services.json in your project
```

## Built With

* [Firebase](https://firebase.google.com/) - Used as database to store switch values
* [DialogFlow](https://dialogflow.com/) - Used for natural language processing
* [ESP8266 Microcontroller](http://www.nodemcu.com/index_en.html) - Used to connect appliances to the internet
* [Firebase Arduino](https://github.com/firebase/firebase-arduino) - Used to connect ESP8266 to Firebase
* [ButterKnife](https://jakewharton.github.io/butterknife/) - Field and method binding for Android views
* [Firebase UI](https://github.com/firebase/FirebaseUI-Android) - Used for FirebaseRecyclerView for Chatbot

## Authors

* **Josh Vocal** - [GitHub](https://github.com/joshvocla)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
