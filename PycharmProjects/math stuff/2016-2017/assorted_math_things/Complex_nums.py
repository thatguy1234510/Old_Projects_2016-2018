import math


class Complex:
    def __init__(self, re, im, p=False):
        if not p:
            self.re = re
            self.im = im
            try:
                self.arg = math.atan(self.im / self.re)
            except ZeroDivisionError:
                self.arg = 90
            self.mag = math.sqrt(math.pow(self.re, 2) + math.pow(self.im, 2))
        else:
            # if given in polar form re=R im=Arg
            self.re = re*math.sin(im)
            self.im = re*math.cos(im)



    def __init__(self, cmplx):
        self.re=cmplx.real
        self.im=cmplx.imag
        try:
            self.arg = math.atan(self.im / self.re)
        except ZeroDivisionError:
            self.arg = 90
        self.mag = math.sqrt(math.pow(self.re, 2) + math.pow(self.im, 2))
    def conjugate(self):
        return Complex(self.re, -self.im)

    def __str__(self):
        return str(self.re) + '+' + str(self.im) + 'i'

    def __add__(self, b):
        return Complex(self.re + b.re, self.im + b.im)

    def __sub__(self, b):
        return Complex(self.re - b.re, self.im - b.im)

    def __mul__(self, b):
        return Complex(self.re * b.re - (self.im * b.im),
                       (self.re * b.im + (self.im * b.re)))

    def __truediv__(self, b):
        conj = b.conjugate()
        quo = conj * b
        return Complex(self.re / quo, self.im / quo)

    def __pow__(self, pow):
        new = Complex(self.re, self.im)
        for i in range(pow-1):
            new = new * self
        return new



