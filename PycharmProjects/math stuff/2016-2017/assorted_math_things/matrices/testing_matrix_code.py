import time

from assorted_math_things.matrices.matrix_code import *

matrix = mat_input()
print('the inputted matrix is:')
print_mat(matrix.elements)
print('the determinate of the inputted matrix is: ', matrix.det(), '\n')
print('the matrix of minors of the inputted matrix is: ')
print_mat(matrix.minors())
print('the matrix of cofactors of the matrix of minors of the inputted matrix is: ')
print_mat(cofactors(matrix.minors()))
print('The adjugate of the cofactors of the minors of the inputted matrix is: ')
print_mat(transpose(cofactors(matrix.minors())))
print('The inverse of the inputted matrix is:')
start = time.process_time()
print_mat(matrix.inverse())
elapsed = time.process_time()
print('time elapsed is equal to: ', elapsed-start, 'seconds')


# my program returns a matrix object which can find it's own det and minors and cofactors and adjucgate and inverse
