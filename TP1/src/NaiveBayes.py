def len_datasets(datasets):
    return sum([len(dataset) for dataset in datasets])


class NaiveBayes:
    probabilities = []
    datasets = None

    # datasets es una lista de DataFrames
    def train(self, datasets, possible_values = None):
        self.datasets = datasets
        self.probabilities = []
        for dataset in datasets:
            probabilities = []
            for i in range(0, len(dataset.columns)):
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

                for item in dataset[dataset.columns[i]]:
                    if item in occurrences_map:
                        occurrences_map[item] += 1
                    else:
                        occurrences_map[item] = 1

                for key in occurrences_map.keys():
                    occurrences_map[key] = (occurrences_map[key] + 1) / (len(dataset) + len(occurrences_map.keys()))

                probabilities.append(occurrences_map)

            self.probabilities.append(probabilities)

    # to_analize es una lista con los valores a analizar
    def calculate_category(self, to_analize):
        results = []
        for j in range(0, len(self.probabilities)):
            probability = self.probabilities[j]
            result = 1
            for i in range(0, len(to_analize)):
                property_dict = probability[i]
                result *= property_dict[to_analize[i]]
            result *= len(self.datasets[j]) / len_datasets(self.datasets)
            results.append(result)

        return results.index(max(results))
