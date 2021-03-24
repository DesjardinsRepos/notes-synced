// initialisation
const admin = require('firebase-admin');
const firebase = require('firebase');
const app = require('express')();

admin.initializeApp({
    credential: admin.credential.cert(require('./serviceAccountKey.json')),
    databaseURL: "https://notes-synced.firebaseio.com",
    storageBucket: 'notes-synced.appspot.com'
});

const db = admin.firestore();
const storage = admin.storage();
const firebaseConfig = {
    apiKey: "AIzaSyA0iz0HDXys7pSU_k-m1LBMOiXr1_jPpGM",
    authDomain: "notes-synced.firebaseapp.com",
    databaseURL: "https://notes-synced.firebaseio.com", 
    projectId: "notes-synced",
    storageBucket: "notes-synced.appspot.com",
    messagingSenderId: "221507720871",
    appId: "1:221507720871:web:3de56f80c319d68a6f2d82",
    measurementId: "G-BEFJVGV5KK"
};

firebase.initializeApp(firebaseConfig);

app.use(require('cors')());

const isEmpty = str => str.trim() === '';

const isEmail = email => email.match(
    /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
);

const FBAuth = (request, response, next) => { // decode the token and get data from db

    let idToken;
    const noImg = 'defaultProfilePicture.png'

    if(request.headers.authorization && request.headers.authorization.startsWith('Bearer ')) {
        idToken = request.headers.authorization.split('Bearer ')[1];

    } else {
        console.error('No token found');
        return response.status(403).json({error: 'Unauthorized'});
    }

    admin.auth().verifyIdToken(idToken)
        .then(decodedToken => { 
            request.user = decodedToken;
            return db.collection('users').where('userId', '==', request.user.uid).limit(1).get();
        })
        .then(data => {
            try {
                request.user.handle = data.docs[0].data().handle; // data() extracts data from doc[]
                request.user.image = data.docs[0].data().image; // why not do this in one line?
                request.user.notes = data.docs[0].data().notes;
                request.user.email = data.docs[0].data().email;

            } catch { // user has no db entry(android), create a new one
            
                let userId = request.user['uid'];
                request.user = {
                    name: request.user.name.replace(/ /g, ""),
                    handle: userId,
                    notes: "",
                    image: `https://firebasestorage.googleapis.com/v0/b/${firebaseConfig.storageBucket}/o/${noImg}?alt=media`,
                    email: request.user.firebase.identities.email[0],
                    userId
                }

                db.doc(`/users/${userId}`).set(request.user);
            }

            return next();
        })
        .catch(e => {
            console.error('Error while verifying token ', e);
            response.status(403).json(e);
        });
}

const validateInput = (user, type) => { // validating function for signin/ signup

    let exceptions = {};

    if(type === 'signUp') {

        isEmpty(user.email) 
            ? exceptions.email = 'Email must not be empty.'
            : !isEmail(user.email) 
                ? exceptions.email = 'Must be a valid email adress.'
                : 1

        isEmpty(user.password) 
            ? exceptions.password = 'Password must not be empty.'
            : 1

        isEmpty(user.confirmPassword) 
            ? exceptions.confirmPassword = 'This field must not be empty.'
            : user.password !== user.confirmPassword 
                ? exceptions.confirmPassword = 'Passwords are not the same.'
                : 1

        isEmpty(user.handle) 
            ? exceptions.handle = 'Username must not be empty.'
            : 1

        return {
            exceptions,
            valid: Object.keys(exceptions).length === 0
        }
        
    } else if(type === 'signIn') {

        isEmpty(user.email) 
            ? exceptions.email = 'Email must not be empty.'
            : isEmpty(user.password) 
                ? exceptions.password = 'Password must not be empty.'
                : 1

        return {
            exceptions,
            valid: Object.keys(exceptions).length === 0
        }
    }
}

