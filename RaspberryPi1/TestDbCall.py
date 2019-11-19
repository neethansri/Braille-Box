
import RaspberryPi1
import unittest



class TestDatabaseCommands(unittest.TestCase):
   # testdb = RaspberryPi1.DB()
    
    def test_getNotSent(self):
        
        testdb = RaspberryPi1.DB()
        
        conn = testdb.server()
        p = testdb.getNotSent(conn)
        r=[('test5',0,12)]
        self.assertEqual(p,r)
    
    def test_insertMessage(self):
        
        testdb = RaspberryPi1.db()
        
        conn = testdb.server()
        p = testdb.addMessageInfo()
  
        
    
    def main():
        test_getNotSent()
         
#if__name__=='__main__'
unittest.main()
        
        
        


