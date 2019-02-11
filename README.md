# CheckAppUpdate
Library to check app update via firebase remote config

# Steps to use
1) Connect your app to firebase
2) Open Remote config in firebase and add keys to be used for checking update

![alt text](https://drive.google.com/open?id=1cKeW6IVaUERBdhaCryN6d7LCMlxB4zM2)

# Use in Project

**Add following line in you main gradle inside repository block**

maven {
    url  "https://dl.bintray.com/ankitahuja0508/CheckAppUpdate"
  }

**Add following line in project gradle for adding this as library**

implementation 'aexyn.com.checkappupdate:checkappupdate:1.0.0'

**And to use this library add below line in MainActivity of your project**

new CheckAppUpdate(this, BuildConfig.VERSION_CODE).checkVersion();
