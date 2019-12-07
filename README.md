# Braille_Box
A braille box using, 2 Raspberry Pi 4, Ardiuno, an Android App, and web based app and the ability to communicate with the deaf-blind
The software for this project uses both Java and Python for its various needs


# Running the code:

Android App: The BrailleBox folder contains the whole Android Studio folder. To edit/use the BrailleBox app, you need to download that folder and store it in the default directory "AndroidStudioProjects". This directory is where the projects are saved from Android Studio.
The other folder is called Receiver. This folder contains the Receiver Java file and class. When this receiver code runs with the appropriate port number that is inputted into the app. A communication between the app and the machine running the receiver code is established.

RaspberryPi1: To run code for the server, Sqlite 3 and Python 3 must already be installed on your computer. Clone the repository, and open the RaspberryPi1.py code into a IDE, choose an appropriate port number above 1024,compile the code and run. The server code will wait to receive messsages from the Android App.

RaspberryPi2: To run the code for the client pi (RPi2), begin by downloading the RaspberryPi2.java file as well as the BrailleDictionary.java file. These two files are to be included in a project together, as the RaspberryPi2 class uses the BrailleDictionary class. One must also download the two external libraries for serial communication (jSerialComm-2.5.3 and jSerialComm-2.5.2-sources) and load them as external libraries into the project. Once all of these preparations are complete, one can run the code from within the project IDE by simply running the main function from RaspberryPi2.java. Running the code will prompt the user to enter the port number to use for communication, which must match the output port number used by the server pi (RPi1). Once entered, the code will continue to run and endlessly send received messages to the Arduino for outputting.

Arduino:In the Arduino, the one file present is the ArduinoSerialCOMCode.
The ArduinoSerialCOMCode file contains the hardware and Arduino set up including the output and input pins. The file contains the code necessary to read a string from the serial port, split the string into characters and check every push or off solenoid state using digitalWrite. The file contains code that will read the input signal from both play and pause buttons. If button play or paused are pressed the Arduino will write into the serial port. 
Upload the described ArduinoSerialCOMCode to the Arduino and compile  and run the program. The code will be stored within the Arduino until it is overwritten by new a new uploaded code piece.

