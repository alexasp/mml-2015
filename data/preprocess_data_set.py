from __future__ import print_function

__author__ = 'alex'

import sys
import random


def sample_training(samples, labels, training_ratio):
    record_count = len(samples)
    indices = range(record_count)
    random.shuffle(indices)
    train = [samples[i] for i in indices[0:int(training_ratio * record_count)]]
    train_labels = [labels[i] for i in indices[0:int(training_ratio * record_count)]]
    test = [samples[i] for i in indices[int(training_ratio * record_count):]]
    test_labels = [labels[i] for i in indices[int(training_ratio * record_count):]]
    return train, train_labels, test, test_labels


def calculate_rescale_0_1(training_set):
    scaling = []
    for feature_index in range(len(training_set[0])):
        minimum = float("inf")
        maximum = float("-inf")
        for sample in training_set:
            if sample[feature_index] < minimum:
                minimum = sample[feature_index]
            if sample[feature_index] > maximum:
                maximum = sample[feature_index]
        scaling.append({'min': minimum, 'max': maximum})
    return scaling


def rescale_0_1(samples, scaling):
    scaled_samples = []
    for sample in samples:
        scaled_sample = []
        for i in range(len(sample)):
            scaled_feature = (sample[i] - scaling[i]['min'])/(scaling[i]['max'] - scaling[i]['min'])
            scaled_sample.append(scaled_feature)
        scaled_samples.append(scaled_sample)
    return scaled_samples

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

scaling = calculate_rescale_0_1(train_set)

train_set = rescale_0_1(train_set, scaling)
test_set = rescale_0_1(test_set, scaling)

write_data_set(data_set_name + '.train', train_set, train_labels)
write_data_set(data_set_name + '.test', test_set, test_labels)