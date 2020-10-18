from typing import Tuple, List

from PIL import Image
from sklearn import svm
from sklearn.model_selection import train_test_split
import matplotlib.pyplot as plt


class Point:
    def __init__(self, position: Tuple[int, int], color: Tuple[int, int, int], category: str):
        self.position = position
        self.color = color
        self.category = category


def image_to_points(image: Image, category: str) -> List[Point]:
    points: List[Point] = []

    w, h = image.size

    for x in range(w):
        for y in range(h):
            new_point = Point((x, y), image.getpixel((x, y)), category)
            points.append(new_point)

    return points


def category_to_color(category: str, color: Tuple[int, int, int]) -> Tuple[int, int, int]:
    x, y, z = color
    if category == 'cow':
        return x, y, 0
    elif category == 'sky':
        return x, 0, z
    else:
        return 0, y, z


def run(clf):
    full_image = Image.open('../data/images/cow.jpg')
    cow = image_to_points(Image.open('../data/images/vaca.jpg'), 'cow')
    sky = image_to_points(Image.open('../data/images/cielo.jpg'), 'sky')
    grass = image_to_points(Image.open('../data/images/pasto.jpg'), 'grass')
    
    test_size = 0.25

    cow_train, cow_test = train_test_split(cow, test_size=test_size)
    sky_train, sky_test = train_test_split(sky, test_size=test_size)
    grass_train, grass_test = train_test_split(grass, test_size=test_size)

    train = cow_train + sky_train + grass_train

    colors = [point.color for point in train]
    categories = [point.category for point in train]

    clf.fit(colors, categories)

    test = cow_test + sky_test + grass_test

    colors_test = [point.color for point in test]
    categories_test = [point.category for point in test]

    prediction = clf.predict(colors_test)

    errors = 0
    for expected, predicted in zip(categories_test, prediction):
        errors += 1 if expected != predicted else 0

    print('%s errores de %s predicciones (%.2f)' % (errors, len(prediction), float(errors)/len(prediction)))


run(svm.SVC())
