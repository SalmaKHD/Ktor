package db

import org.ktorm.database.Database

object DatabaseConnection {
    // connect to database
    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/notes", // 3306: port number, notes: database name,
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "1234" // should typically be retrieved from a config file stored on the server
    )
}