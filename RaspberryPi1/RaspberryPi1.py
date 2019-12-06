import sqlite3
import json
import datetime
import socket, sys, time


class DB():
    @staticmethod
    def create_table(conn):
        """create tables for the database 
        :param conn: Connection object
        :return:
        """
        
         # Creates the message_Info table with the following columns
        sql = ''' CREATE TABLE IF NOT EXISTS message_Info (
                    message text NOT NULL,
                    messageSent boolean NOT NULL,
                    messageId integer NOT NULL PRIMARY KEY
                    ); '''
        # Creates the message_Container table with the following columns
        sql2 = ''' CREATE TABLE IF NOT EXISTS message_Container (
                     lengthOfMessage integer NOT NULL,
                     receivedTimeStamp Date NOT NULL,
                     messageId integer NOT NULL,
                     firstName TEXT NOT NULL,
                     lastName TEXT NOT NULL,
                     FOREIGN KEY(messageId) REFERENCES message_Info (messageId)
                     );'''
        
        # Try catch statement to execute the sql statements and catch any errors that may occur
        try:
            c = conn.cursor()
            c.execute(sql)
            conn.commit()
            c.execute(sql2)
            conn.commit()
            
            conn.close()
        except Error as e:
            print(e)

    @staticmethod
    def server():
        """Intialize sever for the database 
        :param: 
        :return: Connection Object
        """
        connection = None
        try:
            connection = sqlite3.connect('messageDatabase.db') # creates the connction object for the db
        except Error as e:
            print(e)

        return connection

    @staticmethod
    def getNotSent(connection):
        """Get the messages not sent and the create a list with these messages
        :param conn: Connection object
        :return a list of messageId
        """
        curs = connection.cursor()
        #print ("\nEntire Database contents:\n")
        curs.execute("SELECT * FROM message_Info WHERE messageSent = '0'")
        connection.commit()
        rows = curs.fetchall()
        return rows


    def messageIdGenerator(self,connection):
        """Generates id values for the database 
        :param conn: Connection object
        :return: Id value for new messages
        """
        # sql statements to be executed to find max value in messageId column
        sql = ''' SELECT MAX(messageId) FROM message_Info  '''
        cur = connection.cursor()
        cur.execute(sql)
        connection.commit()
        
        idValue = cur.fetchone()[0]
       
        if idValue is None:
            idValue = 1
        else:
            idValue = idValue + 1
        return idValue
    
    @staticmethod
    def parsejson():
        with open('data.json','r')as f:
            dataVals = json.load(f)
            
        for val in dataVals:
            ipaddress = val['IP Address']
            firstName = val['First Name']
            lastName = val['Last Name']
            message = val['Message Sent']
            port = val['Port Number']
            packet = (ipaddress,firstName,lastName,message,port)
        return packet
        
    @staticmethod
    def addMessageInfo(tableValue):
        """Add data to the columns in the message_Info table
        :param conn: Connection object
        :param tableValue: data to be put into the database
        :return: the last row value
        """
        connection = DB.server()
        sql = ''' INSERT INTO message_Info(message,messageSent,messageId)

                VALUES(?,?,?) '''
        cur = connection.cursor()
        cur.execute(sql, tableValue)
        connection.commit()
        #connection.close()
        print("Message was saved in database")
        
        

    @staticmethod
    def addMessageContainer(tableValue):
        """Add data to the columns in the message_Container table
        :param conn: Connection object
        :param tableValue: data to be put into the database
        :return: the last row value
        """
        connection = DB.server()
        sql = ''' INSERT INTO Message_Container(lengthOfMessage,receivedTimeStamp,messageId,firstName,lastName)

                VALUES(?,?,?,?,?) '''
        cur = connection.cursor()
        cur.execute(sql, tableValue)
        connection.commit()
        #connection.close()
        

   

    @staticmethod
    def updateSentStatus(connection, idValue):
        """Update boolean flag for specific messages in the message_Info table
        :param conn: Connection object
        :param idValue: data to be put into the database
        :return: 
        """
        sql = ''' Update Message_Info
                  set messageSent = 1
                  where messageId = ?'''

        cur = connection.cursor()
        cur.execute(sql, (idValue,))
        connection.commit()
        #connection.close()
        
     
    def getLengthOfMessage(self,msg):
        
        length = len(msg)
        return length
    
    
    def deleteTable(self,conn):
        """Used to delete tables without needing 
        to open the database file to manually delete them
        :param self: Instance of the class
        :param conn: connection object
        """
        sql = ''' DROP TABLE message_Info '''
        cur = conn.cursor()
        cur.execute(sql)
        conn.commit()
        
    
    def receiveMessage(self):
        """ Receiver function, allows us to receive messages 
        from the app.
        :param self: Instance of the class
        :return: String
        """
        
        
        hostAddress = '10.0.0.1'
        
        
        #Opens the socket and prepares to get inputs
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_address = ("172.20.10.2", 1111)
        print(server_address)       
        s.bind(server_address)
        s.listen(5)
        print('Receiving message')
        
        (client,address) = s.accept()
        print(client)
        
        #Gets the UDP packet
        buf = client.recv(100)
        print(buf)
        #Send the app a acknoledgement message
        data = ("Message Received")
        s.sendto(data.encode('utf-8'), hostAddress)
        #Converts the packet to a string
        rawStr = str(buf)
        #Return string
        return message 



    def sendResult(self,result):
        """ SendResult is a function that will send the 
        parsed message to the device using UDP
        :param self: Instance of the class
        :param result: a string representation of the result 
        """
        
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        hostAddress = ('172.20.10.4', 1112) 
        
        data = str(result)
        s.sendto(data.encode('utf-8'), hostAddress)
    


    
    def parseText(self,message):
        """parseText is the function that will convert the message into its 
        proper braille format which will allow the device to convert the message into 
        its corresponding braille.
        :param self: Instance of the class
        :param message: A String representation of the message
        :returns: String representaion of the braille format of the message
        """
        #Makes the whole message lower case
        message = message.lower()
        formattedArray = list()
        numToggle = False

        #Iterates through each charavter
        for char in message:

            num = ord(char)
            if(num>48 and num<58):
                if(numToggle == False):
                    numToggle = True
                    formattedArray.append('#')
                formattedArray.append(chr(num+48))
            elif(num == 48):
                formattedArray.append('#')
                numToggle = True
                formattedArray.append('j')
            else:
                if(numToggle == True):
                    numToggle = False
                    formattedArray.append(';')
                formattedArray.append(char)
        #Puts the character array back into a string
        formattedArray = ''.join(formattedArray)
        #returns the string
        return formattedArray

