import matplotlib.pyplot as plt
import csv
import numpy as np
import math



def run():
    x = []
    y = []
    z = []
    c = []

    with open('acath.csv', newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter=',')

        i=0
        for row in reader:
            if row[3] == "" or i==0:
                i += 1
                continue
            x.append(float(row[2]))
            y.append(float(row[3]))
            # z.append(float(row[3]))
            # c.append('r' if int(row[4]) == 1 else 'b')
            i+=1
    # fig = plt.figure()
    #
    # ax = fig.add_subplot(111, projection='3d')
    #
    # ax.scatter(xs = x, ys = y, zs = z, c = c)
    # plt.show()

    thetas = [230.3574273390882, -378.9608022291337, -1592.5045273885112]

    xs = np.arange(start=min(x), stop=max(x), step=0.1)
    ys = np.arange(start=min(y), stop=max(y), step=0.1)


    print(xs)
    zs = [sigmoid(thetas[0] + thetas[1] * a + thetas[2] * b) for a, b in zip(xs, ys)]

    fig = plt.figure()

    ax = fig.add_subplot(111, projection='3d')

    ax.scatter(xs = xs, ys = ys, zs = zs)
    plt.show()


def sigmoid(x):
    print(x)
    return 1 / (1 + math.exp(-x))

run()