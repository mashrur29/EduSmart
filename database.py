from pymongo import MongoClient

# Database Connection Online
MONGODB_URI = "mongodb://mashrur29:m1234567@ds239412.mlab.com:39412/onlineforum"
client = MongoClient(MONGODB_URI, connectTimeoutMS=30000)
db = client.get_database("onlineforum")
