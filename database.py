

import pyrebase

config =  {
    "apiKey": "AIzaSyChb3i7w5Z3-Dzbwe4GLo61zFPUlIdXgsE",
    "authDomain": "iamcool-4a1fe.firebaseapp.com",
    "databaseURL": "https://iamcool-4a1fe.firebaseio.com",
    "projectId": "iamcool-4a1fe",
    "storageBucket": "iamcool-4a1fe.appspot.com",
    "messagingSenderId": "449742770637"
}

config_new = {
    "apiKey": "AIzaSyC6h6r6k-MAnqoMgsNZklseV_n73wApJlI",
    "authDomain": "edusmart-8a0e7.firebaseapp.com",
    "databaseURL": "https://edusmart-8a0e7.firebaseio.com",
    "projectId": "edusmart-8a0e7",
    "storageBucket": "edusmart-8a0e7.appspot.com",
    "messagingSenderId": "272003538744"
}

firebase = pyrebase.initialize_app(config_new)