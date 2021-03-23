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

### (Android) Hauptansicht
<br/>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/info-main.png?alt=media&token=12295342-55b5-41ce-8932-62676e49faaa"/>
<br/><br/><br/>

### (Android) Einloggen und Registrieren
<br/>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/info-anmelden.png?alt=media&token=90af8a21-7db6-4e40-8d74-f059bf78d65f"/>
<br/><br/><br/>

### (Android) Einstellungen
<br/>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/info-settings.png?alt=media&token=7372aa11-5d0a-4e26-aff5-f245f7e09c79"/>
<br/><br/><br/>

### (Web) Hauptansicht
<br/>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/1.png?alt=media&token=a3ebab73-f04b-47a4-9b7e-4c3360c82458"/>
<br/><br/><br/>

### (Web) Notiz erstellen
<br/>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/2.png?alt=media&token=d8c649db-3d1f-4a64-a2ea-3db65f10503c"/>
<br/><br/><br/>

### (Web) Einloggen
<br/>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/3.png?alt=media&token=6d56c51f-c52e-4ae1-b661-8a8d986cdc02"/>
<br/><br/><br/>

### (Web) Registrieren
<br/>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/4.png?alt=media&token=8e6a527d-06cb-42c1-85bc-167a5ee317c7"/>
<br/><br/><br/>

<br/>

# Globale Variablen (nur Backend)

| Name           | Wert                                            |
|----------------|-------------------------------------------------|
| admin          | Objekt zur Initialisierung von firebase <br/> (Rückgabewert von require('firebase-admin'))|
| firebase       | Globaler Namespace für alle Firebase-Services <br/> (Rückgabewert von require('firebase'))|
| app            | Express.js, ein serverseitiges Web-Framework <br/> (Rückgabewert von require('express')())|
| db             | Datenbank-Referenz <br/> (Rückgabewert von admin.firestore())|
| storage        | Firebase-Storage-Referenz <br/> (Rückgabewert von admin.storage())|
| firebaseConfig | Konfiguration für firebase.initializeApp() |


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

- (Android) Pull-to-refresh implementieren

- (Android) flüssigeres Scrollen

- (Android) Signup Bug


<br/>

# Quellen

- den [Firestore](https://firebase.google.com/docs/firestore)- [Storage](https://firebase.google.com/docs/storage)- und [Functions](https://firebase.google.com/docs/functions)- Teil der Firebase-Dokumentation

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
