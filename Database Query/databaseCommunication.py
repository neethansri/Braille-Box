import sqlite3
import socket, sys, time

def server():
    connection = None
    try:
        connection = sqlite3.connect('messageDatabase.db')
    except Error as e:
        print(e)

    return connection


def getNotSent(connection):
    curs = connection.cursor()
    #print ("\nEntire Database contents:\n")
    for row in curs.execute("SELECT * FROM message_Info WHERE messageSent = '0'"):
        print (row)
    return


def messageIdGenerator(connection):
	sql = ''' SELECT messageId FROM message_Info 
		  WHERE messageId = (SELECT MAX(messageId) FROM message_Info '''
	curr = connection.cursor()
	cur.execute(sql)
	idValue = cur.fetchone()[0]
	idValue = idValue + 1
	return idValue


def addMessageInfo(connection, tableValue):

    sql = ''' INSERT INTO message_Info(message,messageSent,messageId)

            VALUES(?,?,?) '''
    cur = connection.cursor()
    cur.execute(sql, tableValue)
    return cur.lastrowid  # used for connecting tables


def addMessageContainer(connection, tableValue):
    sql = ''' INSERT INTO message_Container(lengthOfMessage,receivedTimeStamp,finishedTimeStamp,userId,messageId)

            VALUES(?,?,?,?,?) '''
    cur = connection.cursor()
    cur.execute(sql, tableValue)
    return cur.lastrowid  # used for connecting tables


def addUserInfo(connection, tableValue):
    sql = ''' INSERT INTO User_Info(firstName,lastName,userId)

            VALUES(?,?,?)'''
    cur = connection.cursor()
    cur.execute(sql, tableValue)
    return cur.lastrowid  # used for connecting tables


def updateSentStatus(connection, idValue):
    sql = ''' Update Message_Info
              set messageSent = 1
              where messageId = ?'''

    cur = connection.cursor()
    cur.execute(sql, (idValue,))


def getLengthOfMessage(connection, idValue):
	sql = ''' SELECT * FROM Message_Info
		  WHERE messageId = ? '''

	cur = connection.cursor()
	cur.execute(sql, (idValue,))
	strings = cur.fetchone()[0]
	length = len(strings)
	print(length)


port = 1001
hostAddress = ''


def receiveMessage():
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



def sendResult(result):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    global hostAddress
    print(hostAddress)
    data = str(result)
    s.sendto(data.encode('utf-8'), hostAddress)

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
    #conn = server()
    # with conn:
        #string = 'test5'
	#idValue = messageIdGenerator(conn)
        #message = (string,0,idValue)
        #length = len(string)
        #messageId = addMessageInfo(conn,message)

        #messageContainer = (length,0,0,1,messageId)

        #print("\nInserting stuff:\n")
        # addMessageContainer(conn,messageContainer)

        # getNotSent(conn)
        # updateSentStatus(conn,0)

	# getLengthOfMessage(conn,0)
    # conn.close()

    msg = receiveMessage()
    
    print(parseText(msg))
    sendResult(False)

main()
