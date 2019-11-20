
import RaspberryPi1
import unittest
import json
import sqlite3



class TestDatabaseCommands(unittest.TestCase):
    
        
    def test_insertMessage(self):
        try:
            testdb = RaspberryPi1.DB()
            
            conn = testdb.server()
            cur = conn.cursor()
            testdb.create_table(conn)
            
            p = testdb.parsejson()
            print(p)
            testdb.addMessageInfo(p)
            
            r = testdb.getAllMessages(conn)
            print(r)
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
        try:
            testdb = RaspberryPi1.DB()
            
            conn = testdb.server()
            cur = conn.cursor()
            testdb.create_table(conn)
            testdb.addMessageInfo(('test',0,0))
            p= testdb.getNotSent(conn)
            print(p)
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
        try:
            testdb = RaspberryPi1.DB()
            
            conn = testdb.server()
            cur = conn.cursor()
            testdb.create_table(conn)
            testdb.addMessageInfo(('test',0,0))
            
            testdb.create_table(conn)
            testdb.addMessageInfo(('test',0,1))
            
            r = testdb.getAllMessages(conn)
            print(r)
            
            testdb.updateSentStatus(conn,0)
            
            p = testdb.getSentList(conn)
            print(p)
            
            a = testdb.getNotSent(conn)
            print(a)
            
            self.assertEqual(p,([('test',1,0)]))
            
        except sqlite3.Error as error:
                print("Error :",error)
        finally:
            if(conn):
                testdb.deleteTable(conn)
                conn.close()
                print("\nConnection Close")
                
        
        
       
                    
    def test_parseText(self):
        try:
            testdb = RaspberryPi1.DB()
            
            val = testdb.parseText('test')
            print(val)
            
            val1 = testdb.parseText('123')
            print(val1)
            
            val2 = testdb.parseText('test123')
            print(val2)
            
            val3 = testdb.parseText('test123test')
            print(val3)
            
            self.assertEqual(val,(['t', 'e', 's', 't']))
            self.assertEqual(val1,(['#', 'a', 'b', 'c']))
            self.assertEqual(val2,(['t', 'e', 's', 't', '#', 'a', 'b', 'c']))
            self.assertEqual(val3,(['t', 'e', 's', 't', '#', 'a', 'b', 'c', ';', 't', 'e', 's', 't']))
            
            
        except sqlite3.Error as error:
            print("Error :",error)
        finally:
                print("\n Conversion Done")
            
   
if __name__ == '__main__':
    unittest.main()        


