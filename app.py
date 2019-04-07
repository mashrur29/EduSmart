import ast

from bson import ObjectId
from flask import Flask, render_template, flash, redirect, url_for, session, request, logging, session
import sqlite3
from wtforms import Form, StringField, TextAreaField, PasswordField, validators
from functools import wraps
from database import db
import classes.statistics as stat
import classes.StatisticsStd as stdev
import time
import datetime
import random

app = Flask(__name__)

now = datetime.datetime.now()

@app.route('/')
def index():
    return render_template('home.html')

@app.route('/about')
def about():
    return render_template('about.html')

def is_logged_in(f):
    @wraps(f)
    def wrap(*args, **kwargs):
        if 'logged_in' in session:
            return f(*args, **kwargs)
        else:
            flash('Unauthorized, Please login', 'danger')
            return redirect(url_for('login'))
    return wrap

@app.route('/classroom')
def classroom():
    classes = db.classes.find()

    if classes is not None:
        return render_template('classes.html', classes=classes)
    else:
        msg = 'No Class Found'
        return render_template('classes.html', msg=msg)

class ClassroomForm(Form):
    title = StringField('Class Name', [validators.Length(min=1, max=200)])
    code = StringField('Class Instructor', [validators.Length(min=4)])

@app.route('/add_classroom', methods=['GET', 'POST'])
@is_logged_in
def add_classroom():
    form = ClassroomForm(request.form)
    if request.method == 'POST' and form.validate():
        title = form.title.data
        code = form.code.data
        instructor = session['username']

        db.classes.insert({"title": title, "code": code, "instructor": instructor})

        flash('Class Created', 'success')

        return redirect(url_for('classroom'))

    return render_template('add_classroom.html', form=form)

@app.route('/delete_classroom/<string:id>', methods=['GET', 'POST'])
def delete_classroom(id):
    db.classes.remove({"_id": ObjectId(id)})
    flash('Classroom Deleted', 'success')
    return redirect(url_for('classroom'))


@app.route('/view_project/<string:id>/')
def view_project(id):
    classes = db.classes.find_one({"_id": ObjectId(id)})
    return render_template('projects.html', classes=classes)


@app.route('/articles')
def articles():
    articles = db.article.find()

    if articles is not None:
        return render_template('articles.html', articles=articles)
    else:
        msg = 'No Articles Found'
        return render_template('articles.html', msg=msg)

@app.route('/article/<string:id>/')
def article(id):
    article = db.article.find_one({"_id": ObjectId(id)})

    print(article['title'])
    return render_template('article.html', article=article)


class RegisterForm(Form):
    name = StringField('Name', [validators.Length(min=1, max=50)])
    username = StringField('Username', [validators.Length(min=4, max=25)])
    email = StringField('Email', [validators.Length(min=6, max=50)])
    password = PasswordField('Password', [
        validators.DataRequired(),
        validators.EqualTo('confirm', message='Passwords do not match')
    ])
    confirm = PasswordField('Confirm Password')

@app.route('/register', methods=['GET', 'POST'])
def register():
    form = RegisterForm(request.form)
    if request.method == 'POST' and form.validate():
        conn = sqlite3.connect('tutorial.db')
        c = conn.cursor()
        name = form.name.data
        email = form.email.data
        username = form.username.data
        password = form.password.data
        c.execute("INSERT INTO users (name, Username, Email, password) VALUES (?, ?, ?, ?)",
                  (name, username, email, password))
        conn.commit()
        c.close()
        conn.close()
        flash('You are now registered and can log in', 'success')
        return redirect(url_for('index'))
    return render_template('register.html', form=form)


# Login
@app.route('/login', methods=['GET', 'POST'])
def login():
    print('Inside login')
    if request.method == 'POST':
        # getting form fields
        Username = request.form['username']
        password_candidate = request.form['password']
        conn = sqlite3.connect('tutorial.db')
        c = conn.cursor()

        c.execute("SELECT * FROM users WHERE Username = ?", (Username,))
        data = c.fetchone()
        # print(data)

        c.close()
        conn.close()

        if data is not None:
            # Get stored hash
            password = data[3]
            print('In')

            # Compare Passwords
            if password_candidate == password:
                # Passed
                session['logged_in'] = True
                session['username'] = Username
                flash('You are now logged in', 'success')
                return redirect(url_for('dashboard'))
            else:
                error = 'Invalid login'
                return render_template('login.html', error=error)

        else:
            error = 'Username not found'
            return render_template('login.html', error=error)

    return render_template('login.html')

@app.route('/logout')
@is_logged_in
def logout():
    session.clear()
    flash('Logout Successful', 'success')
    return redirect(url_for('login'))

