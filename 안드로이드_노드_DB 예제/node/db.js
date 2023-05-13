const mysql = require("mysql2/promise");

const pool = mysql.createPool({
    user: "root",
    password: "0000",
    port: 3306,
    database: "android",
});

module.exports = pool;