const signUp = (request, response) => { // sign up a new user

    const newUser = {
        email: request.body.email,
        password: request.body.password,
        confirmPassword: request.body.confirmPassword,
        handle: request.body.handle
    }
    
    let { valid, exceptions } = validateInput(newUser, 'signUp');
    if(!valid) return response.status(400).json(exceptions);

    const noImg = 'defaultProfilePicture.png'
    let userId;

    db.doc(`/users/${newUser.handle}`).get()
        .then(doc => { 

            if(doc.exists) {
                return response.status(400).json({handle: 'this handle is already taken'});

            } else {
                firebase.auth().createUserWithEmailAndPassword(newUser.email, newUser.password) // create user
                    .then(data => { // WHEN RETURNING HANDLE IN USE THIS IS PERFORMED ANYWAY
                        userId = data.user.uid;
                        return data.user.getIdToken();
                    })
                    .then (token => { 
                        const userCredentials = {
                            handle: newUser.handle,
                            email: newUser.email,
                            notes: "",
                            image: `https://firebasestorage.googleapis.com/v0/b/${firebaseConfig.storageBucket}/o/${noImg}?alt=media`,
                            userId
                        };
                        db.doc(`/users/${newUser.handle}`).set(userCredentials); // add db entry

                        return response.status(201).json({ token });
                    })
                    .catch(e => {

                        console.error(e);

                        if(e.code === 'auth/email-already-in-use') {
                            return response.status(400).json({email: 'Email is already in use' });

                        } else {
                            return response.status(500).json({ general: `Something went wrong. ${e} was thrown.` });
                        }
                    });
            }
        });
}

const signIn = (request, response) => { // sign in an existing user

    const user = {
        email: request.body.email,
        password: request.body.password
    }

    let { valid, exceptions } = validateInput(user, 'signIn');
    if(!valid) return response.status(400).json(exceptions);
	
    firebase.auth().signInWithEmailAndPassword(user.email, user.password) // sign user in
        .then(data => {
            return data.user.getIdToken(); // get token
        })
        .then(token => {
            return response.json({ token }); // return the token
        })
        .catch(e => {
            console.error(e);
            if(e.code == 'auth/invalid-email') return response.status(403).json({ email: 'This seems to be an invalid email, please try again.'}); // most common error codes
            if(e.code == 'auth/user-not-found') return response.status(403).json({ email: 'An Account with this email does not exist.'});
            return response.status(403).json({ password: 'Wrong password, please try again.'});
        });
}

const pullData = (request, response) => { // get the db entry of a user

    db.doc(`/users/${request.user.handle}`).get()
        .then(doc => { // cant i user data in request.user?
            return response.status(201).json(JSON.stringify(doc.data().notes).replace(/{},/g, '')); // why the hell does firebase add an empty object as of node >8 ???
        })
        .catch(e => {
            console.error(e);
            return response.status(500).json({ error: e.code });
        });
}

const update = (request, response) => { // update properties of the user

    if(request.body.image) { // if an image was supplied
        const busBoy = new require('busboy')({ headers: request.headers });
        let image = {};
    
        busBoy.on('file', (fieldname, file, filename, encoding, mimetype) => {
            if(!mimetype.includes('image')) return response.status(400).json({ error: 'wrong file type submitted' });
            
            const imageExtension = filename.split('.')[filename.split('.').length - 1];
            image.filename = `${Math.round(Math.random()*100000000000)}.${imageExtension}`; 
            
            image.filepath = require('path').join(require('os').tmpdir(), image.filename);
            image.mimetype = mimetype;

            file.pipe(require('fs').createWriteStream(image.filepath)); // create file
        })
        .catch(e => {
            console.error(e);
            return response.status(500).json({ error: e.code });
        });
    
        busBoy.on('finish', () => { // upload the file
            storage.bucket().upload(image.filepath, {
                resumable: false, 
                destination: `ProfilePictures/${image.filename}`,
                metadata: {
                    metadata: {
                        firebaseStorageDownloadTokens: Math.round(Math.random()*100000000000),
                        contentType: image.mimetype
                    }
                }
            })
            .then(() => {
                return db.doc(`/users/${request.user.handle}`).update({ // add a db reference to the uploaded file
                    image: `https://firebasestorage.googleapis.com/v0/b/${firebaseConfig.storageBucket}/o/ProfilePictures%2F${image.filename}?alt=media` 
                });
            })
            .then(() => {
                if(!request.body.notes) return response.status(200).json({ message: "updated successfully" });
            })
            .catch(e => {
                console.error(e);
                return response.status(500).json({ error: e.code });
            });
        });
    
        busBoy.end(request.rawBody);
    }

    request.body.notes && db.doc(`/users/${request.user.handle}`) // if notes-array supplied
        .update({ // update the notes entry of a user
            notes: request.body.notes
        })
        .then(() => {
            return response.status(200).json({ message: "updated successfully" });
        })
        .catch(e => {
            console.error(e);
            return response.status(500).json({ error: e.code });
        });
}

app.post('/signup', signUp);
app.post('/signin', signIn);
app.post('/pullData', FBAuth, pullData);
app.post('/update', FBAuth, update);

exports.api = require('firebase-functions').region('europe-west1').https.onRequest(app);