from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score
from sklearn import cross_validation


samples = []
labels = []

with open('../data/uci_spambase.csv.train') as csvdata:
	for line in csvdata:
		vector = line.split(',')
		labels.append(int(vector[-1]))
		sample = [float(feature) for feature in vector[:-1]]
		samples.append(sample)

testsamples = []
testlabels = []
with open('../data/uci_spambase.csv.test') as csvdata:
	for line in csvdata:
		vector = line.split(',')
		testlabels.append(int(vector[-1]))
		sample = [float(feature) for feature in vector[:-1]]
		testsamples.append(sample)


model = LogisticRegression()
model.fit(samples, labels)
print accuracy_score(model.predict(testsamples), testlabels)

print cross_validation.cross_val_score(model, samples, labels)


