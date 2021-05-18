
# Parámetros Base de datos
BBDD_Driver    = "{SQL Server}"
BBDD_Server    = "DESKTOP-3OTJNGI"
BBDD_Database  = "EurekaLabs"
CONNECTION     = 'Driver=' + BBDD_Driver + ';Server=' + BBDD_Server + ';Database=' + BBDD_Database + ';Trusted_Connection=yes;'

# Tablas
TABLA_USERS = "Users"

# Queries
QRY_SAVE_NEW_RECORD = "insert into "+TABLA_USERS+" ('name','last_name','email','key') values (?,?,?,?)"
QRY_GET_KEY         = "select * from "+TABLA_USERS+" where key = ?"
QRY_CHECK_USER      = "select * from "+TABLA_USERS+" where email = ?"
QRY_RETURN_KEY      = "select key from "+TABLA_USERS+" where email = ?"