def main():
    #Defining a class variable to intialize connection for the database
        foo = DB()
        conn = foo.server()
        cur = conn.cursor()

    #Oldline used to check if the message is trying to be spammed
        oldLine = None

    #Opens the socket
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_address = ("", 5300)
        print(server_address)       
        s.bind(server_address)
        s.listen(5)
        
        #Continues to run waiting for more messages
        while(True):
            print('Receiving message')
            (client,address) = s.accept()

            #Gets app input 
            buf = client.recv(100)
            print(buf)
            
            #Convert to string
            rawStr = str(buf)
            
            #Check if previous message is a duplicate with the current one being sent
            if rawStr == oldLine:
                print("This is a duplicate message trying to be sent by the same user")
            else:
                #Splits the string into its designated parts and adds information accordingly to database
                oldLine = rawStr
                string = rawStr.split(',')   
                     
                firstName = string[0]
                lastName = string[1]
                 
                ipAddress = string[2]
                 
                port = string[3]
                 
                msg = string[4]
                 
                #Everything is tucked into a tuple
                packet = (firstName,lastName,ipAddress,port,msg)
                
                #Used to generate a unique id
                idV = foo.messageIdGenerator(conn)
               
                messagedb = (packet[4],0,idV)
                time = datetime.datetime.now()
                 
                foo.addMessageInfo(messagedb)
                lengthMsg = foo.getLengthOfMessage(packet[4])
                 
                 
                foo.addMessageContainer((lengthMsg,time,idV,packet[0],packet[1]))
                #Sends acknoledgement to the app saying the message was received
                data = ("Message Received")
                s.sendto(data.encode('utf-8'), hostAddress)
                #Updates the database with the status of the message was sent
                foo.updateSentStatus(conn,idV)
                #parses result
                result = foo.parseText(packet[4])
                #sends result
                foo.sendResult(result)
              
#Used to run main function      
if __name__=='__main__':
        main()




 