import numpy as np
import collections
from sklearn.datasets import make_blobs
from sklearn.ensemble import BaggingClassifier, RandomForestClassifier
from sklearn.linear_model import SGDClassifier
from sklearn.metrics import accuracy_score, balanced_accuracy_score, average_precision_score, f1_score, precision_score, \
    recall_score
from sklearn.preprocessing import LabelEncoder
from sklearn.cluster import KMeans
from sklearn.naive_bayes import GaussianNB
from sklearn.neighbors import KNeighborsClassifier
from sklearn.neural_network import MLPClassifier
from sklearn.model_selection import ShuffleSplit, cross_val_score
from sklearn import preprocessing

from DataParser import SubjectGenerator
from sklearn import svm, tree

def TrainTest(seed,pTest,x):

    np.random.seed(seed)
    rs = ShuffleSplit(n_splits=1, test_size=pTest, random_state=0)

    idxTrain = None
    idxTest = None
    for idx_Train, idx_Test in rs.split(x):
        idxTrain = idx_Train
        idxTest = idx_Test
        # print("TRAIN:", idxTrain, "TEST:", idxTest)

    x_train = x[idxTrain]
    x_test = x[idxTest]

    return x_train,x_test

def Classification(infoX, dataY):

    print(np.shape(dataY))

    l = int((len(infoX) * 5) / 6)
    X = dataY[:l]
    y = list(map(int, infoX[:l, 1]))


    models = [
              # MLPClassifier(solver='lbfgs', alpha=1e-5,hidden_layer_sizes=(5,2), random_state=1),
              svm.LinearSVC(random_state=0, tol=1e-5),
              # KNeighborsClassifier(n_neighbors=3),
              # GaussianNB(),
              # svm.SVC(gamma='scale', decision_function_shape='ovo')
    ]


    # models = [svm.LinearSVC(random_state=1, tol=1e-5)]
    # model = svm.SVR()
    # model = svm.SVC(gamma='scale', decision_function_shape='ovo')
    # model = svm.SVC(kernel='rbf')
    # model = tree.DecisionTreeClassifier()
    # model = MLPClassifier(solver='lbfgs', alpha=1e-5,hidden_layer_sizes=(5,2), random_state=1)
    # model = SGDClassifier(loss="hinge", penalty="l2", max_iter=5)
    # model = KNeighborsClassifier(n_neighbors=3)
    # model = RandomForestClassifier(n_estimators=10)
    # model = GaussianNB()


    for model in models:
        # print(model)
        model.fit(X, y)
        return Accuracy(infoX,dataY,model,l)


def Accuracy(infoX,dataY,model,l):
    correct = 0
    incorrect = 0

    yTrue = []
    yPredict = []

    for i in range(l, len(dataY)):

        prediction = model.predict([dataY[i]])[0]
        expected = int(infoX[i][1])

        yTrue.append(expected)
        yPredict.append(prediction)

        # print(prediction,expected)

        if prediction == expected:
            correct += 1
        else:
            incorrect += 1

    # print()
    # print(correct)
    # print(incorrect)

    print()
    print('Accuracy ', ((correct * 100) / (correct + incorrect)))
    print()
    return yTrue,yPredict


def Clustering(X):

    for i in range(2,22):
        y_pred = KMeans(n_clusters=i, random_state=170).fit_predict(X)
        counter = collections.Counter(y_pred)
        print(counter)

def ModelValidation(y_true,y_pred):

    # clf = svm.SVC(gamma='scale', random_state=0)
    # crossValScore = cross_val_score(clf, data, list(map(int, info[:, 1])), scoring='recall_macro', cv=5)
    # print("Cross Validaion Score",crossValScore)

    s1 = accuracy_score(y_true, y_pred)
    print(s1)

    s2 = balanced_accuracy_score(y_true, y_pred)
    print(s2)

    s3 = f1_score(y_true, y_pred, average='weighted')
    print(s3)

    s4 = precision_score(y_true, y_pred, average='weighted')
    print(s4)

    s5 = recall_score(y_true, y_pred, average='weighted')
    print(s5)
    return s1,s2,s3,s4,s5

def main_method():

    oneToNine = 1
    subjects = SubjectGenerator(oneToNine)
    minL = 19750

    for i in range(oneToNine):
        subject = subjects[i]
        meta = subject.meta
        info = subject.info
        data = subject.data

        inf = []
        dat = []



        # for i in range(meta.ntrials):
        #     # if  info[i][0]=='animal' or info[i][0]=='manmade' or info[i][0]=='tool':
        #     if  info[i][0]=='animal' or info[i][0]=='vehicle':
        #     # if  info[i][2]=='cat' or info[i][2]=='bicycle':
        #     # if  int(info[i][1])<=7:
        #     # if True:
        #         inf.append(info[i])
        #         # dat.append(data[i,part])
        #         dat.append(data[i])
        # inf = np.array(inf)

        # Clustering(meta.colToCoord)

        # sel = VarianceThreshold(threshold=(.8 * (1 - .8)))
        # data = sel.fit_transform(data)
        # dat = sel.fit_transform(dat)

        binarizer = preprocessing.Binarizer().fit(data)
        data = binarizer.transform(data)





        # print(len(inf),len(data[0]))
        # Classification(inf,dat)
        t,p = Classification(info,data)

        print(t)
        print(p)

        return ModelValidation(t,p)

        # for i in range(333,333+5):
        #     part = []
        #     for x in range(meta.dimx):
        #         for y in range(meta.dimy):
        #             for z in range(meta.dimz):
        #                 if(meta.coordToCol[x][y][z]!=0):
        #                     # part.append(meta.coordToCol[x][y][z])
        #                     part.append([x,y,z,data[i][meta.coordToCol[x][y][z]-1]])
        #
        #     part =  np.array(part)
        #     part = part[::10]
        #     # visualize(part[:, 0], part[:, 1], part[:, 2], part[:, 3],i,0)
        #     # visualize(part[:, 0], part[:, 1], part[:, 2], part[:, 3],i,30)
        #     # visualize(part[:, 0], part[:, 1], part[:, 2], part[:, 3],i,45)
        #     # visualize(part[:, 0], part[:, 1], part[:, 2], part[:, 3],i,60)
        #     # visualize(part[:, 0], part[:, 1], part[:, 2], part[:, 3],i,90)
        #     # visualize(part[:, 0], part[:, 1], part[:, 2], part[:, 3],i,120)
        #     # visualize(part[:, 0], part[:, 1], part[:, 2], part[:, 3],i,135)
        #     # visualize(part[:, 0], part[:, 1], part[:, 2], part[:, 3],i,150)
        #     # visualize(part[:, 0], part[:, 1], part[:, 2], part[:, 3],i,180)
        #     visualize(part[:, 0], part[:, 1], part[:, 2], part[:, 3],str(i)+" "+info[i][0]+" "+info[i][2],-1)
        #     # visualize(part[:, 0], part[:, 1], part[:, 2], part[:, 3],str(i)+" "+info[i][0]+" "+info[i][2],45)
        #     # visualize(part[:, 0], part[:, 1], part[:, 2], part[:, 3],str(i)+" "+info[i][0]+" "+info[i][2],90)


        # visualize(meta.colToCoord[:,0],meta.colToCoord[:,1],meta.colToCoord[:,2],meta.colToCoord[:,1])
        # visualize([0,1,2,3,4],[0,1,2,3,4],[0,1,2,3,4],[1,1,100,90,100])

        # VisualizeInterective()


        # for p in part:
            # print(p)



    exit()



