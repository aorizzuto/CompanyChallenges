"""Clase creada para generar la URL a usar."""

import database, log

class Parametros():
    """Class to handle parameters and apis."""

    logger = log.logger                                     # Object Logger
    WS_ALPHA = "www.alphavantage.co/query"                  # API where info will be requested
    GET_KEYS=['function','symbol','outputsize','apikey']    # Keys are constant
    POST_KEYS=['name','last_name','email']                  # User info
    #PARAM = {'function':'TIME_SERIES_DAILY','symbol':'FB','outputsize':'compact','apikey':'X86NOH6II01P7R24'} # Ejemplo

    def __init__(self):
        """Class constructor."""
        self.logger.info("Object Parametros has been created.")
        pass

    def createURL(self, param):
        """Creation of URL to get information."""
        URL = "https://" + self.WS_ALPHA + "?" + '&'.join([list(param.keys())[i]+'='+list(param.values())[i] for i in range(len(param))])
        self.logger.info("createURL method called.")
        return URL

    def saveParameters(self, records, key):
        """Aquí deberíamos guardar la información del usuario en base de datos junto con la key."""
        self.logger.info("saveParameters method called.")
        db = database.DataBase()
        db.saveRecord(rec=records,key=key)
    
    def checkKey(self,key):
        """Check if key exist."""
        self.logger.info("checkKey method called.")
        db = database.DataBase()
        return db.getKey(key)
    
    def checkParameters(self,dct,keys):
        """Check if parameters are fine."""
        self.logger.info("checkParameters method called.")
        return all([x in dct.keys() for x in keys])       # Check if expected parmeters are passed

    def existUser(self,rec):
        """Check if user exist already."""
        self.logger.info("existUser method called.")
        db = database.DataBase()
        return db.checkUser(rec)