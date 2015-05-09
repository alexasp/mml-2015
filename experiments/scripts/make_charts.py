import sys, pickle, os
from archipelago_tuples import Parameters, Metrics
import matplotlib.pyplot as plt
import pylab


chart_path = 'charts'

if not os.path.isdir(chart_path):
	os.makedirs(chart_path)

def plotByPeerCounts(summary_filename):
	with open(summary_filename) as summary_file:
		summary = pickle.load(summary_file)
		peer_counts = []
		means = []
		stds = []
		for entry in summary:
			peer_counts.append(int(entry.peer_count))
			means.append(float(summary[entry].mean))
			stds.append(float(summary[entry].std))
		print peer_counts
		print means
		print stds

		figure = plt.figure()
		plt.errorbar(peer_counts, means, stds, linestyle='None', marker='^')
		savefigure(figure, peer_counts, means, summary_filename)

def plotByGroupSize(summary_filename):
	with open(summary_filename) as summary_file:
		summary = pickle.load(summary_file)
		groupSizes = []
		means = []
		stds = []
		for entry in summary:
			groupSizes.append(int(entry.group_size))
			means.append(float(summary[entry].mean))
			stds.append(float(summary[entry].std))
		print groupSizes
		print means
		print stds

		
		figure = plt.figure()
		plt.errorbar(groupSizes, means, stds, linestyle='None', marker='^')
		savefigure(figure, groupSizes, means, summary_filename)


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
		plotByGroupSize(summary_filename)
	elif plot_type == 'peer_counts':
		plotByPeerCounts(summary_filename)
