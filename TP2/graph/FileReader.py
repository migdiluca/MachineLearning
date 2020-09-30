
def read_file(path):
    lines = open(path).read().splitlines()
    x = []
    y = []
    for line in lines:
        numbers = line.split(' ')
        x.append(float(numbers[0]))
        y.append(float(numbers[1]))
    return x, y
