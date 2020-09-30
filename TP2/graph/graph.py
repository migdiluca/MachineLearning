from FileReader import read_file
import matplotlib.pyplot as plt

x, y = read_file("../results/ID3")
plt.plot(x, y)
plt.title("ID3 precisión")
plt.ylabel('Precisión')
plt.xlabel('Porcentaje de testeo')
plt.show()

x, y = read_file("../results/ID3WithDepthLimit")
plt.plot(x, y)
plt.title("ID3 con límite de profundidad")
plt.ylabel('Precisión')
plt.xlabel('Límite de profundidad')
plt.show()

x, y = read_file("../results/ID3WithDepthLimitTraining")
plt.plot(x, y)
plt.title("ID3 con límite de profundidad. Solo conjunto de entrenamiento.")
plt.ylabel('Precisión')
plt.xlabel('Límite de profundidad')
plt.show()

x, y = read_file("../results/ID3WithLimitTraining")
plt.plot(x, y)
plt.title("ID3 con límite de observaciones. Solo conjunto de entrenamiento.")
plt.ylabel('Precisión')
plt.xlabel('Límite de observaciones')
plt.show()

x, y = read_file("../results/ID3WithLimit")
plt.plot(x, y)
plt.title("ID3 con límite de observaciones")
plt.ylabel('Precisión')
plt.xlabel('Límite de observaciones')
plt.show()


x, y = read_file("../results/RandomForest")
plt.plot(x, y)
plt.title("RandomForest precisión")
plt.ylabel('Precisión')
plt.xlabel('Porcentaje de testeo')
plt.show()

x, y = read_file("../results/RandomForestWithDepthLimit")
plt.plot(x, y)
plt.title("Random forest con límite de profundidad")
plt.ylabel('Precisión')
plt.xlabel('Límite de profundidad')
plt.show()

x, y = read_file("../results/RandomForestWithDepthLimitTraining")
plt.plot(x, y)
plt.title("Random forest con límite de profundidad. Solo conjunto de entrenamiento.")
plt.ylabel('Precisión')
plt.xlabel('Límite de profundidad')
plt.show()

x, y = read_file("../results/RandomForestWithLimitTraining")
plt.plot(x, y)
plt.title("Random forest con límite de observaciones. Solo conjunto de entrenamiento.")
plt.ylabel('Precisión')
plt.xlabel('Límite de observaciones')
plt.show()

x, y = read_file("../results/RandomForestWithLimit")
plt.plot(x, y)
plt.title("Random forest con límite de observaciones")
plt.ylabel('Precisión')
plt.xlabel('Límite de observaciones')
plt.show()


