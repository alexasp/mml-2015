from collections import namedtuple


Parameters = namedtuple('Parameters', 'epsilon regularization aggregation_cost peer_count group_size')
Metrics = namedtuple('Metrics', 'mean std max min')