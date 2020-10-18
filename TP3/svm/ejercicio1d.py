import numpy as np
from sklearn import svm


def read_csv(path):
    with open(path) as f:
        content = f.readlines()
    return [x.strip().split(",") for x in content]


def get_points(path):
    lines = read_csv(path)[1::]

    vectors = [[float(x), float(y)] for x, y, c in lines]
    categories = [1 if c == '1' else -1 for x, y, c in lines]

    clf = svm.SVC()
    clf.fit(vectors, categories)

    error_sum = 0
    for result, expected in zip(clf.predict(vectors), categories):
        error_sum += abs(result - expected)

    print(error_sum)


get_points('../data/TP3-1.csv')
get_points('../data/TP3-2.csv')

