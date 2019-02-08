"""
绘制loss折线图
"""

import sys
import json
import matplotlib.pyplot as plt

filePath = sys.argv[1]
title = sys.argv[2]
xlable = sys.argv[3]
ylabel = sys.argv[4]
file = open(filePath, "r")
load_dict = json.load(file)
file.close()
loss = load_dict['doubleArray']
plt.plot(range(1, len(loss)+1), loss)
plt.xlabel(xlable)
plt.ylabel(ylabel)
plt.title(title)
plt.show()


