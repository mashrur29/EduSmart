import ast
import requests
from bson import ObjectId, Binary
from flask import Flask, render_template, flash, redirect, url_for, session, request, logging, session, json
from wtforms import Form, StringField, TextAreaField, PasswordField, validators
from functools import wraps
from database import db
import classes.statistics as stat
import classes.StatisticsStd as stdev
import time
import datetime
import random
from helpers import *
import emoji

app = Flask(__name__, template_folder='Templates')
app.secret_key = 'super secret key'
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
    projects = db.projects.find({"classCode": str(id)})
    return render_template('projects.html', classes=classes, projects=projects)


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
        name = form.name.data
        email = form.email.data
        username = form.username.data
        password = form.password.data
        db.users.insert({"name": name, "email": email, "username": username, "password": password})
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
        users = db.users.find({"username": str(Username)})


        for user in users:
            if user is not None:
                # Get stored hash
                password = str(user["password"])

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

@app.route('/download_file/<string:id>', methods=['POST', 'GET'])
def download_file(id):
    print('Downloading')
    file = db.projects.find({"_id": ObjectId(id)})
    f = open('download', 'w')
    str1 = 'download'
    f.write(str1)
    f.close()
    print('file downloaded')

    return redirect(url_for('dashboard'))

@app.route('/upload_file/<string:id>', methods=['POST', 'GET'])
def upload_file(id):
    file = request.files['inputfile']
    author = session['username']
    title = file.filename
    db.projects.insert({"title": title, "author": author, "fileBody": str(file), "date": str(now), "classCode": str(id)})
    classes = db.classes.find_one({"_id": ObjectId(id)})
    projects = db.projects.find({"classCode": str(id)})
    return render_template('projects.html', classes=classes, projects=projects)

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

@app.route('/jobs')
def jobs():
    jobs = db.jobs.find()

    if jobs is not None:
        return render_template('jobs.html', jobs=jobs)
    else:
        msg = 'Nobody is Interested :('
        return render_template('jobs.html', msg=msg)

@app.route('/delete_job/<string:id>', methods=['GET', 'POST'])
def delete_job(id):
    db.jobs.remove({"_id": ObjectId(id)})
    flash('Job Deleted', 'success')
    return redirect(url_for('jobs'))

class JobForm(Form):
    tutor = StringField('Tutor', [validators.Length(min=1, max=5000)])
    contact = StringField('Contact', [validators.Length(min=1, max=5000)])
    subject = StringField('Interested Subject', [validators.Length(min=1, max=5000)])
    compensation = StringField('Compensation', [validators.Length(min=1, max=5000)])

@app.route('/add_job', methods=['GET', 'POST'])
@is_logged_in
def add_job():
    form = JobForm(request.form)
    if request.method == 'POST' and form.validate():
        contact = form.contact.data
        subject = form.subject.data
        compensation = form.compensation.data
        tutor = form.tutor.data

        db.jobs.insert({"contact": contact, "subject": subject, "tutor": tutor, "compensation": compensation})

        flash('Job Created', 'success')

        return redirect(url_for('jobs'))

    return render_template('add_job.html', form=form)




##########################################################################################

languages = {"c": 1, "cpp": 2, "cpp14": 58, "java": 3,"mysql": 10,"python2": 5, "python3": 30,"text": 28}

@app.route('/online_judge', methods=['GET', 'POST'])
def online_judge():
    problems = db.problems.find()

    if problems is not None:
        return render_template('online_judge.html', problems=problems)
    else:
        msg = 'No Problems Available'
        return render_template('online_judge.html', msg=msg)

class ProblemForm(Form):
    name = StringField('Name', [validators.Length(min=1, max=5000)])
    body = TextAreaField('Body', [validators.Length(min=1)])
    input = TextAreaField('Input', [validators.Length(min=0)])
    output = TextAreaField('Output', [validators.Length(min=1)])

@app.route('/add_problem', methods=['GET', 'POST'])
def add_problem():
    form = ProblemForm(request.form)
    if request.method == 'POST' and form.validate():
        name = form.name.data
        body = form.body.data
        input = form.input.data
        output = form.output.data

        db.problems.insert({"name": name, "body": body, "input": input, "output": output})

        flash('Problem Created', 'success')

        return redirect(url_for('online_judge'))

    return render_template('add_problem.html', form=form)

