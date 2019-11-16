import sqlite3


def server():
    connection = None
    try:
        connection = sqlite3.connect('messageDatabase.db')
    except Error as e:
        print(e)
        
    return connection;

  
def getNotSent(connection):
    curs = connection.cursor()
    #print ("\nEntire Database contents:\n")
    for row in curs.execute("SELECT * FROM message_Info WHERE messageSent = '0'"):
        print (row)
    return;

def messageIdGenerator(connection)
	sql = ''' SELECT messageId FROM message_Info 
		  WHERE messageId = SELECT MAX(messageId) FROM message_Info '''
	curr  = connection.cursor()
	cur.execute(sql)
	idValue = cur.fetchone()[0]
	idValue = idValue + 1;
	return idValue

def addMessageInfo(connection,tableValue):
    
    sql = ''' INSERT INTO message_Info(message,messageSent,messageId)

            VALUES(?,?,?) '''
    cur = connection.cursor()
    cur.execute(sql,tableValue)
    return cur.lastrowid #used for connecting tables

def getMessage(connection, message):
	data = (message,0,messageIdGenerator(connection))
	addMessageInfo(connection, data)
	
def addMessageContainer(connection,tableValue):
    sql = ''' INSERT INTO message_Container(lengthOfMessage,receivedTimeStamp,finishedTimeStamp,userId,messageId)

            VALUES(?,?,?,?,?) '''
    cur = connection.cursor()
    cur.execute(sql,tableValue)
    return cur.lastrowid #used for connecting tables

def addUserInfo(connection,tableValue):
    sql = ''' INSERT INTO User_Info(firstName,lastName,userId)

            VALUES(?,?,?)'''
    cur = connection.cursor()
    cur.execute(sql,tableValue)
    return cur.lastrowid #used for connecting tables

def updateSentStatus(connection,idValue):
    sql = ''' Update Message_Info
              set messageSent = 1
              where messageId = ?'''
    
    cur = connection.cursor()
    cur.execute(sql,(idValue,))

def getLengthOfMessage(connection,idValue):
	sql = ''' SELECT * FROM Message_Info
		  WHERE messageId = ? '''

	cur = connection.cursor()
	cur.execute(sql,(idValue,))
	strings = cur.fetchone()[0]
	length = len(strings)
	print(length)
    
def main():
    conn = server()
    with conn:
        string = 'test5' #to be removed later and expect app input for the messages, used for test purposes atm
	#idValue = messageIdGenerator(conn)
        #message = (string,0,idValue)
	getMessage(connection,string)
        length = len(string)
        messageId = addMessageInfo(conn,message)
        
        messageContainer = (length,0,0,1,messageId)
        
        print("\nInserting stuff:\n")
        addMessageContainer(conn,messageContainer)
        
        
        #getNotSent(conn)
        updateSentStatus(conn,0)

	getLengthOfMessage(conn,0)
    conn.close()


main()
