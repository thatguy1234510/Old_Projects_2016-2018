import math
import decimal


###############################################
def print_mat(mat):
    mat = clean_of_uglyness(mat)
    rounded_mat = rounding(mat)
    for i in range(len(rounded_mat)):
        print(' ', rounded_mat[i], '\n')


#
def mat_input():
    mat = []
    x = int(input('how many rows? '))
    y = int(input('how many columns? '))
    for i in range(x):
        print('input row', i + 1)
        row = [decimal.Decimal(i) for i in input().split()]
        # the user inputs numbers with a space or spaces between them
        # you could also split at commas but i like spaces better
        if len(row) < y or len(row) > y:
            print('this program only works for square matrices')
            raise ValueError
        mat.append(row)
    matrix = s_Mat(mat)
    return matrix


def clean_of_uglyness(matrix):
    for i in range(len(matrix)):
        for j in range(len(matrix[0])):
            matrix[i][j] = float(matrix[i][j])
    return matrix
    # in this program i use the decimal data
    # type because it is super precise but when you put it in a list
    # and print the list it prints [Decimal('arbitrary')] which i think is ugly,
    # so i convert it back to a float before printing which does not have much effect
    # because the calculations only need to be precise when you are doing math on them


def rounding(mat):
    rmat = []
    for arb in mat:
        rmat.append(arb)
    for i in range(len(rmat)):
        for j in range(len(mat[0])):
            rmat[i][j] = round(rmat[i][j], 5)
    return rmat


def swap(list_var, item1, item2):
    index1 = list_var.index(item1)
    index2 = list_var.index(item2)
    list_var[index1] = item2
    list_var[index2] = item1
    # this swaps
    return list_var


#
##
def minors(matrix):
    mat_of_minors = []
    for arb in matrix:
        mat_of_minors.append([])
    # this creates the rows of the matrix
    for row_avoid in range(len(matrix)):
        # cycle through the rows you avoid
        for column_avoid in range(len(matrix[row_avoid])):
            # cycle through the column you avoid
            smlr_mat = []
            for row_num in range(len(matrix)):
                # this cycles the row you are inspecting
                smlr_row = []
                for column_num in range(len(matrix[row_num])):
                    # this cycles the element you want
                    if column_num != column_avoid and row_num != row_avoid:
                        # if the column is not what we are avoiding
                        # and the row is not what we are avoiding we append it to a row
                        #  which we then append to the matrix
                        smlr_row.append(matrix[row_num][column_num])
                if len(smlr_row) > 0:
                    # this prevents an error i was getting
                    # where it would get to the row of the avoiding row and append nothing so it would be blank
                    # causing the determinate function to freak out
                    smlr_mat.append(smlr_row)

            # print('smaller mat: ', smlr_mat)
            det = find_higher_dets(smlr_mat)
            # print('det = ', det)
            # cofactor=det*((-1)**(row_avoid+column_avoid))

            mat_of_minors[row_avoid].append(det)
    return mat_of_minors


def find_smaller_dets(matrix, column_avoid):
    smlr_matrix = []
    smrow = []
    for row_num in range(1, len(matrix)):
        # increment the row we are working in starting after the first row
        for column_num in range(len(matrix[0])):
            # this goes through the columns one by one
            # print('column_num: ', column_num)

            if column_num != column_avoid:
                # print(I)
                # because i want all the elements that are not in the same row or column I is the column
                # and i test if it is equal to the column of the element it is currently on
                # print(matrix[row_num][column_num])
                smrow.append(matrix[row_num][column_num])
                # i append the elements to a row, then a full matrix

        smlr_matrix.append(smrow)
        smrow = []
        # reset the row
    # print(smlr_matrix)
    # print(find_higher_dets(smlr_matrix))
    return find_higher_dets(smlr_matrix)
    # the function is recursive


def find_higher_dets(matrix):
    # note: for some matrices the function simply crashes and i don't know why
    # note cont: however when it does not crash it always gets the right answer(so far)
    det = 0
    # the determinate
    try:
        if len(matrix) == len(matrix[0]):
            # if it is not square throw it out
            if len(matrix[0]) == 2:
                # once we break it down to two we can solve it
                det = matrix[0][0] * matrix[1][1] - (matrix[0][1] * matrix[1][0])
                return det

            else:
                for i in range(0, len(matrix[0]), 1):
                    # start on row 1 because row 0 is what we are multiplying by
                    if i % 2 != 0:
                        det -= matrix[0][i] * find_smaller_dets(matrix, i)
                    # every other sub-matrix we subtract
                    else:
                        det += matrix[0][i] * find_smaller_dets(matrix, i)
                return det
        else:
            return False
    except IndexError:
        return matrix[0][0]
        # this is for my matrix of minors program witch will sometimes send this function a matrix with one element,
        #  of course this will cause a list index error because there is not length to an int
        # the solution is to simply return the element of the matrix
        # because the determinate of a scalar is just the scalar


