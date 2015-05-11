import sys, pickle, os
from archipelago_tuples import Parameters, Metrics
import matplotlib.pyplot as plt
import pylab


chart_path = 'charts'

if not os.path.isdir(chart_path):
    os.makedirs(chart_path)


def mean_plot_by(summary_filename, x_axis_field, x_min, x_max):
    with open(summary_filename) as summary_file:
        summary = pickle.load(summary_file)
        x_axis_values = []
        means = []
        stds = []
        for entry in summary:
            x_axis_values.append(float(getattr(entry, x_axis_field)))
            means.append(float(summary[entry].mean))
            stds.append(float(summary[entry].std))
        print x_axis_values
        print means
        print stds

        figure = plt.figure()
        plt.errorbar(x_axis_values, means, stds, linestyle='None', marker='^')
        # plt.xscale('log')
        annotate(figure, x_axis_values, means)
        axes = plt.gca()
        axes.set_xlim([x_min, x_max])
        axes.set_ylim([0, 1.0])
        save_figure(summary_filename)
        # plt.show()


def annotate(fig, x, y):

    #
    ax = fig.add_subplot(111)
    for i, j in zip(x, y):
        ax.annotate(str(j)[:5], xy=(i, j))


def save_figure(summary_filename):
    pylab.savefig(chart_path + '/' + summary_filename.split('/')[-1] + '.png')


def plot(plot_type, summary_filename, x_min, x_max):
    mean_plot_by(summary_filename, plot_type, x_min, x_max)
