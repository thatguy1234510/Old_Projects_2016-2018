import matplotlib.pyplot as plt
import numpy as np


def in_julia_set(cmplx,bailout_val,critical_point):
    mb_iter=cmplx
    for i in range(bailout_val):
        mb_iter=mb_iter*mb_iter+critical_point
        if mb_iter.re>25 or mb_iter.im>25 or mb_iter.re<-25 or mb_iter.im<-25:
            return False
    return True

im_vals=[]
re_vals=[]
r=float(input('what is the real value of the critical point of the julia set? '))
i=float(input('what is the imaginary value of the critical point of the julia set? '))
print('Thinking...')
crit=complex(r,i)
for i in np.arange(-2,2,0.01):
    for j in np.arange(-2,2,0.01):
        possible_val=complex(i,j)
        if in_julia_set(possible_val,50,crit):
            im_vals.append(possible_val.im)
            re_vals.append(possible_val.re)

plt.scatter(re_vals,im_vals,0.75)
plt.show()