<p align="center">
   <img src="https://i.pinimg.com/originals/91/f3/e0/91f3e06d665633d6aa79b33f941739f4.png" alt="alt text" width="300"/>
   <h1 align="center">NOTES-SYNCED</h1>
   <p align="center">eine synchronisierte Notizen-App, gehostet unter <a href="https://notes-synced.web.app">notes-synced.ga</a></p>
   <br/><br/><br/><br/>
</p>


# Aufgabenstellung

Erstelle eine synchronisierte Notizen-App, eine Web-Version ohne Framework, und eine Mobile-Version mit Android Studio/Java.



<br/>

# Bedienungsanleitung



<br/>

# Globale Variablen (nur Backend)

| Name           | Wert                                            |
|----------------|-------------------------------------------------|
| admin          | Rückgabewert von require('firebase-admin')      |
| firebase       | Rückgabewert von require('firebase')            |
| app            | Rückgabewert von require('express')()           |
| db             | Rückgabewert von admin.firestore()              |
| storage        | Rückgabewert von admin.storage()                |
| firebaseconfig | JSON-Konfiguration für firebase.initializeApp() |



<br/>

# Struktogramme
[] (in ....)



<br/>

# Mögliche Verbesserungen und Erweiterungen

- Preview von URLs

- Passwortgeschützte Notizen

- Notizen mit share-Funktion hinzufügen

- bessere Nutzer-Anpassung (Color Schemes, ...)

- weitere Funktionen (Nutzer löschen, Bestätigungs-Mails, Passwort-Reset-Mails, ...)

- (Web)     Allgemein bessere UI (Framework benutzen)

- (Android) Aktualisierung beim herunter scrollen

- (Android) flüssigeres Scrollen


<br/>

# Quellen

- den [Firestore](https://firebase.google.com/docs/firestore)- und [Functions](https://firebase.google.com/docs/functions)- Teil der Firebase-Dokumentation

- die [Android-Studio docs](https://developer.android.com/docs)

- die [JQuery docs](https://jquery.com/)

- Teile 5, 6 und 16 [dieses](https://www.youtube.com/watch?v=hVJe51Z67Bo&list=PLdHg5T0SNpN2NimxW3piNqEVBWtXcraz-&index=1) Tutorials

- [Diese Vorlage](https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example) für meine RecyclerView

- [Diesen code](https://stackoverflow.com/questions/27839105/android-lollipop-change-navigation-bar-color) um die Farbe der Home Bar zu ändern

- 6 Klassen/ 1 XML-Datei von dem Repository [ponnamkarthik/RichLinkPreview](https://github.com/ponnamkarthik/RichLinkPreview) für URL Previews

- einige meiner alten Programme

### Dependencies

- eine automatisch generierte Liste gibts [hier](https://github.com/DesjardinsRepos/notes-synced/network/dependencies)



<br/>

# Selber Ausprobieren

    git clone https://github.com/DesjardinsRepos/notes-synced
    cd notes-synced/client-android/
    ./gradlew build
