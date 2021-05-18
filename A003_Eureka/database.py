#import pyodbc 
import DBConst as cte
import log

class DataBase():
    """Class to handle database functions and queries."""

    logger = log.logger

    # def getConnection(self):
    #     """Método para conectarse a la base."""
    #     try:
    #         conn = pyodbc.connect(cte.CONNECTION)
    #         cursor = conn.cursor()
    #         cursor.execute("SELECT @@version;")     # Execute query to check connectivity
    #         return conn
    #     except Exception as e:
    #         self.logger.error("Error - getConnection: {}".format(e))
    #         return False

    def saveRecord(self,rec,key):
        """Save a new record."""
        name = rec['name']                  # Get information
        last_name = rec['last_name']
        email = rec['email']
        
        # conn = self.getConnection()         # Connect to database
        
        # if conn != False:
        #     try:
        #         cursor = conn.cursor()        
        #         VALUES=[name,last_name,email,key]
        #         cursor.execute(cte.QRY_SAVE_NEW_RECORD, VALUES)
        #         conn.commit()
        #         conn.close()
        #         self.logger.info("Record has been succesfully loaded!")
        #     except Exception as e:
        #         self.logger.error("Error - Could not insert the record: {}".format(e))
        # else:
        #     self.logger.error("Error - Could not stablish connection.")


        ## Para pruebas ####################################
        if True: # para comentar la parte de DB
            """Saving information in txt file instead using database just for tests."""
            with open('archivo.txt','a') as file_BBDD:
                values=' '.join([name, last_name, email, key])
                file_BBDD.write(values+'\n')

    def getKey(self,key):
        """Get key from database."""
        # conn = self.getConnection()
        
        # if conn != False:
        #     try:
        #         cursor = conn.cursor()        
        #         cursor.execute(cte.QRY_GET_KEY,key)
        #         row = cursor.fetchall()
        #         conn.close()
        #         self.logger.info("Key found!")
        #         return True if row[0][0] is not "" else False
        #     except Exception as e:
        #         self.logger.error("Error - Could not found the key: {}".format(e))
        #         return False
        # else:
        #     self.logger.error("Error - Could not stablish connection.")
        #     #return False

            ## Para pruebas ####################################
        if True: # para comentar la parte de DB
            """Saving information in txt file instead using database just for tests."""
            with open('archivo.txt','r') as file_BBDD:
                for line in file_BBDD:
                    if key in line:
                        return True
            return False

    def checkUser(self, rec):
        """Check if user exist in database."""
        # conn = self.getConnection()
        
        # if conn != False:
        #     try:
        #         cursor = conn.cursor()        
        #         cursor.execute(cte.QRY_CHECK_USER,rec['email']) # Check the user with his email
        #         row = cursor.fetchall()
        #         conn.close()
        #         return True if row[0][0] is not "" else False
        #     except Exception as e:
        #         self.logger.error("Error - Could not found the key: {}".format(e))
        #         return False
        # else:
        #     self.logger.error("Error - Could not stablish connection.")
        #     #return False

            ## Para pruebas ####################################
        if True: # para comentar la parte de DB
            """Saving information in txt file instead using database just for tests."""
            with open('archivo.txt','r') as file_BBDD:
                for line in file_BBDD:
                    if rec['email'] in line:
                        return True
            return False

    def returnKey(self, rec):
        """Get the key for the user."""
        # conn = self.getConnection()
        
        # if conn != False:
        #     try:
        #         cursor = conn.cursor()        
        #         cursor.execute(cte.QRY_RETURN_KEY,rec['email']) # Check the user with his email
        #         row = cursor.fetchall()
        #         conn.close()
        #         return row[0][0]
        #     except Exception as e:
        #         self.logger.error("Error - Could not found the key: {}".format(e))
        #         return False
        # else:
        #     self.logger.error("Error - Could not stablish connection.")
        #     #return False

            ## Para pruebas ####################################
        if True: # para comentar la parte de DB
            """Saving information in txt file instead using database just for tests."""
            with open('archivo.txt','r') as file_BBDD:
                for line in file_BBDD:
                    if rec['email'] in line:
                        return line.split(' ')[-1]
            return False