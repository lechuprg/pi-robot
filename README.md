# Raspberry Pi Robot

Project contains code samples for integration of multiple external devices with raspberry pi platform.
All code is written in Java 8.

YouTube link: https://www.youtube.com/watch?v=_jniu6XSi1k

Base modules integration:
 - servo: SG90Servo
 - HG7881 dual  motor drive with any dc motor
 - motion detector: HC-SR501
 - distance detector: HCSR04
 - digital relative humidity &temperature sensor AM2302/DHT22 
 - pi camera

Code was writen a few yers back as a home security system. Robot is able to
 - monitor moves make a photo and send it to email
 - if temperature is to high or to low you will also get notification via email
 

All services are exposed via rest controller (no security).
Check pi-robot-android-app repository for android app client.

