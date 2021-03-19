# notes-synced
eine synchronisierte Notizen-App

# Aufgabenstellung

Erstelle eine synchronisierte Notizen-App, eine Web-Version ohne Framework, und eine Android-Version mit Android Studio/Java.




# Bedienungsanleitung




# Globale Variablen (nur Backend)

| Name           | Wert                                            |
|----------------|-------------------------------------------------|
| admin          | Rückgabewert von require('firebase-admin')      |
| firebase       | Rückgabewert von require('firebase')            |
| app            | Rückgabewert von require('express')()           |
| db             | Rückgabewert von admin.firestore()              |
| storage        | Rückgabewert von admin.storage()                |
| firebaseconfig | JSON-Konfiguration für firebase.initializeApp() |




# Mögliche Verbesserungen und Erweiterungen

- Preview von URLs

- Passwortgeschützte Notizen

- bessere Nutzer-Anpassung (Color Schemes, ...)

- weitere Funktionen (Nutzer löschen, Bestätigungs-Mails, Passwort-Reset-Mails, ...)

- (Web)     Allgemein bessere UI (Framework benutzen)

- (Android) Aktualisierung bim herunter scrollen




# Quellen

- den [Firestore](https://firebase.google.com/docs/firestore)- und [Functions](https://firebase.google.com/docs/functions)-Teil der Firebase-Dokumentation

- die [Android-Studio docs](https://developer.android.com/docs)

- Teile 5, 6 und 16 [dieses](https://www.youtube.com/watch?v=hVJe51Z67Bo&list=PLdHg5T0SNpN2NimxW3piNqEVBWtXcraz-&index=1) Tutorials

- [Diese Vorlage](https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example) für meine RecyclerView

- [Diesen code](https://stackoverflow.com/questions/27839105/android-lollipop-change-navigation-bar-color) um die Farbe der Home Bar zu ändern

- Dokumentationen

### Dependencies

eine automatisch generierte Liste gibts [hier](https://github.com/DesjardinsRepos/notes-synced/network/dependencies)
