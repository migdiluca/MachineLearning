import pandas as pd
import re
import numpy as np
import matplotlib.pyplot as plt
from typing import List

from TP1.src.NaiveBayes import NaiveBayes


def confusion_matrix(nb: NaiveBayes, test: List[pd.DataFrame]):
    matrix = []
    for i in range(len(test)):
        matrix_row = [0] * len(test)
        for index, row in test[i].iterrows():
            res = nb.calculate_category(row.values)
            matrix_row[res] += 1
        matrix.append(matrix_row)
    return matrix


def add_columns_from_text(df: pd.DataFrame):
    for index, row in df.iterrows():
        title = re.sub('[.,;?!:]', '', row['titular'])
        words = title.lower().split(' ')

        for w in words:
            if w not in df:
                df[w] = [0] * len(df.values)
            df.at[index, w] = 1

    return df


def get_categories(df: pd.DataFrame):
    return [x for x in set(df['categoria']) if pd.notna(x)]


def separate_in_categories(df: pd.DataFrame, categories: List[str]):
    return [df[df['categoria'] == cat] for cat in categories]


def get_metrics(c_matrix: List[List[float]], categories: List[str]):
    all_metrics = {}
    for i in range(len(categories)):
        category = categories[i]

        tp = c_matrix[i][i]
        fn = sum([c_matrix[i][j] for j in range(len(categories)) if j != i])
        fp = sum([c_matrix[j][i] for j in range(len(categories)) if j != i])

        tn = - tp
        for a in range(len(categories)):
            for b in range(len(categories)):
                tn += c_matrix[a][b]

        acc = (tp + tn) / (tp + tn + fn + tn)
        pres = tp / (tp + fp)
        recall = tp / tp + fn
        f1_score = (2 * pres * recall) / (pres + recall)

        tp_rate = tp / (tp + fn)
        fp_rate = fp / (fp + tn)

        all_metrics[category] = {
            'TP': tp,
            'FN': fn,
            'FP': fp,
            'TN': tn,
            'Accuracy': acc,
            'Precision': pres,
            'F1-Score': f1_score,
            'FP-Rate': fp_rate,
            'TP-Rate': tp_rate
        }
    return all_metrics


def invert_dict(dic):
    inverted_dict = {}
    for (k, v) in dic.items():
        for (k_, v_) in v.items():
            if k_ not in inverted_dict:
                inverted_dict[k_] = {}

            inverted_dict[k_][k] = v_

    return inverted_dict


def roc_curve(metrics_dic):

    for k, v in metrics_dic.items():
        plt.scatter(v['FP-Rate'], v['TP-Rate'], label=k)

    plt.legend()
    plt.ylim(ymin=0, ymax=1)
    plt.xlim(xmin=0, xmax=1)
    plt.xlabel('FP-Rate')
    plt.ylabel('TP-Rate')
    plt.show()


def run():
    df = pd.read_csv('../dataset/Noticias_argentinas.csv', nrows=1000)

    df = df[['titular', 'categoria']]

    df = add_columns_from_text(df)

    train = df.sample(frac=0.8)
    test = df.drop(train.index)

    categories = get_categories(df)[0:4]

    train_datasets = separate_in_categories(train, categories)
    test_datasets = separate_in_categories(test, categories)

    for d in train_datasets: print(len(d))

    train_datasets = [df.drop(columns=['titular', 'categoria']) for df in train_datasets]
    test_datasets = [df.drop(columns=['titular', 'categoria']) for df in test_datasets]

    nb = NaiveBayes()
    nb.train(train_datasets)

    res = confusion_matrix(nb, test_datasets)
    res_dic = {categories[i]: {categories[j]: res[i][j] for j in range(len(res[i]))} for i in range(len(res))}

    print(pd.DataFrame.from_dict(res_dic))

    metrics_dic = get_metrics(res, categories)
    print(pd.DataFrame.from_dict(invert_dict(metrics_dic)))

    roc_curve(metrics_dic)



run()
