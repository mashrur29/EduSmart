import scipy.io
import numpy as np


def SubjectGenerator(oneToNine):
    subjects = []
    for i in range(oneToNine):
        subjects.append(Subject(scipy.io.loadmat('E:\Projects\OnlineForum\datasets\data-science-P'+str(i+1)+'.mat')))
    return subjects

class Meta:
    def __init__(self, study, subject, ntrials, nvoxels, dimx, dimy, dimz, colToCoord, coordToCol):
        self.study = study
        self.subject = subject
        self.ntrials = ntrials
        self.nvoxels = nvoxels
        self.dimx = dimx
        self.dimy = dimy
        self.dimz = dimz
        self.colToCoord = colToCoord
        self.coordToCol = coordToCol

class InfoDictionary:
    def __init__(self, info):
        condNum = list(map(int, info[:, 1]))
        wordNum = list(map(int, info[:, 3]))
        word = info[:, 2]
        cond = info[:, 0]
        self.condition = {}
        self.word = {}
        self.conditionWord = {}

        for (k1, k2, v1, v2) in zip(condNum, wordNum, cond, word):
            self.condition.update({k1: v1})
            self.word.update({(k1, k2): v2})

        self.condition=dict(sorted(self.condition.items()))

        self.totalWords = []
        for key,cond in self.condition.items():
            wordList = []
            for wordNo in range(1,6):
                wordList.append(self.word[(key, wordNo)])
            self.conditionWord.update({cond:wordList})
            # wordList.insert(0,cond)
            self.totalWords.append(np.array(wordList))

        self.totalWords=np.array(self.totalWords)
        # print(self.totalWords)

        # for trial in info:
        #     print(trial)
            # print(trial[0],' ',trial[1],' ',trial[2],' ',trial[3],' ',trial[4])


        # print(type(i))
        # print(type(d))
        # print(i[:,0:2],' ',i[:,2:4])
        # i[:,0:1]
        # i[:,1:2]
        # x = i[:, 0],i[:, 2]
        # condition = dict(zip(condNum, cond))
        # c = Counter(condition_word)
        # print(len(c.keys()))
        # print(c.keys())
        # print(c.values())
        # print(type(cond))
        # print((cond))
        # print(type(condNumber))
        # print((condNumber))
        # for key,value in self.condition_word.items():
        #     print(key,' ',value)
        # print(condition[10])
        # print(self.condition_word[(10,1)])


class Subject:

    def __init__(self,mat):
        self.header = mat['__header__']
        self.version = mat['__version__']
        self.globals = mat['__globals__']
        self.meta = self.PrepareMeta(mat['meta'])
        self.info = self.PrepareInfo(mat['info'])
        self.data = self.PrepareData(mat['data'])
        self.infoDictionary = self.PrepareInfoDictionary(self.info)

        # print(self.header)
        # print()
        # print(self.version)
        # print()
        # print(self.globals)
        # print()
        # print(mat['meta'])
        # print()
        # print(self.info)
        # print()
        # print(self.data)

        # print(np.shape(mat['meta']))
        # print(np.shape(mat['info']))
        # print(np.shape(mat['data']))


    def PrepareData(self, data):

        dataArr = []
        for entry in data:
            dataArr.append(entry[0][0])

        for d in dataArr[:10]:

            dlist = ''
            for dd in d[:5]:
                dlist+=str(dd)+'           '
            # print(dlist)

        return np.array(dataArr)


    def PrepareInfoDictionary(self, preparedInfo):
        return InfoDictionary(np.array(preparedInfo))


    def PrepareInfo(self,info):
        infoArr = []
        for trailInfo in info[0]:
            cond = trailInfo[0][0]
            cond_number = trailInfo[1][0][0]
            word = trailInfo[2][0]
            word_number = trailInfo[3][0][0]
            epoch = trailInfo[4][0][0]

            singleTrialInfo = []
            singleTrialInfo.append(cond)
            singleTrialInfo.append(cond_number)
            singleTrialInfo.append(word)
            singleTrialInfo.append(word_number)
            singleTrialInfo.append(epoch)
            infoArr.append(singleTrialInfo)

        return np.array(infoArr)


    def PrepareMeta(self,meta):

        study = meta[0][0][0][0]                        #'science'
        subject = meta[0][0][1][0]                      #'P1'
        ntrials = meta[0][0][2][0][0]                   #360
        nvoxels = meta[0][0][3][0][0]                   #21764
        dimx = meta[0][0][4][0][0]                      #51
        dimy = meta[0][0][5][0][0]                      #61
        dimz = meta[0][0][6][0][0]                      #23
        colToCoord = np.array(meta[0][0][7])            #[21764x3 double]
        coordToCol = np.array(meta[0][0][8])            #[51x61x23 double]

        return Meta(study,subject,ntrials,nvoxels,dimx,dimy,dimz,colToCoord,coordToCol)

        # print(study)
        # print(subject)
        # print(ntrials)
        # print(nvoxels)
        # print(dimx)
        # print(dimy)
        # print(dimz)
        # print(colToCoord)
        # print(len(colToCoord))
        # print(coordToCol)
        # print(len(coordToCol))
        # print(len(coordToCol[0]))
        # print(len(coordToCol[0][0]))

        # print(colToCoord[0:999,0])
        # print(colToCoord[0:999,1])
        # print(colToCoord[0:999,2])
        # pl.Plot(colToCoord[0:9999,0],colToCoord[0:9999,1],colToCoord[0:9999,2])

        # Plot(colToCoord[0:99,0],colToCoord[0:99,1],colToCoord[0:99,2])
