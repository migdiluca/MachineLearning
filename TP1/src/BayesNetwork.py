import copy

import pandas as pd


class BayesNetwork:
    probabilities = []
    nodes = []
    parents = {'rank': [], 'gre': ['rank'], 'gpa': ['rank'], 'admit': ['gre', 'gpa', 'rank']}
    node_values = {'rank': [1, 2, 3, 4], 'gre': [0, 1], 'gpa': [0, 1], 'admit': [0, 1]}
    data = None

    def __init__(self):
        data = pd.read_csv('../dataset/binary.csv')
        self.data = self.discretizar(data)
        self.nodes = data.columns.values

    def discretizar(self, data):
        data['gre'] = (data['gre'] >= 500).astype(int)
        data['gpa'] = (data['gpa'] > 3).astype(int)
        return data

    def joint_probability(self, conditions, missing_nodes):
        if len(missing_nodes) == 0:
            return self.conditional_solver(conditions)

        new_missing_nodes = copy.deepcopy(missing_nodes)
        selected_missing = new_missing_nodes.pop()
        result = 0
        for value in self.node_values[selected_missing]:
            new_conditions = copy.deepcopy(conditions)
            new_conditions[selected_missing] = value
            result += self.joint_probability(new_conditions, new_missing_nodes)

        return result

    def conditional_solver(self, conditions):
        result = 1
        for condition in conditions:
            denominador = self.data
            if len(self.parents[condition]) == 0:
                result *= (len(self.data[self.data[condition] == conditions[condition]]) + 1) / (len(self.data) + 1)
            else:
                for parent in self.parents[condition]:
                    denominador = denominador[denominador[parent] == conditions[parent]]

                nominador = copy.deepcopy(denominador)
                nominador = nominador[nominador[condition] == conditions[condition]]
                result *= (len(nominador) + 1) / (len(denominador) + 1)

        return result

    def conditional_probability(self, principal_condition, conditions):
        all_conditions = dict(conditions)
        all_conditions.update(principal_condition)
        missing_nodes = [item for item in self.nodes if item not in all_conditions.keys()]
        missing_nodes_2 = [item for item in self.nodes if item not in conditions.keys()]
        one = self.joint_probability(all_conditions, copy.deepcopy(missing_nodes))
        two = self.joint_probability(conditions, copy.deepcopy(missing_nodes_2))
        return one/two
