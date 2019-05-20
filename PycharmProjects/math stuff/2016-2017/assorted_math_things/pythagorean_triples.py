import math
import time

def checkint(a, b):
    c = math.sqrt(a ** 2 + (b ** 2))
    if math.floor(c) == math.ceil(c):
        return True
    else:
        return False


def trypyths(lim, Interval):
    a = 3
    b = a
    pyths = []
    #print('debug')
    while a < lim:

        # print(a, b)
        b = b + Interval
        # print(b)
        if checkint(a, b):
            # print(a,b)
            pyths.append([a, b, math.sqrt(a ** 2 + (b ** 2))])
        if b > (a ** 2 - 1) / 2:
            # print('debug2')
            a += 1
            b = a
    return pyths


def checkels(pyths):
    elementals = []
    #print('debug 3')
    for i in pyths:
        # print(i)
        if el(i):
            elementals.append(i)

    return elementals


def el(i):
    n = 1
    while n < i[0]:
        n = n + 1
        if i[0] % n == 0 and i[1] % n == 0 and i[2] % n == 0:
            return False

    return True

# file=open('elemental_pyths','a')
'''
pyths = trypyths(1000, 1)
print('all pyths:', pyths)
print('\n')
print('elementals:', checkels(pyths))
'''
'''
for i in checkels(pyths):
    file.write(str(i))
    file.write('\n')
'''

def calc_alg_accuracy():
    elementals= [[3, 4, 5.0], [5, 12, 13.0], [7, 24, 25.0], [8, 15, 17.0], [9, 40, 41.0], [11, 60, 61.0], [12, 35, 37.0], [13, 84, 85.0], [15, 112, 113.0], [16, 63, 65.0], [17, 144, 145.0], [19, 180, 181.0], [20, 21, 29.0], [20, 99, 101.0], [21, 220, 221.0], [23, 264, 265.0], [24, 143, 145.0], [25, 312, 313.0], [27, 364, 365.0], [28, 45, 53.0], [28, 195, 197.0], [29, 420, 421.0], [31, 480, 481.0], [32, 255, 257.0], [33, 56, 65.0], [33, 544, 545.0], [35, 612, 613.0], [36, 77, 85.0], [36, 323, 325.0], [37, 684, 685.0], [39, 80, 89.0], [39, 760, 761.0], [40, 399, 401.0], [41, 840, 841.0], [43, 924, 925.0], [44, 117, 125.0], [44, 483, 485.0], [45, 1012, 1013.0], [47, 1104, 1105.0], [48, 55, 73.0], [48, 575, 577.0], [49, 1200, 1201.0], [51, 140, 149.0], [51, 1300, 1301.0], [52, 165, 173.0], [52, 675, 677.0], [53, 1404, 1405.0], [55, 1512, 1513.0], [56, 783, 785.0], [57, 176, 185.0], [57, 1624, 1625.0], [59, 1740, 1741.0], [60, 91, 109.0], [60, 221, 229.0], [60, 899, 901.0], [61, 1860, 1861.0], [63, 1984, 1985.0], [64, 1023, 1025.0], [65, 72, 97.0], [65, 2112, 2113.0], [67, 2244, 2245.0], [68, 285, 293.0], [68, 1155, 1157.0], [69, 260, 269.0], [69, 2380, 2381.0], [71, 2520, 2521.0], [72, 1295, 1297.0], [73, 2664, 2665.0], [75, 308, 317.0], [75, 2812, 2813.0], [76, 357, 365.0], [76, 1443, 1445.0], [77, 2964, 2965.0], [79, 3120, 3121.0], [80, 1599, 1601.0], [81, 3280, 3281.0], [83, 3444, 3445.0], [84, 187, 205.0], [84, 437, 445.0], [84, 1763, 1765.0], [85, 132, 157.0], [85, 3612, 3613.0], [87, 416, 425.0], [87, 3784, 3785.0], [88, 105, 137.0], [88, 1935, 1937.0], [89, 3960, 3961.0], [91, 4140, 4141.0], [92, 525, 533.0], [92, 2115, 2117.0], [93, 476, 485.0], [93, 4324, 4325.0], [95, 168, 193.0], [95, 4512, 4513.0], [96, 247, 265.0], [96, 2303, 2305.0], [97, 4704, 4705.0], [99, 4900, 4901.0], [100, 621, 629.0], [100, 2499, 2501.0]]
    alg_els=[]
    for i in range(3,101):
        if i%2!=0:
            alg_els.append([i,(i**2-1)/2,(i**2-1)/2+1])
        if i>4 and i%4==0:
            alg_els.append([i,(i**2)/4-1,(i**2)/4+1])
    els_alg_found=0
    for els in alg_els:
        if els in elementals:
            els_alg_found+=1
    print('the current algorithm found',str(els_alg_found/len(elementals)*100)+'%','of all tripples between 1 and 100')
#calc_alg_accuracy()


start = time.clock()
arb=checkels(trypyths(1000, 1))
elapsed = time.clock()
print('time elapsed is equal to: ', elapsed-start, 'seconds')