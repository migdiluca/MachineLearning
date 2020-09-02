from TP1.src.BayesNetwork import BayesNetwork
import pandas as pd

bn = BayesNetwork()

print("Resultado: " + str(bn.conditional_probability({'admit': 1}, {'rank': 2, 'gre': 0, 'gpa': 1})))

#
# data = pd.read_csv('../binary.csv')
# data['gre'] = (data['gre'] >= 500).astype(int)
# data['gpa'] = (data['gpa'] > 3).astype(int)
#
# res = 1
# pr = len(data[data['rank'] == 1]) / len(data)
#
# d = data[data['rank'] == 1]
# n = d[d['gpa'] == 0]
#
# n2 = d[d['gre'] == 0]
#
# da = data[(data['gre'] == 0) & (data['gpa'] == 0)]
# na = da[da['admit'] == 0]
# res = pr * ((len(n)+1)/(len(d)+1)) * ((len(n2)+1)/(len(d)+1)) * ((len(na)+1)/(len(da)+1))
#
# print(res)

#=> P(admit:0 | rank:1): 0.4321300321300321

#=> P(admit:1 | rank:2, gre:0, gpa:1): 0.21739130434782608

#0.003519
