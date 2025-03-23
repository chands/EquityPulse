# EquityPulse

A concise stock market news app that provides bite-sized stock market information in 60 words or less.

## Description

EquityPulse is an Android application that delivers the most important stock market news in a condensed format, making it easy for busy professionals to stay informed on market movements, stock performance, and financial events without spending too much time.

Think of it as "Inshorts for Stock Market News" - all the essential information, none of the fluff.

## Features

- **Concise News**: Every stock market story summarized in 60 words or less
- **Personalized Feed**: Follow stocks you're interested in
- **Market Categories**: Filter news by market sectors and indices
- **Bookmarks**: Save important stories for later reference
- **Offline Mode**: Read previously loaded content anytime, anywhere
- **Real-time Updates**: Get timely notifications about significant market events
- **Market Sentiment**: Understand market mood at a glance

## Technology Stack

- **UI**: Kotlin with Jetpack Compose
- **Architecture**: MVVM with Clean Architecture
- **Database**: Room for local storage
- **Networking**: Retrofit for API communications
- **Dependency Injection**: Dagger 2
- **Concurrency**: Coroutines and Flow
- **Testing**: JUnit, Mockito, Espresso

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or newer
- Min SDK: Android 5.0 (API level 21)
- Target SDK: Android 13 (API level 33)
- Kotlin 1.7.0+

### Setup

1. Clone this repository
2. Open the project in Android Studio
3. Sync project with Gradle files
4. Run the app on an emulator or physical device

## Development Approach

This project follows Test-Driven Development (TDD) principles:
1. Write failing tests first
2. Implement the minimum code needed to pass tests
3. Refactor to improve code quality while keeping tests passing

## Development Roadmap

### Phase 1: Core Improvements (Current Priority)
- [ ] **API Integration Enhancement**
  - Replace hardcoded API key with secure storage solution
  - Implement proper error handling for API rate limits
  - Add retry policy with exponential backoff for network failures
  - Create comprehensive DTOs for all API responses

- [ ] **Performance Optimization**
  - Implement WorkManager for background data syncing
  - Add pagination support for news feed with RecycledViewPool
  - Optimize database queries with proper indices
  - Implement memory cache for frequently accessed data

- [ ] **Testing Coverage**
  - Increase unit test coverage to at least 80%
  - Add integration tests for repository layer
  - Implement UI tests for main user flows
  - Set up automated testing pipeline

### Phase 2: Feature Expansion
- [ ] **Stock Portfolio Tracking**
  - Add custom stock watchlist creation
  - Implement portfolio performance analytics
  - Create price alerts for watched stocks
  - Add widget support for key portfolio metrics

- [ ] **Advanced News Filtering**
  - Implement topic-based news filtering
  - Add sentiment analysis visualization
  - Create customizable news categories
  - Add search history and suggestions

- [ ] **Offline Experience**
  - Implement robust caching strategy
  - Add scheduled background syncing
  - Create reading history and offline article management
  - Implement data compression for offline storage

### Phase 3: User Experience Enhancement
- [ ] **UI/UX Refinement**
  - Add animations for state transitions
  - Implement dynamic theming based on market sentiment
  - Create custom charts and visualizations
  - Add accessibility features (TalkBack, content scaling)

- [ ] **Personalization**
  - Implement user preference learning
  - Add content recommendation engine
  - Create customizable home screen layouts
  - Implement reading time estimates

- [ ] **Social Features**
  - Add sharing capabilities with custom previews
  - Implement saved article collections
  - Create community comment section for news items
  - Add follow capabilities for specific news sources

### Phase 4: Advanced Features
- [ ] **Analytics & Insights**
  - Implement Firebase Analytics for usage tracking
  - Add crash reporting with Crashlytics
  - Create user engagement metrics dashboard
  - Implement A/B testing framework

- [ ] **Performance Monitoring**
  - Add network performance tracking
  - Implement startup time optimization
  - Create custom performance markers for critical paths
  - Add automated performance regression testing

- [ ] **Scalability Enhancements**
  - Implement modular architecture with dynamic feature modules
  - Add multi-module support for faster build times
  - Create framework for feature toggles and remote config
  - Implement proper ProGuard rules for app size reduction

## Architectural Considerations

### Current Architecture
The app follows MVVM with Clean Architecture principles:
- **Presentation Layer**: Compose UI, ViewModels
- **Domain Layer**: UseCases, Repository Interfaces
- **Data Layer**: Repository Implementations, Local & Remote Data Sources

### Suggested Architectural Improvements
- Implement proper dependency injection with Hilt instead of Dagger for simpler setup
- Move to MVI pattern for more predictable state management
- Add proper error handling strategy throughout the app
- Improve separation of concerns in the Repository layer

## Contributing

If you'd like to contribute, please fork the repository and create a pull request. Issues and feature requests are welcome!

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Inshorts app for inspiration
- Various financial news services providing the data
