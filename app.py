from flask import Flask, render_template, flash, redirect, url_for, session, request, logging, session
from data import Articles
import sqlite3
from wtforms import Form, StringField, TextAreaField, PasswordField, validators
from functools import wraps
import time
import datetime
import random

app = Flask(__name__)
Articles = Articles()

@app.route('/')
def index():
    return render_template('home.html')

@app.route('/about')
def about():
    return render_template('about.html')

@app.route('/articles')
def articles():
    conn = sqlite3.connect('tutorial.db')
    c = conn.cursor()

    c.execute("SELECT * FROM articles")
    articles = c.fetchall()

    c.close()
    conn.close()

    if articles is not None:
        return render_template('articles.html', articles=articles)
    else:
        msg = 'No Articles Found'
        return render_template('articles.html', msg=msg)

@app.route('/article/<string:id>/')
def article(id):
    conn = sqlite3.connect('tutorial.db')
    c = conn.cursor()

    c.execute("SELECT * FROM articles where id = ?", (id,))
    articles = c.fetchone()

    c.close()
    conn.close()

    return render_template('article.html', articles=articles)


def is_logged_in(f):
    @wraps(f)
    def wrap(*args, **kwargs):
        if 'logged_in' in session:
            return f(*args, **kwargs)
        else:
            flash('Unauthorized, Please login', 'danger')
            return redirect(url_for('login'))
    return wrap


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
    conn = sqlite3.connect('tutorial.db')
    c = conn.cursor()

    c.execute("SELECT * FROM articles")
    articles = c.fetchall()
    print(articles)

    c.close()
    conn.close()

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
        conn = sqlite3.connect('tutorial.db')
        c = conn.cursor()
        c.execute("INSERT INTO articles (title, author, body) VALUES (?, ?, ?)",
                  (title, author, body))
        conn.commit()
        c.close()
        conn.close()

        flash('Article Created', 'success')

        return redirect(url_for('dashboard'))

    return render_template('add_article.html', form=form)


if __name__ == '__main__':
    app.secret_key = 'super secret key'
    app.run(debug=True)

