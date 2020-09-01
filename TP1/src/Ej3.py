import pandas as pd
import re

from typing import List

from TP1.src.NaiveBayes import NaiveBayes


def confusion_matrix(nb: NaiveBayes, test: List[pd.DataFrame]):
    matrix = []
    for i in range(len(test)):
        matrix_row = [0] * len(test)
        for index, row in test[i].iterrows():
            res = nb.calculate_category(row)
            matrix_row[res] += 1
        print(matrix_row)
        matrix.append(matrix_row)
    return matrix


def add_columns_from_text(df: pd.DataFrame):
    for index, row in df.iterrows():
        title = re.sub('[.,;?!:]', '', row['titular'])
        words = title.lower().split(' ')

        for w in words:
            if w not in df:
                df[w] = [0] * len(df)
            row[w] = 1

    return df


def get_categories(df: pd.DataFrame):
    return [x for x in set(df['categoria']) if pd.notna(x)]


def separate_in_categories(df: pd.DataFrame, categories: List[str]):
    return [df[df['categoria'] == cat] for cat in categories]


def run():
    df = pd.read_csv('../dataset/Noticias_argentinas.csv', nrows=1000)

    df = df[['titular', 'categoria']]

    train = df.sample(frac=0.8)
    test = df.drop(train.index)

    categories = get_categories(df)[0:4]

    print(categories)

    train_datasets = separate_in_categories(add_columns_from_text(train), categories)
    test_datasets = separate_in_categories(add_columns_from_text(test), categories)

    for d in train_datasets: print(len(d))

    train_datasets = [df.drop(columns=['titular', 'categoria']) for df in train_datasets]
    test_datasets = [df.drop(columns=['titular', 'categoria']) for df in test_datasets]

    nb = NaiveBayes()
    nb.train(train_datasets)

    confusion_matrix(nb, test_datasets)


run()