##
###
def cofactors(matrix):
    for row_num in range(len(matrix)):
        # counts row num
        for column_num in range(len(matrix[0])):
            # counts column num
            matrix[row_num][column_num] *= int(math.pow(-1, (row_num + column_num + 2)))
            # the cofactor of the minor is it times -1^(i+j)
            # but the computer does not see the first entry as 1,1 so you have to add 2
    return matrix


###
#####
def transpose(matrix):
    arbchar = 65
    els = []
    for row in range(len(matrix)):
        for col in range(len(matrix[0])):
            if matrix[row][col] in els:
                # only add a character if it is used more than once in the matrix
                arbchar += 1
                if chr(arbchar) != 'e' and chr(arbchar) != 'E':
                    matrix[row][col] = str(matrix[row][col]) + chr(arbchar)

                elif chr(arbchar) == 'e' or chr(arbchar) == 'E':
                    arbchar += 1
                    matrix[row][col] = str(matrix[row][col]) + chr(arbchar)
                    # you don't want to append e or E because the computer will get confused

            els.append(matrix[row][col])
    # the transpose part of the program does not care if it is dealing with strings or numbers
    # so in order to not confuse the dictionary of locations i add a letter to the number
    # then at the end i remove the letter so it can be handled by the other parts of the function
    locations = {}
    for row_num in range(len(matrix)):
        for column_num in range(len(matrix[0])):
            locations[matrix[row_num][column_num]] = [row_num, column_num]
    # this bit of code creates a dictionary with the value as the key and there location as the lock
    for starting_col in range(len(matrix)):
        matrix = replace_diagonal(matrix, 0, starting_col, locations)

    for starting_row in range(1, len(matrix)):
        matrix = replace_diagonal(matrix, starting_row, len(matrix) - 1, locations)

    # these two vars will first move through the columns then rows replacing the diagonals along the way

    return clean_mat_of_chars(matrix)


def find_diagonal(matrix, start_row, start_col):
    row = start_row
    col = start_col
    diagonal = []
    while len(matrix) - 1 >= row >= start_row and len(matrix[0]) - 1 >= col > -1:
        # this make sure that the column and the row never go over and loop around to the back of the matrix
        diagonal.append(matrix[row][col])
        col -= 1
        row += 1
    # this code goes through a matrix starting at some point and finds the diagonal
    return diagonal


def replace_diagonal(matrix, start_row, start_col, locations):
    diagonal = find_diagonal(matrix, start_row, start_col)
    diagonal2 = list(reversed(diagonal))
    for arb in diagonal:
        matrix[locations[arb][0]][locations[arb][1]] = diagonal2[diagonal.index(arb)]
        # this replaces the diagonal in the matrix with the reversed diagonal
    return matrix


def clean_mat_of_chars(matrix):
    for row_num in range(len(matrix)):
        for column_num in range(len(matrix[0])):
            # print('currently cleaning: ', matrix[row_num][column_num])
            try:
                matrix[row_num][column_num] = decimal.Decimal(
                    ''.join([c for c in matrix[row_num][column_num] if c in '1234567890.-E']))
                # this goes through and removes the characters added so that the dictionary works
            except (decimal.InvalidOperation, TypeError) as err:
                # in order to create this error you would need an element about as small as 0.0001 in your matrix
                #  or an element as big as 9.664726912162087650985347448383266932836477280943654477596282958984375E-9
                # this is rare but can screw things up if you don't catch it
                """
                i figured out why you get this error,
                you get it because you are converting something a decimal cant represent into a decimal
                but the computer can represent it in hardware so when you pass the error it still works
                """
                '''
                print('the program ran into an issue with 32bit floating point handlers')
                print('an element of the matrix was either ridiculously huge or tiny')
                print('could not clean', matrix[row_num][column_num])
                print(err)
                '''
                pass
                # this is super weird because after encountering this error it still outputs the correct answer
    return matrix


#####
######
def inverse(matrix):
    inverse_mat = transpose(cofactors(minors(matrix)))
    for row in range(len(inverse_mat)):
        for col in range(len(inverse_mat[0])):
            inverse_mat[row][col] = float(inverse_mat[row][col]) / float(find_higher_dets(matrix))
    return inverse_mat


######
###############################################
# for debugging the determinate function

"obj orntd code that is arguably worse"


class Matrix():
    def __init__(self, elements):
        self.elements = elements
        self.rows = len(self.elements)
        self.cols = len(self.elements[0])

    def veiw(self):
        for i in range(len(self.elements)):
            print(' ', self.elements[i], '\n')


class s_Mat(Matrix):
    def det(self):
        return find_higher_dets(self.elements)

    def minors(self):
        return minors(self.elements)

    def cofactors(self):
        return cofactors(self.elements)

    def adjugate(self):
        return transpose(self.elements)

    def inverse(self):
        return inverse(self.elements)
