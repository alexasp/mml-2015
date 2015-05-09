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

		axes = plt.gca()
		axes.set_xlim([0,110])
		axes.set_ylim([0,0.5])

		plt.errorbar(peer_counts, means, stds, linestyle='None', marker='^')
		#plt.show()
		pylab.savefig(chart_path + '/' + summary_filename.split('/')[-1] + '.png')

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

		axes = plt.gca()
		axes.set_xlim([0,110])
		axes.set_ylim([0,0.5])

		plt.errorbar(groupSizes, means, stds, linestyle='None', marker='^')
		#plt.show()
		pylab.savefig(chart_path + '/' + summary_filename.split('/')[-1] + '.png')



plotType = sys.argv[2]

summary_filename = sys.argv[1]

if plotType == 'group_sizes':
	plotByGroupSize(summary_filename)
elif plotType == 'peer_counts':
	plotByPeerCounts(summary_filename)
