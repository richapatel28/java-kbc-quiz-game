# KBC JavaFX (Kaun Banega Crorepati)

Simple JavaFX + SQLite JDBC KBC game. Login/Signup, play MCQs with prize ladder, lifelines (50-50 and Audience Poll), results and styling.

## Requirements
- JDK 17+
- IntelliJ IDEA (recommended)
- Internet access for Maven dependencies on first build

## Run (IntelliJ)
1. Open the project folder in IntelliJ (Open `pom.xml`).
2. Let Maven import.
3. Run configuration:
   - Main class: `com.kbc.Main`
   - VM options (if needed on your OS):
     - `--add-modules javafx.controls,javafx.graphics,javafx.base`
4. Click Run.

Alternatively, via Maven plugin:
```
mvn clean javafx:run
```

## Data
- Users stored in MySQL database `kbc` (configure in `src/main/resources/db.properties`).
- Questions loaded from `src/main/resources/questions.csv`.

### MySQL Setup
1. Install MySQL Server and create DB:
   - `CREATE DATABASE kbc CHARACTER SET utf8mb4;`
2. Update `src/main/resources/db.properties`:
   - `db.url=jdbc:mysql://localhost:3306/kbc?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`
   - `db.user=root`
   - `db.password=root` (your password)
3. First run auto-creates table `users`.

## Notes
- Passwords are stored as plain text for simplicity (educational). Do not use in production.
- No "Phone a Friend" per request.
- Audience Poll uses random but biased distribution shown via BarChart.

## Screens
- Splash ➜ Login/Signup ➜ Dashboard ➜ Game ➜ Result

Enjoy!

