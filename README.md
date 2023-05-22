# Pomodoro Timer Android App

## Description
This is a Pomodoro Timer Android app that allows you to track your work and break sessions using the Pomodoro Technique. It includes a custom ListView that displays the history of your timed sessions. You can click on each session to be transferred to another activity where you can update your goals and add an image for that session. Additionally, when a timer session ends, you will receive a notification.

## Features
- Set custom work and break durations for your Pomodoro sessions.
- Start and pause the timer for work and break sessions.
- View a list of your past timed sessions in a custom ListView.
- Click on each session to update goals and add an image for that session.
- Receive a notification when a timer session ends.

## App Demonstration:
<p align="center" width="100%">
  <img src="https://github.com/eladlevi013/pomodoro-timer-android/assets/60574244/bf3e6240-aada-4009-a19e-b0e4edf0a6d8" width="20%" padding="50%">
   <img src="https://github.com/eladlevi013/pomodoro-timer-android/assets/60574244/21c4f909-42c4-456d-b381-c513e8e9ef46" width="20%" padding="50%">
  </br> 

## Installation
1. Clone the repository: `git clone https://github.com/eladlevi013/pomodoro-timer-android.git`
2. Open the project in Android Studio.
3. Build and run the app on an emulator or a physical device.

## Dependencies
- Android SDK 21+
- [NotificationCompat](https://developer.android.com/training/notify-user/build-notification)
- [Picasso](https://square.github.io/picasso/)
- [Firebase Realtime Database](https://firebase.google.com/docs/database)
- [Google Play Services Location](https://developers.google.com/android/guides/setup)
- [AppCompat](https://developer.android.com/jetpack/androidx/releases/appcompat)
- [Material Design](https://material.io/develop/android/docs/getting-started)
- [ConstraintLayout](https://developer.android.com/training/constraint-layout)
- [Firebase Firestore](https://firebase.google.com/docs/firestore)

## Usage
- Launch the app on your Android device.
- Set the desired work and break durations for your Pomodoro sessions.
- Click the "Start" button to begin a work session.
- When the work session ends, a notification will be displayed.
- Click on the session in the history list to update goals and add an image.
- Images are displayed using Picasso library.
- Session history is stored in Firebase Firestore, a NoSQL document-based database provided by Firebase.
- The session data is structured and stored as documents in collections within Firestore.

## Contributing
Contributions are welcome! If you'd like to contribute to this project, please follow these steps:
1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes and commit them.
4. Push your changes to your forked repository.
5. Submit a pull request detailing your changes.

## License
This project is licensed under the [MIT License](LICENSE).
