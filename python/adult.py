from sklearn.linear_model import LogisticRegression
from sklearn import cross_validation


samples = []
labels = []

with open('../data/a9a_dense.csv') as csvdata:
	for line in csvdata:
		vector = line.split(',')
		labels.append(float(vector[0]))
		sample = [float(feature) for feature in vector[1:]]
		samples.append(sample)


model = LogisticRegression()

print cross_validation.cross_val_score(model, samples, labels)


