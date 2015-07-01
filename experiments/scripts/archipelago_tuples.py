from collections import namedtuple


Parameters = namedtuple('Parameters', 'epsilon regularization aggregation_cost peer_count group_size data_limit')
Metrics = namedtuple('Metrics', 'mean mean_std peer_std_mean peer_std_std max min')