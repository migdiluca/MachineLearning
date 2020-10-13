import matplotlib.pyplot as plt
import numpy as np


def read_csv(path):
    with open(path) as f:
        content = f.readlines()
    return [x.strip() for x in content]


def run():
    points = read_csv('../data/TP3-1.csv')
    weights = read_csv('../data/ej1a.csv')

    markers = {
        '1': 'o',
        '-1': 'x'
    }

    colors = {
        '1': 'b',
        '-1': 'r'
    }

    for x, y, c in [point.split(',') for point in points[1::]]:
        plt.scatter(float(x), float(y), marker=markers[c], color=colors[c])

    x = np.arange(0, 5, 0.01)
    c, a, b = [float(val) for val in weights[0].split(',')]
    plt.plot(np.arange(0, 5, 0.01), [- i * (a / b) - (c / b) for i in x])

    plt.show()


run()
