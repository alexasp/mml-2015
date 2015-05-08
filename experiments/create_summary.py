import os

summaries_dir = 'summaries'

if not os.path.isfile(summaries_dir):
	os.makedirs(summaries_dir)


experiments = get_immediate_subdirectories(summaries_dir + '/' + sys.argv[0])

results = {}

for fileName in experiments:
	with open(fileName) as experiment_file:
		parameters = getParameters(fileName)
		updateResults(results, experiment_file, parameters)


def getParameters():
	parameters = line.strip().split("-")
	values = []
	for parameter in parameters:
		parts = parameter.split(",")
		values.append(parts[1])
	return tuple(values)

def updateResults(results, experiment_file, parameters):
	firstLine = experiment_file.readline()
	results[parameters] = 
	#TODO: each experiment has a number of iterations - compute averages!1l	



def get_immediate_subdirectories(a_dir):
    return [name for name in os.listdir(a_dir if name.startswith("eps"))
            if os.path.isdir(os.path.join(a_dir, name))]