import math
def gcf(l):
    p = primes(smaller(l))
    for i in p:
        if l[0] % i == 0 and l[1] % i == 0:
            # should find better method
            logs = []
            for c in range(0, int(math.log(l[0], i)) + 1):
                if l[0] % i ** c == 0 and l[1] % i ** c == 0:
                    logs.append(i ** c)
            return logs[-1]
    return 1


def smaller(l):
    s = l[0]
    for i in l:
        if i < s:
            s = i
    return s;


def is_prime(n):
    for i in range(2, n):
        if n % i == 0:
            return False
    return True


def primes(max):
    p = []
    for n in range(2, int(max) + 1):
        if is_prime(n):
            p.append(n)
    return p


def is_int(test):
    return math.ceil(test) == math.floor(test)

def lcm(n,on):
    try:
        if n<on:
            p=on
            on=n
            n=p
        if on<0:
            on=abs(on)
            for i in range(1, int(n*on+1))[]:
                if n*i%on==0:
                    return -i
        else:
            on = abs(on)
            for i in range(1, int(n * on + 1)):
                if n * i % on == 0:
                    return i
    except TypeError:
        try:
            if n.deci < on:
                p = on
                on = n
                n = p
                for i in range(-1, on.deci * n - 1):
                    if on.deci * i % n == 0:
                        return i
            else:
                for i in range(1, n.deci * on + 1):
                    if n.deci * i % on == 0:
                        return i

        except TypeError or AttributeError:
            try:
                if n.deci < on.deci:
                    p = on
                    on = n
                    n = p
                for i in range(1, on.deci * n.deci + 1):
                    if on.deci * i % n.deci == 0:
                        return i
            except AttributeError:
                if n < on.deci:
                    p = on
                    on = n
                    n = p
                for i in range(1, on * n.deci + 1):
                    if on * i % n.deci == 0:
                        return i
class fraction():

    def __init__(self, numerator, denomenator):
        self.numer = numerator
        self.deno = denomenator
        try:
            self.deci = numerator / denomenator

        except TypeError or AttributeError:

            try: self.deci=numerator/denomenator.deci

            except AttributeError or TypeError:

                try:
                    self.deci=numerator.deci/denomenator.deci

                except AttributeError:
                    self.deci=numerator.deci/denomenator
        try:
            self.name = numerator.name + "/" + str(denomenator)
        except AttributeError:
            self.name = str(numerator) + "/" + str(denomenator)


    def __add__(self, other):
        try:
            numer=(self.numer*(lcm(other.deno.deci, self.deno)/self.deno)+
                   (other.numer*(lcm(other.deno.deci, self.deno)/other.deno.deci)))
            deno=lcm(other.deno.deci, self.deno)
            return fraction(numer, deno)
        except AttributeError:
            numer = (self.numer * (lcm(other.deno, self.deno) / self.deno) +
                     (other.numer * (lcm(other.deno, self.deno) / other.deno)))
            deno = lcm(other.deno, self.deno)
            return fraction(numer, deno)



    def __sub__(self, other):
        numer = (self.numer * (lcm(other.deno, self.deno) / self.deno) - (other.numer * (lcm(other.deno, self.deno) / other.deno)))
        deno = lcm(other.deno, self.deno)
        return fraction(numer, deno)
"""
    def __init__(self, copy):
        numerator=copy.numer
        denomenator=copy.denoself.numer = numerator
        self.numer = numerator
        self.deno = denomenator
        try:
            self.deci = numerator / denomenator
        except ValueError:
            try: self.deci=numerator/denomenator.deci
            except ValueError:
                self.deci=numerator.deci/denomenator.deci
        self.name = str(numerator) + "/" + str(denomenator)
"""
'''
    def __init__(self, deci, overflowvar):
        overflowvar+=1
        self.numer = deci * math.pow(10, float(str(deci)[::-1].find('.')))
        self.deno = math.pow(10, (str(deci)[::-1].find('.')))
        gcd = gcf([self.numer, self.deno])
        self.numer /= int(gcd)
        self.deno /= int(gcd)
        self.name = str(int(self.numer)) + "/" + str(int(self.deno))
'''

def reduce(frac):
    gcd = gcf([frac.numer, frac.deno])
    frac.numer /= int(gcd)
    frac.deno /= int(gcd)
    # return fraction(numer, deno)


