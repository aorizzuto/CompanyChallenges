import log

class Messages():
    logger = log.logger

    def POSTReturnError(self,keys):
        text = "Parameters to get a new key should be {}\n".format(keys)
        self.logger.error(text)
        return text

    def POSTReturnSuccess(self,key):
        text = "Your KEY is: {}\n".format(key)
        self.logger.info(text)
        return text

    def GETReturnError(self,keys):
        text = "Error: Please specify these fields {}.\n".format(keys)
        self.logger.error(text)
        return text

    def GETReturnErrorInKey(self):
        text = "Error: Key not found.\n"
        self.logger.error(text)
        return text

    def POSTReturnUserExist(self, key):
        text = "User already exist!. Your key is: {}\n".format(key)
        self.logger.error(text)
        return text