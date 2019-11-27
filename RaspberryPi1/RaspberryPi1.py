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
        
         # Creates the message_Info table with the following columns"""
        sql = ''' CREATE TABLE IF NOT EXISTS message_Info (
                    message text NOT NULL,
                    messageSent boolean NOT NULL,
                    messageId integer NOT NULL PRIMARY KEY
                    ); '''
        # Creates the message_Container table with the following columns"""
        sql2 = ''' CREATE TABLE IF NOT EXISTS message_Container (
                     lengthOfMessage integer NOT NULL,
                     receivedTimeStamp Date NOT NULL,
                     messageId integer NOT NULL,
                     firstName TEXT NOT NULL,
                     lastName TEXT NOT NULL,
                     FOREIGN KEY(messageId) REFERENCES message_Info (messageId)
                     );'''
        # Creates the message_Info table with the following columns"""
        #sql3 = ''' CREATE TABLE IF NOT EXISTS User_Info (
                    #firstName text NOT NULL,
                    #lastName text NOT NULL,
                    #userId integer NOT NULL,
                    #FOREIGN KEY (userId) REFERENCES message_Container(userId)
                    #)
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
        # sql statements to be executed to find max value in messageId column"""
        sql = ''' SELECT MAX(messageId) FROM message_Info  '''
        cur = connection.cursor()
        cur.execute(sql)
        connection.commit()
        
        idValue = cur.fetchone()[0]
        print(idValue)
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
        connection.close()
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
        connection.close()
        

   

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
        connection.close()
        
    @staticmethod
    def getSentList(conn):
        
        curs = conn.cursor()
        curs.execute("SELECT * FROM message_Info WHERE messageSent = '1'")
        rows = curs.fetchall()
        return rows
    
    @staticmethod
    def getAllMessages(conn):   
        curs = conn.cursor()
        sql = (''' SELECT*FROM message_Info ''')
        
        curs.execute(sql)
        rows = curs.fetchall()
        return rows

    @staticmethod
    def getLengthOfMessage(msg):
        
        length = len(msg)
        return length
    
    @staticmethod
    def deleteTable(conn):
        sql = ''' DROP TABLE message_Info '''
        cur = conn.cursor()
        cur.execute(sql)
        conn.commit()
        
    
    port = 1001
    hostAddress = ''


    def receiveMessage():
        #change later
        
        global hostAddress
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        server_address = ('', port)
        s.bind(server_address)
        print('Receiving message')
        buf, hostAddress = s.recvfrom(port)
        print(hostAddress)
        rawStr = str(buf)
        message = rawStr[2:len(rawStr)-1]
        print(message)
        s.shutdown(1)
        return message 



    def sendResult(self,result):
        # change later
        
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        global hostAddress
        print(hostAddress)
        data = str(result)
        s.sendto(data.encode('utf-8'), hostAddress)
        
    @staticmethod
    def parseText(message):
        message = message.lower()
        formattedArray = list()
        numToggle = False

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
            
        
        return formattedArray

def main():
        foo = DB()
        conn = foo.server()
        cur = conn.cursor()
        while(True):
            filepath = 'parse.txt'
            data = None
            with open(filepath) as fp:
                for line in fp:
                        a,b,c,d,e = line.split(',')
                        firstName = a
                        lastName = b
                        ipAddress = c
                        port = d
                        msg = e
             
                        packet = (firstName,lastName,ipAddress,port,msg)
                        
                        idV = foo.messageIdGenerator(conn)
                        print(idV)
                        messagedb = (packet[4],0,idV)
                        time = datetime.datetime.now().strftime("c")
                        foo.addMessageInfo(messagedb)
                        lengthMsg = foo.getLengthOfMessage(packet[4])
                        
                        
                        foo.addMessageContainer((lengthMsg,time,idV,packet[0],packet[1]))
                       
                        result = foo.parseText(packet[4])
                        foo.sendResult(result)
                        fp.close()
            
            
                
            
if __name__=='__main__':
        main()




 