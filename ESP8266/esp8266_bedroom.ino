// Bedroom
// Version 1.0

// Libraries
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

// Firebase Connection
#define FIREBASE_HOST "home-cb143.firebaseio.com"
#define FIREBASE_AUTH "6CrBYr0y3au6uuNKJXFzyYKGF9ccWwh9FJ8FehRv"

// WiFi Connection
#define WIFI_SSID "TELUS5956-2.4G"
#define WIFI_PASSWORD "ftpsg89zc7"

// Pin Constants
const int ON = 1;
const int OFF = 0;
const int PRINT_DELAY_MS = 500;
const int LED_PIN = 12;
const String PATH = "JoshsRoom/BedroomLight/value";

void connectToWiFi() {

  // Connect to WiFi
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.println("Connecting to WiFi");

  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(PRINT_DELAY_MS);
  }

  if (WiFi.status() == WL_CONNECTED) {
    Serial.println();
    Serial.print("Connected: ");
    Serial.println(WiFi.localIP());
  }
}

void connectToFirebase() {
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.set(PATH, Firebase.getBool(PATH));
}

void setup() {
  Serial.begin(9600);

  pinMode(LED_BUILTIN, OUTPUT);
  pinMode(LED_PIN, OUTPUT);

  connectToWiFi();
  connectToFirebase();
}

void loop() {
  digitalWrite(LED_PIN, Firebase.getBool(PATH));
  delay(1000);
}



