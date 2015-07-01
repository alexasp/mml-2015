from sklearn.linear_model import LogisticRegression
from sklearn import cross_validation


samples = []
labels = []

with open('../data/australian_test_fixed.csv') as csvdata:
	for line in csvdata:
		vector = line.split(',')
		labels.append(int(vector[-1]))
		sample = [float(feature) for feature in vector[:-1]]
		samples.append(sample)


model = LogisticRegression()

print cross_validation.cross_val_score(model, samples, labels)


