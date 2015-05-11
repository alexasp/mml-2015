import sys, pickle, os
from archipelago_tuples import Parameters, Metrics
import matplotlib.pyplot as plt
import pylab


chart_path = 'charts'

if not os.path.isdir(chart_path):
	os.makedirs(chart_path)

def meanPlotBy(summary_filename, x_axis_field):
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
		savefigure(figure, x_axis_values, means, summary_filename)


def savefigure(fig, x, y, summary_filename):
	axes = plt.gca()	
	axes.set_xlim([0,110])
	axes.set_ylim([0,0.5])
	

	ax = fig.add_subplot(111)
	for i,j in zip(x,y):
		ax.annotate(str(j)[:5],xy=(i,j))
	pylab.savefig(chart_path + '/' + summary_filename.split('/')[-1] + '.png')


def plot(plot_type, summary_filename):
	if plot_type == 'group_sizes':
		meanPlotBy(summary_filename, 'group_size')
	elif plot_type == 'peer_counts':
		meanPlotBy(summary_filename, 'peer_count')
	elif plot_type == 'epsilons':
		meanPlotBy(summary_filename, 'epsilon')
