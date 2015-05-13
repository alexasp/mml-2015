from __future__ import print_function

__author__ = 'alex'

import sys
import random
from sklearn import preprocessing


def sample_training(samples, labels, training_ratio):
    zipped = zip(samples, labels)
    random.shuffle(zipped)
    train_max = int(training_ratio * len(zipped))
    train_samples = [sample for (sample, label) in zipped[:train_max]]
    train_labels = [label for (sample, label) in zipped[:train_max]]
    test_samples = [sample for (sample, label) in zipped[train_max:]]
    test_labels = [label for (sample, label) in zipped[train_max:]]
    return train_samples, train_labels, test_samples, test_labels


def write_data_set(output_file, samples, labels):
    with open(output_file, 'w') as csv_file:
        for i in range(len(samples)):
            print(','.join([str(feature) for feature in samples[i]]) + ',' + str(labels[i]), file=csv_file)


data_set_name = sys.argv[1]
training_ratio = float(sys.argv[2])
label_index = int(sys.argv[3])

samples = []
labels = []

with open(data_set_name) as csv_data:
    for line in csv_data:
        vector = line.split(',')
        labels.append(int(vector[label_index]))
        sample = [float(vector[i]) for i in range(len(vector)) if not i == label_index]
        samples.append(sample)

train_set, train_labels, test_set, test_labels = sample_training(samples, labels, training_ratio)

min_max_scaler = preprocessing.MinMaxScaler()

train_set = min_max_scaler.fit_transform(train_set)

test_set = min_max_scaler.transform(test_set)

write_data_set(data_set_name + '.train', train_set, train_labels)
write_data_set(data_set_name + '.test', test_set, test_labels)