@app.route('/problem/<string:id>/')
def problem(id):
    problem = db.problems.find_one({"_id": ObjectId(id)})

    print(problem['name'])
    return render_template('problem.html', problem=problem)

class SolveProblemForm(Form):
    code = TextAreaField('Source Code', [validators.Length(min=1)])
    lang = StringField('Source Language', [validators.Length(min=1)])


@app.route('/add_problem/<string:id>/', methods=['GET', 'POST'])
def solve_problem(id):
    form = SolveProblemForm(request.form)
    problem = db.problems.find_one({"_id": ObjectId(id)})
    if request.method == 'POST' and form.validate():
        code = form.code.data
        language = form.lang.data
        input_ = problem['input']
        if input_ == "":
            input_ = " "

        verdict = 'Wrong Answer'
        guest = hackerrank_api(code=code, language=language, input_=input_)
        print(guest['output'])
        if guest['result'] == "Successfully Executed":
            if(guest['output'].rstrip() == problem['output']):
                verdict = 'Accepted'
                flash('Accepted', 'success')
            else:
                flash('Wrong Answer', 'danger')
        else:
            flash('Compilation Error', 'warning')

        db.submission.insert({"name": problem['name'], "user": session['username'], "verdict": verdict})
        return redirect(url_for('online_judge'))

    return render_template('solve_problem.html', form=form, languages=languages)


def hackerrank_api(username=None, title=None, code=None, language=None, input_=None):
    language = int(language)
    s_lang = (list(languages.keys())[list(languages.values()).index(language)])
    print(s_lang)
    try:
        RUN_URL = 'https://api.jdoodle.com/v1/execute'
        headers = {'content-type': 'application/json'}
        data = {
            'script': code,
            'language': s_lang,
            'stdin': input_,
            'versionIndex': '0',
            'clientId': '5cd01283205107ec2f32a6380c097804',
            'clientSecret': '9c7bb43dab2f76c610a7ed85870d00d20754e9717a0e09bd3d1fc9cbd457165a',

        }

        r = requests.post(RUN_URL, data=json.dumps(data), headers=headers)
        response = r.json()
        output = response['output']
        result = "Compilation Error"
        time = None
        mem = None
        print(response)
        if output:
            message = response['statusCode']
            if message == 200:
                result = "Successfully Executed"
                output = response['output']
                time = response['cpuTime']
                mem = response['memory']
            elif message != "200":
                result = "Runtime Error"
                output = response['error']
    except Exception as e:
        print(e)
        result = "Unable to process your request. Please try again later. Sorry for inconvenience."
        output = None
        time = None
        mem = None
    O_IDE = {}
    O_IDE['code'] = code
    O_IDE['lang'] = int(language)
    O_IDE['input'] = input_
    O_IDE['output'] = output
    O_IDE['result'] = result
    O_IDE['time'] = time
    O_IDE['memory'] = mem
    if username is not None:
        # Authenticated User
        O_IDE['username'] = username
        O_IDE['title'] = title
    # print(O_IDE)
    return O_IDE

##########################################################################################

@app.route('/profile')
def profile():
    users = db.users.find({"username": str(session['username'])})
    curuser = None
    for user in users:
        curuser = user
        break
    submissions = db.submission.find({"user": str(session['username'])})
    return render_template('profile.html', user=curuser, submissions=submissions)


##########################################################################################

@app.route('/git')
def git_index():
	"""Index page"""
	return render_template("git_index.html")

@app.route('/git/about')
def git_about():
	"""About page"""
	return render_template("about.html")

@app.route('/git/profile', methods=["GET", "POST"])
def git_profile():
	"""Render profile according to request"""

	# Get username from post method
	user = request.form.get("username")
	#print user
	if not user:
		return redirect(url_for('index'))

	# Check if the request is given from post method or not
	if request.method == "POST":
		# Get all the data from github api
		basic = basic_retrive(user)
		# Check if anything is missing or not
		if not basic:
			return render_template("git_not_found.html")

		watch = watch_list(user)
		org = organizations(user)


		# If everything goes fine
		return render_template("git_profile.html", basic=basic, watch=watch, org=org, emoji=emoji)

	# If request method is get then redirect to
	else:
		return redirect(url_for('git_index'))

@app.errorhandler(404)
def git_page_not_found(e):
    """Return a custom 404 error."""
    return render_template("404.html"), 404



##########################################################################################

if __name__ == '__main__':
    app.run(debug=True)

