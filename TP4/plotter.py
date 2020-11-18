import matplotlib.pyplot as plt
import csv




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
            x.append(float(row[1]))
            y.append(float(row[2]))
            z.append(float(row[3]))
            c.append('r' if int(row[4]) == 1 else 'b')
            i+=1
    fig = plt.figure()

    ax = fig.add_subplot(111, projection='3d')

    ax.scatter(xs = x, ys = y, zs = z, c = c)
    plt.show()

run()