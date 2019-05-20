import math


def solvequadratic():
    print("note: this program can not yet solve complex roots")
    print("note: this program does not solve the roots in any particular order")
    while True:
        # print(input(a)("what is variable a"))
        a = float(input("what is variable a"))
        if a == 0:
            print("sorry a can not equal zero")
        else:
            break

    b = float(input('what is variable b'))
    c = float(input('what is variable c'))
    try:
        root1 = ((-b - math.sqrt((b * b) - (4 * a * c))) / (2 * a))
        print("the first root is equal to", root1)
    except ValueError:
        print("sorry this set of numbers has complex roots")

    try:
        root2 = ((-b + math.sqrt((b * b) - (4 * a * c))) / (2 * a))
        print("the secont root is equal to", root2)
    except ValueError:
        print("")


def main():
    yn = str(input("do you want to solve a quadratic equation? "))

    if (yn == "yes"):
        solvequadratic()


main()