@app.route('/dashboard')
@is_logged_in
def dashboard():
    articles = db.article.find()

    if articles is not None:
        return render_template('dashboard.html', articles=articles)
    else:
        msg = 'No Articles Found'
        return render_template('dashboard.html', msg=msg)


class ArticleForm(Form):
    title = StringField('Title', [validators.Length(min=1, max=200)])
    body = TextAreaField('Body', [validators.Length(min=4)])

@app.route('/add_article', methods=['GET', 'POST'])
@is_logged_in
def add_article():
    form = ArticleForm(request.form)
    if request.method == 'POST' and form.validate():
        title = form.title.data
        body = form.body.data
        author = session['username']

        db.article.insert({"title": title, "author": author, "body": body, "date": str(now)})

        flash('Article Created', 'success')

        return redirect(url_for('dashboard'))

    return render_template('add_article.html', form=form)

#######################################################################################

@app.route('/upload_file/<string:id>', methods=['POST', 'GET'])
def upload_file(id):

    print('lol', id)
    file = request.files['inputfile']
    return file.filename

#######################################################################################

@app.route("/calc/stat/entry", methods=['POST', 'GET'])
def stat_entry():
    return render_template(
        'stat_entry.html', **locals())


@app.route("/calc/stat/entry_data", methods=['POST', 'GET'])
def stat_entry_data():
    if request.method == 'POST':
        data = request.form
        data = {'stat_array' : data['stat_array']}
        posts = db.stat
        post_id = posts.insert_one(data).inserted_id
        return redirect('/calc/stat/' + str(post_id))
    return redirect('/calc/stat/entry_data')


@app.route('/calc/stat/<string:name>/')
def stat_simulation(name):
    data = db.stat.find_one({"_id": ObjectId(name)})
    arr = ast.literal_eval(data['stat_array'])
    arr_size = 0
    stat_array = []

    # 5, 12, 6, 8 , 14

    for i in range(100):
        stat_array.append(0)

    for i in range(len(arr)):
        stat_array[i] = int(arr[i])
        arr_size += 1

    s_temp = stdev.Statistics(int(arr_size), stat_array)
    data = s_temp.get_data()

    saved = 0
    if 'username' in session.keys():
        key = db.saved_simulation.find_one({'algo_id': name, "username": session['username'], "type": "Statistics",
                                            "algo": "Unordered"})
        if key is not None:
            saved = 1
    return render_template(
        'stat_output_stddev.html', **locals())



#######################################################################################

@app.route("/calc/stat/freq/entry", methods=['POST', 'GET'])
def stat_freq_entry():
    return render_template(
        'stat_entry.html', **locals())


@app.route("/calc/stat/freq/entry_data", methods=['POST', 'GET'])
def stat_freq_entry_data():
    if request.method == 'POST':
        data = request.form
        data = {'stat_array' : data['stat_array']}
        posts = db.stat
        post_id = posts.insert_one(data).inserted_id
        return redirect('/calc/stat/freq/' + str(post_id))
    return redirect('/calc/stat/freq/entry_data')


@app.route('/calc/stat/freq/<string:name>/')
def stat_freq_simulation(name):
    data = db.stat.find_one({"_id": ObjectId(name)})
    arr = ast.literal_eval(data['stat_array'])
    arr_size = 0
    stat_array = []

    for i in range(len(arr)):
        stat_array.append(0)

    for i in range(len(arr)):
        stat_array[i] = int(arr[i])
        arr_size += 1

    s_temp = stat.statistics(int(arr_size), stat_array)
    data = s_temp.get_data()
    # 26,30,45,89,89,74,54,74,26,30,30,26,78,89,54,56,14,54,14
    saved = 0
    if 'username' in session.keys():
        key = db.saved_simulation.find_one({'algo_id': name, "username": session['username'], "type": "Statistics",
                                            "algo": "Frequency"})
        if key is not None:
            saved = 1
    return render_template(
        'stat_output.html', **locals())


@app.route('/edit_article/<string:id>', methods=['GET', 'POST'])
def edit_article(id):
    articles = db.article.find_one({"_id": ObjectId(id)})
    form = ArticleForm(request.form)

    form.title.data = articles['title']
    form.body.data = articles['body']



    if request.method == 'POST' and form.validate():
        title = request.form['title']
        body = request.form['body']
        articles['title'] = title
        articles['body'] = body
        db.article.save(articles)

        flash('Article Updated', 'success')
        return redirect(url_for('dashboard'))
    return render_template('edit_article.html', form=form)

@app.route('/delete_article/<string:id>', methods=['GET', 'POST'])
def delete_article(id):
    db.article.remove({"_id": ObjectId(id)})
    flash('Article Deleted', 'success')
    return redirect(url_for('dashboard'))


#########################################################################################

if __name__ == '__main__':
    app.secret_key = 'super secret key'
    app.run(debug=True)

