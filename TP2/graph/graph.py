from FileReader import read_file
import matplotlib.pyplot as plt

x, y = read_file("../results/ID3")
x2, y2 = read_file("../results/RandomForest")
plt.plot(x, y, label = "ID3")
plt.plot(x2, y2, label = "RandomForest")
plt.ylabel('Precisión')
plt.xlabel('Porcentaje de testeo')
plt.legend()
plt.show()

x, y = read_file("../results/ID3WithDepthLimit")
x2, y2 = read_file("../results/ID3WithDepthLimitTraining")
plt.plot(x, y, label="Test")
plt.plot(x2, y2, label="Conjunto de entrenamiento")
plt.title("ID3 con límite de profundidad")
plt.ylabel('Precisión')
plt.xlabel('Límite de profundidad')
plt.legend()
plt.show()

x, y = read_file("../results/ID3WithLimit")
x2, y2 = read_file("../results/ID3WithLimitTraining")
plt.plot(x, y)
plt.plot(x, y, label="Test")
plt.plot(x2, y2, label="Conjunto de entrenamiento")
plt.title("ID3 con límite de observaciones")
plt.ylabel('Precisión')
plt.xlabel('Mínimo de observaciones')
plt.legend()
plt.show()

x, y = read_file("../results/RandomForestWithDepthLimit")
x2, y2 = read_file("../results/RandomForestWithDepthLimitTraining")
plt.plot(x, y, label="Test")
plt.plot(x2, y2, label="Conjunto de entrenamiento")
plt.title("Random forest con límite de profundidad")
plt.ylabel('Precisión')
plt.xlabel('Límite de profundidad')
plt.legend()
plt.show()


x, y = read_file("../results/RandomForestWithLimit")
x2, y2 = read_file("../results/RandomForestWithLimitTraining")
plt.plot(x, y, label="Test")
plt.plot(x2, y2, label="Conjunto de entrenamiento")
plt.plot(x, y)
plt.title("Random forest con límite de observaciones.")
plt.ylabel('Precisión')
plt.xlabel('Mínimo de observaciones')
plt.legend()
plt.show()
