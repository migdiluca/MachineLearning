import math
from typing import List

import statsmodels.api as sm

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


def add_intercept(x):
    result = []
    for val in x:
        result.append(np.append([1], val))
    return result


def apply_logistic_regression(headers):
    x, y = read_values(headers, 'sigdz')
    x_mean = replace_empty_with_mean(x)

    x_mean = add_intercept(x_mean)

    x_train, x_test, y_train, y_test = train_test_split(x_mean, y, test_size=0.2)

    log_reg = sm.Logit(y_train, x_train).fit()

    y_pred = log_reg.predict(x_test)

    print(confusion_matrix(y_test, [1.0 if val > 0.5 else 0.0 for val in y_pred]))

    return log_reg


def sigmoid(x):
    return 1.0 / (1 + np.exp(-x))


def predict_manually(clf, value):
    print(clf.params)
    thetas = clf.params

    theta0 = thetas[0]

    dot = np.dot(value, thetas[1:])

    print("Manualmente")
    print(sigmoid(dot + theta0))

    print("Por statsmodel")
    print(clf.predict([np.append([1], value)])[0])


log_reg_without_sex = apply_logistic_regression(['age', 'cad.dur', 'choleste'])

log_reg_with_sex = apply_logistic_regression(['sex', 'age', 'cad.dur', 'choleste'])

predict_manually(log_reg_without_sex, np.array([60.0, 2.0, 199.0]))

print(log_reg_without_sex.summary())

print(log_reg_with_sex.summary())

