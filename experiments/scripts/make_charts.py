import sys, pickle, os
import matplotlib.pyplot as plt
import pylab


chart_path = 'charts'
chart_point_path = 'chart_points'

if not os.path.isdir(chart_path):
    os.makedirs(chart_path)




def mean_plot_by(summary_filename, x_axis_field, x_min, x_max, y_axis_field, log_scale):
    with open(summary_filename) as summary_file:
        summary = pickle.load(summary_file)
        x_axis_values = []
        y_axis_values = []
        stds = []
        for entry in summary:
            x_axis_values.append(float(getattr(entry, x_axis_field)))
            y_axis_values.append(float(getattr(summary[entry], y_axis_field)))
            if y_axis_field == 'peer_std_mean':
                stds.append(float(summary[entry].peer_std_std))
            else:
                stds.append(float(summary[entry].mean_std))

        print x_axis_values
        print y_axis_values
        print stds

        figure = plt.figure()
        plt.errorbar(x_axis_values, y_axis_values, stds, linestyle='None', marker='^')
        if log_scale:
            plt.xscale('log')
        annotate(figure, x_axis_values, y_axis_values)
        axes = plt.gca()
        axes.set_xlim([x_min, x_max])
        axes.set_ylim([0, 1.0])
        save_figure(summary_filename, y_axis_field)
        save_figure_points(summary_filename, x_axis_values, y_axis_values, stds, y_axis_field)
        # plt.show()


def annotate(fig, x, y):

    #
    ax = fig.add_subplot(111)
    for i, j in zip(x, y):
        ax.annotate(str(j)[:5], xy=(i, j))


def save_figure(summary_filename, type):
    pylab.savefig(chart_path + '/' + summary_filename.split('/')[-1] + type + '.png')

def save_figure_points(summary_filename, x_axis_values, means, stds, type):
    zipped = zip(x_axis_values, means, stds)
    zipped = sorted(zipped, key=lambda x: x[0])
    x_axis_values = [tup[0] for tup in zipped]
    means = [tup[1] for tup in zipped]
    stds = [tup[2] for tup in zipped]
    with open(chart_point_path + '/' + summary_filename.split('/')[-1] + type + '.csv', 'w') as points_file:
        for i in range(len(x_axis_values)):
            points_file.write(','.join([str(x_axis_values[i]), str(means[i]), str(stds[i])]) + '\n')




def plot(x_field_name, y_field_name, summary_filename, x_min, x_max, log_scale):
    mean_plot_by(summary_filename, x_field_name, x_min, x_max, y_field_name, log_scale)
