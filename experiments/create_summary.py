import os, sys

def main():
	summaries_dir = 'summaries'

	if not os.path.isdir(summaries_dir):
		os.makedirs(summaries_dir)


	experiments = get_immediate_subdirectories(sys.argv[1], "eps")

	results = {}
	for experiment_dirname in experiments:
		print sys.argv[1] + "/" + experiment_dirname
		iterations_filenames = get_immediate_subfiles(sys.argv[1] + "/" + experiment_dirname, "eps")
		valueTuple = computeAverages(iterations_filenames, sys.argv[1] + "/" + experiment_dirname)
		parameters = getParameters(experiment_dirname)
		results[parameters] = valueTuple

	print results

def computeAverages(iterations_filenames, directory):
	mean, std, maximum, minimum = 0,0,0,0
	for iteration_filename in iterations_filenames:
		with open(directory + "/" + iteration_filename) as iteration_file:
			mean += float(iteration_file.readline())
			std += float(iteration_file.readline())
			maximum += float(iteration_file.readline())
			minimum += float(iteration_file.readline())
	mean = mean / float(len(iterations_filenames))
	std = std / float(len(iterations_filenames))
	maximum = maximum / float(len(iterations_filenames))
	minimum = minimum / float(len(iterations_filenames))
	return (mean, std, maximum, minimum)

def getParameters(experiment_dirname):
	parameters = experiment_dirname.strip().split("-")
	values = []
	for parameter in parameters:
		parts = parameter.split(",")
		values.append(parts[1])
	return tuple(values)


def get_immediate_subdirectories(a_dir, prefix):
    return [name for name in os.listdir(a_dir)
            if os.path.isdir(os.path.join(a_dir, name)) and name.startswith(prefix)]

def get_immediate_subfiles(a_dir, prefix):
    return [name for name in os.listdir(a_dir)
            if os.path.isfile(os.path.join(a_dir, name)) and name.startswith(prefix)]

main()
