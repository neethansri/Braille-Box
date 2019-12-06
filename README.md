# Braille_Box
A braille box using, 2 Raspberry Pi 4, Ardiuno, an Android App, and web based app and the ability to communicate with the deaf-blind
The software for this project uses both Java and Python for its various needs


# Running the code:

Android App: 

RaspberryPi1: To run code for the server, Sqlite 3 and Python 3 must already be installed on your computer. Clone the repository, and open the RaspberryPi1.py code into a IDE, choose an appropriate port number above 1024,compile the code and run. The server code will wait to receive messsages from the Android App.

RaspberryPi2: To run the code for the client pi (RPi2), begin by downloading the RaspberryPi2.java file as well as the BrailleDictionary.java file. These two files are to be included in a project together, as the RaspberryPi2 class uses the BrailleDictionary class. One must also download the two external libraries for serial communication (jSerialComm-2.5.3 and jSerialComm-2.5.2-sources) and load them as external libraries into the project. Once all of these preparations are complete, one can run the code from within the project IDE by simply running the main function from RaspberryPi2.java. Running the code will prompt the user to enter the port number to use for communication, which must match the output port number used by the server pi (RPi1). Once entered, the code will continue to run and endlessly send received messages to the Arduino for outputting.

Arduino:
