<p align="center">
   <img src="https://i.pinimg.com/originals/91/f3/e0/91f3e06d665633d6aa79b33f941739f4.png" alt="alt text" width="300"/>
   <h1 align="center"><a href="https://github.com/DesjardinsRepos/notes-synced/blob/dokumentation/README.md">NOTES-SYNCED</a></h1>
   <p align="center">eine synchronisierte Notizen-App, gehostet unter <a href="https://notes-synced.web.app">notes-synced.ga</a></p>
   <br/><br/><br/><br/>
</p>


# Aufgabenstellung

Erstelle eine synchronisierte Notizen-App, eine Web-Version ohne Framework und eine Mobile-Version mit Android Studio/Java.



<br/>

# Wo ist was?
<br/>

### Backend-Code <sub>[functions/index.js](https://github.com/DesjardinsRepos/notes-synced/blob/main/functions/index.js)</sub>

### Android-Frontend(Logik) <sub>[client-android/app/src/main/java/com/example/notes_synced/*](https://github.com/DesjardinsRepos/notes-synced/tree/main/client-android/app/src/main/java/com/example/notes_synced)</sub>

### Android-Frontend(Aussehen) <sub>[client-android/app/src/main/res/layout/*](https://github.com/DesjardinsRepos/notes-synced/tree/main/client-android/app/src/main/res/layout)</sub>

### Web-Frontend <sub>[client/\*\[hc\]\*](https://github.com/DesjardinsRepos/notes-synced/tree/main/client)</sub>

# Bedienungsanleitung
<br/>

### (Android) Hauptansicht
<br/><div background="#ffffff">
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/info-main.png?alt=media&token=12295342-55b5-41ce-8932-62676e49faaa"/>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/info-main2.png?alt=media&token=a210eca8-6169-4d39-b874-377528fe43df" width="0"/>
<br/><br/><br/></div>

### (Android) Einloggen und Registrieren
<br/>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/info-anmelden.png?alt=media&token=90af8a21-7db6-4e40-8d74-f059bf78d65f"/>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/info-anmelden2.png?alt=media&token=fac3506e-858c-4251-a5ef-37d1fb1cea53" width="0"/>
<br/><br/><br/>

### (Android) Einstellungen
<br/>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/info-settings.png?alt=media&token=7372aa11-5d0a-4e26-aff5-f245f7e09c79"/>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/info-settings2.png?alt=media&token=1bc8fe80-d4e7-4258-957e-e2b9fa8e2e55" width="0"/>
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

<br/><br/>

### (Frontend/Android) Update-Funktion <sub>[client-android/app/src/main/java/com/example/notes_synced/MainActivity.java:253-311](https://github.com/DesjardinsRepos/notes-synced/blob/dokumentation/client-android/app/src/main/java/com/example/notes_synced/MainActivity.java)</sub>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/null.png?alt=media&token=b3dafcdb-b772-40bc-ab15-d3cd383a13aa"/>

<br/><br/>

### (Backend) Update-Funktionalität <sub>[functions/index.js:34-62,201-262](https://github.com/DesjardinsRepos/notes-synced/blob/dokumentation/functions/index.js)</sub>
<img src="https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/null(1).png?alt=media&token=a6f32127-34cc-4f3f-9755-fb2d2f4c4cd8"/>


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
