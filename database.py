import sqlite3
import time
import datetime
import random

conn = sqlite3.connect('tutorial.db')
c = conn.cursor()

def create_table():
    # c.execute("CREATE TABLE users(name TEXT, Username TEXT, Email TEXT, password TEXT)")
    c.execute("CREATE TABLE articles(id INTEGER PRIMARY KEY AUTOINCREMENT, title varchar(255), author varchar(100), body TEXT, create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)")

create_table()

c.close()
conn.close()