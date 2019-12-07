#imports
import RaspberryPi1
import unittest
import json
import sqlite3


#Class made to run test functions using python version of unit test
class TestDatabaseCommands(unittest.TestCase):
    
        
    def test_insertMessage(self):
        """test_insertMessage tests if messages are properly added to the database
        :param self: Instance of class
        :return: If method works or not
        """

        try:
            testdb = RaspberryPi1.DB()
            
            conn = testdb.server()
            cur = conn.cursor()
            testdb.create_table(conn)
            
            p = testdb.parsejson()
           
            testdb.addMessageInfo(p)
            #gets all messages from database
            r = testdb.getAllMessages(conn)
            
            q = [("jsonTest",0,5)]
            self.assertEqual(r,q)
            
        except sqlite3.Error as error:
            
             print("Failed to insert record from sqlite table",error)
        finally:
            if(conn):
                testdb.deleteTable(conn)
                conn.close()
                print("\nConnection Close")
        
    def test_getNotSent(self):
        """test_getNotSent tests if messages are properly creating a not sent list from the database
        :param self: Instance of class
        :return: If method works or not
        """
        try:
            testdb = RaspberryPi1.DB()
            
            conn = testdb.server()
            cur = conn.cursor()
            testdb.create_table(conn)
            testdb.addMessageInfo(('test',0,0))
            p= testdb.getNotSent(conn)
            
            r = [("test",0,0)]
            
            self.assertEqual(p,r)
            
        except sqlite3.Error as error:
                
                print("Error: ",error)
        finally:
            if(conn):
                testdb.deleteTable(conn)
                conn.close()
                print("\nConnection Close")
                
    
    def test_UpdateSentStatus(self):
        """test_UpdateSentStatus tests if messages are updated properly to the database
        :param self: Instance of class
        :return: If method works or not
        """
        try:
            testdb = RaspberryPi1.DB()
            
            conn = testdb.server()
            cur = conn.cursor()
            testdb.create_table(conn)
            testdb.addMessageInfo(('test',0,0))
            
            testdb.create_table(conn)
            testdb.addMessageInfo(('test',0,1))
            
            r = testdb.getAllMessages(conn)
            
            
            testdb.updateSentStatus(conn,0)
            
            p = testdb.getSentList(conn)
            
            
            a = testdb.getNotSent(conn)
            
            
            self.assertEqual(p,([('test',1,0)]))
            
        except sqlite3.Error as error:
                print("Error :",error)
        finally:
            if(conn):
                testdb.deleteTable(conn)
                conn.close()
                print("\nConnection Close")
                
        
        
       
                    
    def test_parseText(self):
        """test_parseText tests if messages are being converted into
         proper braille format
        :param self: Instance of class
        :return: If method works or not
        """

        try:
            testdb = RaspberryPi1.DB()
            
            val = testdb.parseText('test')
            
            
            val1 = testdb.parseText('123')
          
            
            val2 = testdb.parseText('test123')
            
            
            val3 = testdb.parseText('test123test')
            
            self.assertEqual(val,(['t', 'e', 's', 't']))
            self.assertEqual(val1,(['#', 'a', 'b', 'c']))
            self.assertEqual(val2,(['t', 'e', 's', 't', '#', 'a', 'b', 'c']))
            self.assertEqual(val3,(['t', 'e', 's', 't', '#', 'a', 'b', 'c', ';', 't', 'e', 's', 't']))
            
            
        except sqlite3.Error as error:
            print("Error :",error)
        finally:
                print("\n Conversion Done")
            
#Used to run the code
if __name__ == '__main__':
    unittest.main()        


