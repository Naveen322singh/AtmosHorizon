Weather Application 🌤️
A sleek, dynamic, and fully-functional Android weather application that provides real-time meteorological data for any city worldwide. Built with modern Android development practices, it features a responsive UI that adapts visually to current weather conditions.


📸 Screenshots

Splash Screen	Main Screen (Sunny)	Main Screen (Rainy)	Search Dialog
<img src="/screenshots/splash.jpg" width="200">	<img src="/screenshots/sunny.jpg" width="200">	<img src="/screenshots/rainy.jpg" width="200">	<img src="/screenshots/search.jpg" width="200">
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

📦 Installation
Clone the repository

bash
git clone https://github.com/your-username/your-weather-app-repo.git
Open in Android Studio
Open the project in Android Studio (Arctic Fox or newer recommended).

Get an API Key

Sign up for a free account at OpenWeatherMap.

Navigate to the API Keys tab and generate a new key.

Add Your API Key

Create a file named secrets.properties in your root project directory (add it to your .gitignore!).

Add the following line to the file:

properties
WEATHER_API_KEY="YOUR_ACTUAL_API_KEY_HERE"
In your build.gradle.kts (Module :app) file, ensure you can access this key (common practice).

Build and Run

Build the project and run it on an emulator or physical device.

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

📧 Contact
Your Name - @YourTwitterHandle - email@example.com

Project Link: https://github.com/your-username/your-weather-app-repo

⭐️ If you found this project helpful, please give it a star on GitHub!
