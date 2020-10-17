import matplotlib.pyplot as plt
import numpy as np


def read_csv(path):
    with open(path) as f:
        content = f.readlines()
    return [x.strip() for x in content]


def run(line_path):
    points = read_csv('../data/TP3-1.csv')
    weights = read_csv(line_path)

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

    x = np.arange(-0.5, 5.5, 0.01)
    a, b, c = [float(val) for val in weights[0].split(',')]
    print(a, b, c)
    plt.axis('scaled')
    plt.plot(x, [i * (-a / b) - (c / b) for i in x])
    plt.show()


run('../data/ej1a.csv')
run('../data/ej1b.csv')

