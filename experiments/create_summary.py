import os, sys, pickle
from scripts.archipelago_tuples import Parameters, Metrics
from scripts.make_charts import plot
import numpy


def main():
    experiment_identifier = sys.argv[1]
    x_value_field, y_value_field = sys.argv[2].split(',')
    x_min = float(sys.argv[3])
    x_max = float(sys.argv[4])
    log = len(sys.argv) == 6 and sys.argv[5] == 'log'

    if not os.path.isdir(experiment_identifier):
        target_experiment = 'output/most_recent'
        os.rename(target_experiment, experiment_identifier)

    summaries_dir = 'summaries'

    if not os.path.isdir(summaries_dir):
        os.makedirs(summaries_dir)

    experiments = get_immediate_subdirectories(experiment_identifier, "eps")

    results = {}
    for experiment_dirname in experiments:
        iterations_filenames = get_immediate_subfiles(experiment_identifier + '/' + experiment_dirname, "eps")
        valueMap = compute_averages(iterations_filenames, experiment_identifier + "/" + experiment_dirname)
        parameters = get_parameters(experiment_dirname)
        results[parameters] = valueMap

    output_path = summaries_dir + "/" + directory_name(experiment_identifier)
    with open(output_path, 'w') as stored_results:
        pickle.dump(results, stored_results)
    plot(x_value_field, y_value_field, output_path, x_min, x_max, log)


def compute_averages(iterations_filenames, directory):
    mean, maximum, minimum = 0, 0, 0
    means = []
    peer_stds = []
    for iteration_filename in iterations_filenames:
        with open(directory + "/" + iteration_filename) as iteration_file:
            means.append(float(iteration_file.readline()))
            peer_stds.append(float(iteration_file.readline()))
            maximum += float(iteration_file.readline())
            minimum += float(iteration_file.readline())
    mean = numpy.mean(means)
    mean_std = numpy.std(means)
    peer_std_std = numpy.std(peer_stds)
    peer_std_mean = numpy.mean(peer_stds)
    maximum /= float(len(iterations_filenames))
    minimum /= float(len(iterations_filenames))
    return Metrics(mean, mean_std, peer_std_mean, peer_std_std, maximum, minimum)


def get_parameters(experiment_dirname):
    parameters = experiment_dirname.strip().split("-")
    values = []
    for parameter in parameters:
        parts = parameter.split(",")
        values.append(parts[1])
    return Parameters(values[0], values[1], values[2], values[3], values[4], values[5])


def get_immediate_subdirectories(a_dir, prefix):
    return [name for name in os.listdir(a_dir)
            if os.path.isdir(os.path.join(a_dir, name)) and name.startswith(prefix)]


def get_immediate_subfiles(a_dir, prefix):
    return [name for name in os.listdir(a_dir)
            if os.path.isfile(os.path.join(a_dir, name)) and name.startswith(prefix)]


def directory_name(path):
    return path.split('/')[-1]


main()
