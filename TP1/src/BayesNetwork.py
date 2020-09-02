import copy

import pandas as pd


class BayesNetwork:
    probabilities = {}
    nodes = []
    parents = None
    node_values = None
    data = None

    def __init__(self, data, parents, node_values):
        self.data = data
        self.nodes = data.columns.values
        self.parents = parents
        self.node_values = node_values

    def get_probabilities(self):
        for node in self.nodes:
            results = []
            for value in self.node_values[node]:
                result = []
                self.joint_solver({node: value}, self.parents[node], result)
                results += result
            self.probabilities[node] = results

    def joint_solver(self, conditions, missing_nodes, result):
        if len(missing_nodes) == 0:
            result.append({'probability': self.conditional_solver(conditions), 'conditions': copy.deepcopy(conditions)})
            return

        new_missing_nodes = copy.deepcopy(missing_nodes)
        selected_missing = new_missing_nodes.pop()
        for value in self.node_values[selected_missing]:
            new_conditions = copy.deepcopy(conditions)
            new_conditions[selected_missing] = value
            self.joint_solver(new_conditions, new_missing_nodes, result)

        return result

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

                nominador = denominador[denominador[condition] == conditions[condition]]
                result *= (len(nominador) + 1) / (len(denominador) + 1)

        return result

    def conditional_probability(self, principal_condition, conditions):
        all_conditions = dict(conditions)
        all_conditions.update(principal_condition)
        missing_nodes = [item for item in self.nodes if item not in all_conditions.keys()]
        missing_nodes_2 = [item for item in self.nodes if item not in conditions.keys()]

        return self.joint_probability(all_conditions, missing_nodes) / self.joint_probability(conditions, missing_nodes_2)
