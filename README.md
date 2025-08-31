Weather Application 🌤️
A sleek, dynamic, and fully-functional Android weather application that provides real-time meteorological data for any city worldwide. Built with modern Android development practices, it features a responsive UI that adapts visually to current weather conditions.


📸 Screenshots

<img src="/splashscreen.jpg" width="200"> <img src="/screenshot1.jpg" width="200">	<img src="/screenshot2.jpg" width="200">	

✨ Features
Real-time Weather Data: Fetches live data from the OpenWeatherMap API.

Dynamic UI: Background and animated icons change based on weather conditions (Sunny, Cloudy, Rainy, Snowy).

City Search: Global search functionality to find weather in any city.

Detailed Metrics: Displays temperature, humidity, wind speed, pressure, sunrise, and sunset times.

Elegant Splash Screen: Features a custom-designed splash screen with a smooth transition.

Modern Architecture: Built with MVVM (Model-View-ViewModel) for separation of concerns and testability.

🛠️ Built With
Kotlin - The primary programming language.

Retrofit2 & Gson - For type-safe HTTP networking and JSON parsing.

Kotlin Coroutines - For managing background threads and asynchronous tasks.

View Binding - For type-safe view interaction and null safety.

Lottie - For displaying beautiful, smooth weather animations.

OpenWeatherMap API - Provides the reliable weather data backbone.

🏗️ Architecture & Patterns
This app utilizes the Model-View-ViewModel (MVVM) architecture to ensure a clean separation of concerns:

Model: Data layer handling API responses via Retrofit and data classes.

View: XML layouts and Activities/Fragments that observe the ViewModel.

ViewModel: Holds UI-related data, survives configuration changes, and provides data to the View.

🔌 API Usage
This app uses the OpenWeatherMap Current Weather Data API.

Endpoint: https://api.openweathermap.org/data/2.5/weather

Parameters: q={cityName}, appid={API_KEY}, units=metric

🎨 Customization
The app's assets are easily customizable:

Backgrounds: Replace the .webp files in res/drawable (rain_bg, sunny_bg, etc.).

Lottie Animations: Download new .json animation files from LottieFiles and add them to res/raw.

Styling: Modify colors, shapes, and themes in res/values.

🤝 Contributing
Contributions, issues, and feature requests are welcome! Feel free to check the issues page.

Fork the Project

Create your Feature Branch (git checkout -b feature/AmazingFeature)

Commit your Changes (git commit -m 'Add some AmazingFeature')

Push to the Branch (git push origin feature/AmazingFeature)

Open a Pull Request

📄 License
This project is licensed under the MIT License. See the LICENSE.md file for details.

🙏 Acknowledgments
OpenWeatherMap for providing a robust and free weather API.

LottieFiles for the amazing weather animations.

Icons and UI inspiration from various designers on Figma.

⭐️ If you found this project helpful, please give it a star on GitHub!
