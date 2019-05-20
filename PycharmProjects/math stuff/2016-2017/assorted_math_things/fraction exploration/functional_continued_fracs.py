from mpl_toolkits.mplot3d import Axes3D
import matplotlib.pyplot as plt
from matplotlib import cm
from matplotlib.ticker import LinearLocator, FormatStrFormatter
import numpy as np

from pprint import pprint,pformat
n = int(input('how levels deep do you want to go? '))


def convergeon(a,b):
    return -1/2*(-a+((a**2+(4*b))**(1/2))), -1/2*(-a-((a**2+(4*b))**(1/2)))


def plot3dphasespace(n):
    Xy = [0 for i in range(2*n)]
    Z= [Xy for i in range(2*n)]
    for i in range(0, 2*n): Z.append(Xy)
    def subsequence(list):
        c = 0
        run = [0]
        for i in range(len(list)):
            if list[i] < max(run):
                c+=1
                run=[list[i]]
            else:
                run.append(list[i])

        try:
            #return n / c
            return  n/c
        except ZeroDivisionError:
            return 0


    def seq(a,b):
        cfvals = [a]
        def func(x):
            try:
                    return a + (b / x)
            except ZeroDivisionError:
                    return 0
                    # resolve div by 0 errors
        for c in range(n):
            cfvals.append(func(cfvals[c]))
        return cfvals


    for i in range(0,2*n):
        for j in range(0, 2*n):
            if type(convergeon(i-n, j-n)[0])==type(complex(1,1)):
                Z[i][j] = subsequence(seq(i-n, j-n))
                #print('debub')
                #Z[i][j]=5

            elif type(convergeon(i-n, j-n)[0])!=type(complex(1,1)):
                #Z[i][j]=1
                print('fuck me')

    def p(list):
        for i in list:
            print(i)
    p(Z)

    # plt.imshow(np.array(Z), cmap='hot', interpolation='nearest')
    # plt.show()

'''
    fig = plt.figure()
    ax = fig.gca(projection='3d')

    X, Y = np.meshgrid(X, Y)
    Z = subsequence(seq(X, Y))

    # Plot the surface.
    surf = ax.plot_surface(X, Y, Z, cmap=cm.coolwarm, linewidth=0, antialiased=False)

    # Customize the z axis.
    ax.set_zlim(-1.01, 1.01)
    ax.zaxis.set_major_locator(LinearLocator(10))
    ax.zaxis.set_major_formatter(FormatStrFormatter('%.02f'))

    # Add a color bar which maps values to colors.
    fig.colorbar(surf, shrink=0.5, aspect=5)

    plt.show()
'''
plot3dphasespace(n)
