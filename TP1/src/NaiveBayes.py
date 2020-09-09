def len_datasets(datasets):
    return sum([len(dataset) for dataset in datasets])


class NaiveBayes:
    probabilities = []
    datasets = None
    general_probabilities = []

    def sum_general_probabilities(self, ocurrencies, index):
        if len(self.general_probabilities) <= index:
            self.general_probabilities.append({})

        for key in ocurrencies:
            if key in self.general_probabilities[index]:
                self.general_probabilities[index][key] += ocurrencies[key]
            else:
                self.general_probabilities[index][key] = ocurrencies[key]

    # datasets es una lista de DataFrames
    def train(self, datasets, possible_values=None):
        self.datasets = datasets
        self.probabilities = []
        self.general_probabilities = []
        # Para cada conjunto
        for dataset in datasets:
            probabilities = []
            # Por cada propiedad
            for i in range(0, len(dataset.columns)):

                # Inicializamos las probabilidades locales
                if possible_values is None:
                    occurrences_map = {0: 0, 1: 0}
                else:
                    occurrences_map = {}
                    if isinstance(possible_values[0], list):
                        for value in possible_values[i]:
                            occurrences_map[value] = 0
                    else:
                        for value in possible_values:
                            occurrences_map[value] = 0

                # Sumamos las ocurrencias en la columna
                for item in dataset[dataset.columns[i]]:
                    if item in occurrences_map:
                        occurrences_map[item] += 1
                    else:
                        occurrences_map[item] = 1

                # Sumamos las ocurrencias generales
                self.sum_general_probabilities(occurrences_map, i)

                # Calculamos la probabilidad
                for key in occurrences_map.keys():
                    occurrences_map[key] = (occurrences_map[key] + 1) / (len(dataset) + len(occurrences_map.keys()))

                # Lo agrego a las probabilidades del dataset
                probabilities.append(occurrences_map)

            # Lo agrego a las probabilidades de los datasets
            self.probabilities.append(probabilities)

        # Calculo las probabilidades generales
        for column_map in self.general_probabilities:
            for key in column_map:
                column_map[key] = column_map[key] / len_datasets(datasets)

    # to_analize es una lista con los valores a analizar
    def calculate_category(self, to_analize):
        results = []
        # Por cada dataset
        for j in range(0, len(self.probabilities)):
            probability = self.probabilities[j]
            result = 1
            # Productoria de probabilidades
            for i in range(0, len(to_analize)):
                property_dict = probability[i]
                result *= property_dict[to_analize[i]]

            # Multiplico por la probabilidad que pertenezca al dataset
            result *= len(self.datasets[j]) / len_datasets(self.datasets)
            results.append(result)

        # Tomo el maximo
        return results.index(max(results))
