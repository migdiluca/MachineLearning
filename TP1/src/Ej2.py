import pandas as pd
from TP1.src.NaiveBayes import NaiveBayes


def divide_by_nationality(data, nationality):
    return data[data['Nacionalidad'] == nationality]


data = pd.read_excel('../PreferenciasBritanicos.xlsx', sheet_name='Hoja1', )

britanicos = divide_by_nationality(data, 'I')
escocesas = divide_by_nationality(data, 'E')

datasets = [britanicos, escocesas]

nb = NaiveBayes()
nb.train(datasets)

category = nb.calculate_category([1, 0, 1, 1, 0])

print("Es de Nacionalidad :" + str(datasets[category]['Nacionalidad'].values[0]))