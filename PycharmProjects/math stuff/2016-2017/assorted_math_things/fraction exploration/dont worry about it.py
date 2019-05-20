from Fracs import fraction
from matplotlib import pyplot as plt

a=int(input('what is a? '))
b=int(input('what is b? '))
r=int(input('how many entries? '))
frac=fraction(a, 1)+fraction(b, 1)
datafracs=[frac]
for i in range(r):
    frac=fraction(a, 1)+fraction(b, datafracs[i])
    print(frac.name)
    datafracs.append(frac)
print(s.name for s in datafracs[::-1])
decidata=[]
for i in datafracs[1::]:
    decidata.append(a+i.deci)

plt.plot(decidata)
plt.show()