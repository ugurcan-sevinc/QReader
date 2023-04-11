# QReader

QReader is an Android application that allows users to scan QR codes using their device's camera or by selecting an image from their gallery. The app uses a QR code scanning library (such as ZXing) to decode QR codes from images. Decoded QR code contents are stored in a Room Database, allowing users to view their scanned links even after closing the app.

## Features

- QR code scanning: Users can use their device's camera to scan QR codes in real-time. The app captures the camera preview frames, decodes the QR codes from the frames, and displays the decoded results to the user.
- Image-based QR code scanning: Users can also select an image from their gallery and the app will decode QR codes from the selected image. The image is first converted to a byte array, then to a bitmap, and finally decoded using a QR code scanning library.
- Result handling: Once a QR code is successfully decoded, the app handles the decoded result by displaying it to the user, storing it in a Room Database for later retrieval, and showing a result dialog with the decoded contents.
- Old links storage: The app uses a Room Database to store previously scanned links, allowing users to view their scanned links even after closing the app. The ViewModel (`linkVM`) is used to communicate with the Room Database.
- Error handling: If no QR code is found in the image or if the decoding process fails, the app displays an error message to the user using a Toast.

## Code

The code for QReader includes a `processImageForQrCode()` method that takes a bitmap image as input, converts it to a byte array, decodes it using a QR code scanning library, and handles the decoded result by storing it in the Room Database and displaying it to the user. The code also uses a ViewModel (`linkVM`) to communicate with the Room Database and store/retrieve the decoded results, and a `showResultDialog()` function to display the decoded contents in a dialog to the user.


## Screenshot

![screenshot](https://user-images.githubusercontent.com/69902076/231161162-99adcb64-dbef-46f5-adf4-03f0f382c1b7.jpg)
