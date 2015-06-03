from sklearn.datasets import load_svmlight_file
import sys

path = sys.argv[1]
features = sys.argv[2]

X_train, y_train = load_svmlight_file(path, n_features=features)

y_train_list = y_train.tolist()
X_train_dense = X_train.todense().tolist()

with open(path + '_dense.csv', 'w') as new_file:
    for i in range(len(X_train_dense)):
        new_file.write(str(y_train_list[i]) + ',' + ','.join([str(x) for x in X_train_dense[i]]) + '\n')




