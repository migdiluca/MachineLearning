import math
from typing import List

from sklearn.linear_model import LogisticRegression
from sklearn.metrics import confusion_matrix
from sklearn.model_selection import train_test_split
import csv
import numpy as np


def read_values(x_headers: List[str], y_header: str) -> (List[np.array], List):
    x_result = []
    y_result = []

    with open('./acath.csv', 'r') as file:
        reader = csv.reader(file)

        file_headers = next(reader)

        x_headers_indexes = [file_headers.index(header) for header in x_headers]
        y_header_index = file_headers.index(y_header)

        for row in reader:
            new_x = [float(row[i]) if row[i] else float("NaN") for i in x_headers_indexes]
            new_y = row[y_header_index]

            x_result.append(np.array(new_x))
            y_result.append(float(new_y))

    return x_result, y_result


def replace_empty_with_mean(x: List[np.array]):
    x_mean = np.transpose(np.array(x))

    for i in range(len(x[0])):
        mean = np.nanmean(x_mean[i])

        x_mean[i] = [mean if math.isnan(val) else val for val in x_mean[i]]
    return np.transpose(x_mean)


def apply_logistic_regression(headers):
    x, y = read_values(headers, 'sigdz')
    x_mean = replace_empty_with_mean(x)

    x_train, x_test, y_train, y_test = train_test_split(x_mean, y, test_size=0.2)

    clf = LogisticRegression(random_state=0).fit(x_train, y_train)

    y_pred = clf.predict(x_test)

    print(confusion_matrix(y_test, y_pred))

    return clf


def sigmoid(x):
    return 1.0 / (1 + np.exp(-x))


def predict_manually(clf, value):
    thetas = clf.coef_[0]

    theta0 = clf.intercept_

    dot = np.dot(value, thetas)

    print("Manualmente")
    print(sigmoid(dot + theta0))

    print("Por sklearn")
    print(clf.predict_proba([value]))


clf_without_sex = apply_logistic_regression(['age', 'cad.dur', 'choleste'])

apply_logistic_regression(['sex', 'age', 'cad.dur', 'choleste'])

predict_manually(clf_without_sex, np.array([60.0, 2.0, 199.0]))
