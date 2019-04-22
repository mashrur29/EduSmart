# This file has all the functions which will
# be used in retriving data from GitHub API.
try:
    import urllib.request as urllib2
except ImportError:
    import urllib2
import json
import os

try:
    # Getting client id and secret for extending rate limit
    secret = '?client_id=' + os.environ.get('CLID') + '&client_secret=' + os.environ.get('CLSEC')
except:
    secret = ''


# Basic retrival of data from
# https://api.github.com/users/user_name
def basic_retrive(user_name):
    # concatenate user name to create link
    link = "https://api.github.com/users/" + user_name + secret
    # empty list for collecting things I need
    box = []

    # Try to open link and if failed return None
    try:
        response = urllib2.urlopen(link)
        data = json.load(response)
        if not data:
            return None
    except:
        return None

    # Check if the link opened is user type
    if not data['type'] == "User":
        return None

    box = dict()
    # Collect everything in box and return box
    try:
        box['login'] = data['login']
        box['avatar_url'] = data['avatar_url']
        box['html_url'] = data['html_url']
        box['name'] = data['name']
        box['company'] = data['company']

        if (("http" not in data['blog']) and (data['blog'] !="")):
            data['blog'] = str("http://" + data['blog'])

        box['blog'] = data['blog']
        box['location'] = data['location']
        box['bio'] = data['bio']
        box['public_repos'] = data['public_repos']
        box['public_gists'] = data['public_gists']
        box['followers'] = data['followers']
        box['following'] = data['following']
        #print box
        return box
    except:
        return None


def watch_list(user_name):
    # concatenate user name to create link
    link = "https://api.github.com/users/" + user_name + "/subscriptions" + secret

    # empty list for collecting things I need
    box = []

    # Try to open link and if failed return None
    try:
        response = urllib2.urlopen(link)
        data = json.load(response)
        if not data:
            return None
    except:
        return None

    # pack the box with info we need
    for i in range(len(data)):
        box_feed = {}
        box_feed["name"] = data[i]["name"]
        box_feed["html_url"] = data[i]["html_url"]
        box.append(box_feed)

    return box


def organizations(user_name):
    # concatenate user name to create link
    link = "https://api.github.com/users/" + user_name + "/orgs" + secret

    # empty list for collecting things I need
    box = []

    # Try to open link and if failed return None
    try:
        response = urllib2.urlopen(link)
        data = json.load(response)
        if not data:
            return None
    except:
        return None

    # pack the box with info we need
    for i in range(len(data)):
        box_feed = {}
        box_feed["name"] = data[i]["login"]
        box_feed["url"] = "https://github.com/" + data[i]["login"]
        box_feed["icon"] = data[i]["avatar_url"]
        box.append(box_feed)

    return box
