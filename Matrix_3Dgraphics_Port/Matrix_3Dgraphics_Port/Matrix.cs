using System;
using System.Diagnostics.Eventing.Reader;
using System.Drawing.Design;
using System.Drawing.Printing;
using System.Xml.Serialization;

namespace Matrix_3Dgraphics_Port
{
    // i use the row-major order convention, for my sanity's sake


    // yes, i know. i am abusing generics, 
    // but i don't feel like writing 4 overloaded copies of all my operators
    // supports float, double, int, and uint, but technically any type would work, operators wouldn't work though
    public class Matrix<Arbtype>
    {
        //TODO: rewrite this to use non-generic types 
        public readonly Type MatType;

        // internally els is a row-major ordered generic array
        public Arbtype[] els { get; }

        public uint rows { get; }
        public uint cols { get; }


        // constructor defaults to row-major
        // if col-major is set true it will convert it to row major internally
        public Matrix(Arbtype[] list, uint row, uint col, bool colMaj = false)
        {
            MatType = list[0].GetType();

            if (!colMaj)
            {
                els = list;
                rows = row;
                cols = col;
            }
            else
            {
                els = ElsTransform();
                rows = row;
                cols = col;
            }
        }

        // copy constructor:
        public Matrix(Matrix<Arbtype> mat)
        {
            els = mat.els;
            rows = mat.rows;
            cols = mat.cols;
        }

        public Matrix(Vector3 vect)
        {
            els = (dynamic) new double[3] {vect.x, vect.y, vect.z};
            rows = 1;
            cols = 3;
        }

        public Matrix(Vector4 vect)
        {
            els = (dynamic) new double[4] {vect.x, vect.y, vect.z, vect.w};
            rows = 1;
            cols = 4;
        }

        public Matrix(Vector4[] vectlist)
        {
            Arbtype[] vectEls = new Arbtype[4 * vectlist.Length];
            for (int i = 0; i < vectlist.Length; i++)
            {
                vectEls[4 * i] = (dynamic) vectlist[i].x;
                vectEls[4 * i + 1] = (dynamic) vectlist[i].y;
                vectEls[4 * i + 2] = (dynamic) vectlist[i].z;
                vectEls[4 * i + 3] = (dynamic) vectlist[i].w;
            }

            els = vectEls;
            rows = (uint) vectlist.Length;
            cols = 4;
        }

        public Matrix(Vector3[] vectlist)
        {
            Arbtype[] vectEls = new Arbtype[3 * vectlist.Length];
            for (int i = 0; i < vectlist.Length; i++)
            {
                vectEls[3 * i] = (dynamic) vectlist[i].x;
                vectEls[3 * i + 1] = (dynamic) vectlist[i].y;
                vectEls[3 * i + 2] = (dynamic) vectlist[i].z;
            }

            els = vectEls;
            rows = (uint) vectlist.Length;
            cols = 3;
        }


        public Vector4[] ToVector4s(bool colMajor = false)
        {
            Vector4[] vectlist = new Vector4[this.rows];

            for (int i = 0; i < this.rows; i++)
            {
                double[] v = new double[4];
                for (int j = 0; j < 4; j++)
                {
                    if (!colMajor)
                    {
                        v[j] = (dynamic) this.getItem(i, j);
                    }
                    else
                    {
                        v[j] = (dynamic) this.getItem(j, i);
                    }
                }

                vectlist[i] = new Vector4(v);
            }

            return vectlist;
        }


        public static Matrix<Arbtype> identity(uint dim)
        {
            // yeah, this is particularly agregeos
            var idEls = new Arbtype[dim * dim];
            for (var i = 0; i < dim; i++) idEls[i * dim + i] = (dynamic) 1;
            return new Matrix<Arbtype>(idEls, dim, dim);
        }

        public Arbtype getItem(int i, int j)
        {
            return els[cols * i + j];
        }

        public void setItem(int i, int j, Arbtype val)
        {
            els[cols * i + j] = val;
        }

        // All operations preserve Matrix type(besides * for matricies of diff types)
        public Matrix<Arbtype> Transform()
        {
            return new Matrix<Arbtype>(ElsTransform(), cols, rows);
        }

        private Arbtype[] ElsTransform()
        {
            var NewEls = new Arbtype[rows * cols];


            for (var i = 0; i < rows; i++)
            for (var j = 0; j < cols; j++)
                NewEls[i * cols + j] = els[j * rows + i];

            return NewEls;

            //TODO: return transformed version of els
        }

        private Arbtype[] ListTransform(Arbtype[] list, uint r, uint c)
        {
            Arbtype[] NewEls = new Arbtype[r * c];


            for (uint i = 0; i < r; i++)
            {
                for (uint j = 0; j < c; j++)
                {
                    NewEls[i * c + j] = list[j * r + i];
                }
            }

            return NewEls;
        }

        private static double[] ListTransformD(double[] list, uint r, uint c)
        {
            var NewEls = new double[r * c];


            for (uint i = 0; i < r; i++)
            for (uint j = 0; j < c; j++)
                NewEls[i * c + j] = list[j * r + i];

            return NewEls;
        }


        public static Matrix<Arbtype> operator +(Matrix<Arbtype> uno, Matrix<Arbtype> dos)
        {
            if (uno.rows != dos.rows || uno.cols != dos.cols)
                throw new InvalidOperationException("The matricies must be the same size for addition");

            var newEls = new Arbtype[uno.rows * uno.cols];
            for (var i = 0; i < dos.rows * dos.cols; i++)
                //yes, i am abusing generics and this is shitty code, deal with it
                newEls[i] = (dynamic) uno.els[i] + (dynamic) dos.els[i];
            return new Matrix<Arbtype>(newEls, uno.rows, uno.cols);
        }

        public static Matrix<Arbtype> operator -(Matrix<Arbtype> uno, Matrix<Arbtype> dos)
        {
            if (uno.rows != dos.rows || uno.cols != dos.cols)
                throw new InvalidOperationException("The matricies must be the same size for subtraction");

            var newEls = new Arbtype[uno.rows * uno.cols];
            for (var i = 0; i < dos.rows * dos.cols; i++)
                //yes, i am abusing generics and this is shitty code, deal with it
                newEls[i] = (dynamic) uno.els[i] - (dynamic) dos.els[i];
            return new Matrix<Arbtype>(newEls, uno.rows, uno.cols);
        }

        //TODO: much later: optimize matrix multiplication current = O(n^3)
        public static Matrix<Arbtype> operator *(Matrix<Arbtype> uno, Matrix<Arbtype> dos)
        {
            // if it does not work throw it out
            if (uno.cols != dos.rows) throw new InvalidOperationException("matricies are of incompatable size");
            // making it implicitly cast to double makes my life easier, if it was anything els you can just cast the vals to ints or whatever later
            var prdct = new Arbtype[uno.rows * dos.cols];
            for (var i = 0; i < uno.rows; i++)
            for (var j = 0; j < dos.cols; j++)
            {
                // i is the row it takes and j is the col it takes
                // it then stores a dot product of those and then appends that val to the product mat
                dynamic dot_prdct = 0;
                for (var dotLoop = 0; dotLoop < dos.cols; dotLoop++)
                    dot_prdct += (dynamic) dos.els[(uno.cols * dotLoop) + j] *
                                 (dynamic) uno.els[(i * dos.cols) + dotLoop];

                // append dot product
                prdct[i * dos.cols + j] = dot_prdct;
            }

            var arb = new Matrix<Arbtype>(prdct, uno.rows, dos.cols);

            return arb;
        }

        public static Matrix<Arbtype> operator *(Matrix<Arbtype> uno, Arbtype dos)
        {
            var newEls = new Arbtype[uno.els.Length];
            for (uint i = 0; i < uno.els.Length; i++) newEls[i] = (dynamic) uno.els[i] * (dynamic) dos;
            return new Matrix<Arbtype>(newEls, uno.rows, uno.cols);
        }

        public static Matrix<Arbtype> operator ^(Matrix<Arbtype> uno, int n)
        {
            if (uno.rows != uno.cols)
                throw new InvalidOperationException("A non-square matrix times itself cannot exsist");

            if (n > 1)
            {
                var i = 0;
                var output = new Matrix<Arbtype>(uno);

                while (i < n - 1) output = output * uno;

                return output;
            }

            if (n == 1) return uno;
            if (n == 0) return identity(uno.rows);
            if (n < 0)
            {
                //TODO: write inverse function
            }

            return uno;
        }

        public override string ToString()
        {
            var comp = "\n";
            for (uint i = 0; i < rows; i++)
            {
                comp += "[ ";
                for (uint j = 0; j < cols; j++)
                    if (j == cols - 1)
                        comp += els[i * cols + j].ToString();
                    else
                        comp += els[i * cols + j].ToString() + ", ";
                comp += "]\n";
            }

            return comp;
        }

        public double det()
        {
            return determinate(els, rows);
        }

        public double minor(uint row_avoid, uint col_aviod)
        {
            if (rows != cols) throw new InvalidOperationException("only a square matrix can have a minor");
            return minor(els, row_avoid, col_aviod, rows, cols);
        }

        public Matrix<Arbtype> Inverse()
        {
            if (rows != cols) throw new InvalidOperationException("Only square matricies can be inverted");
            var adjointMat = new Arbtype[els.Length];

            double det;
            double invDet;
            adjointMat = (dynamic) AdjointMat(els, rows, cols);
            det = determinate(els, rows);

            if (det == 0) throw new InvalidOperationException("Matrix must have non-zero determinate");
            invDet = 1 / det;

            for (uint i = 0; i < els.Length; i++)
                adjointMat[i] *= (dynamic) invDet;
            // note: its only realy acurate to ~10 decimal places
            return new Matrix<Arbtype>(adjointMat, rows, rows);
        }

        // helpers for det and inverse funcs:
        private double minor(Arbtype[] els, uint row_avoid, uint col_avoid, uint rows, uint cols)
        {
            var smlr_mat = new Arbtype[(rows - 1) * (cols - 1)];
            if (els == smlr_mat) throw new Exception("given an empty matrix");
            uint mInd = 0;
            for (var i = 0; i < els.Length / rows; i++)
            {
                for (var j = 0; j < els.Length / cols; j++)
                {
                    if (j != col_avoid && i != row_avoid)
                    {
                        smlr_mat[mInd] = els[cols * i + j];
                        mInd++;
                    }
                }
            }

            // this makes a smaller matrix and returns the det of that
            return determinate(smlr_mat, rows - 1);
        }

        private double determinate(Arbtype[] mat, uint row_len)
        {
            // this is what the minor func calls
            if (mat.Length % row_len == 0)
            {
                // throws error if not square

                double det;
                if (mat.Length < 1)
                {
                    throw new Exception("dug into base case, caught at mat length less than 1");
                }

                if (mat.Length == 1)
                {
                    //Console.WriteLine("debug4");
                    return (dynamic) mat[0];
                }

                if (mat.Length == 4)
                {
                    det = (dynamic) mat[0] * (dynamic) mat[3] - (dynamic) mat[2] * (dynamic) mat[1];
                    //Console.WriteLine("debug3");
                    return det;
                    //if mat is 2x2 then just solve it
                }

                det = 0.0;
                uint i = 0;
                while (i < row_len)
                {
                    //Console.WriteLine($"i is at: {i}");
                    if (i % 2 != 0)
                    {
                        det -= (dynamic) mat[i] * (dynamic) minor(mat, 0, i, row_len, row_len);
                        //Console.WriteLine("debug1");
                    }
                    else
                    {
                        det += (dynamic) mat[i] * (dynamic) minor(mat, 0, i, row_len, row_len);
                        //Console.WriteLine("debug2");
                    }

                    i++;
                }

                return det;
            }

            throw new InvalidOperationException("only square matricies have determinates");
        }

        private double[] AdjointMat(Arbtype[] els, uint rows, uint cols)
        {
            var cofmat = new double[rows * rows];
            // get mat of minors then mat of cofs
            for (uint i = 0; i < rows; i++)
            for (uint j = 0; j < cols; j++)
                // get minor then multiply by -1^i+j for neg checkering, +2 because computers start at 0,0 instead of 1,1
                cofmat[rows * j + i] = minor(els, i, j, rows, cols) * Math.Pow(-1, i + j + 2);

            return cofmat;
        }
    }

    // TODO: write Matrix4 with baked calculations and non-generic types for speed
    struct Matrix4
    {
    }


    public struct Vector3
    {
        public double x, y, z;

        public Vector3(double[] vals)
        {
            x = vals[0];
            y = vals[1];
            z = vals[2];
        }

        public Vector3(string str)
        {
            string[] strings = str.Split(' ');
            double[] vals = new double[3];

            for (int i = 0; i < 3; i++)
            {
                try
                {
                    vals[i] = Convert.ToDouble(strings[i]);
                }
                catch (Exception e)
                {
                    Console.WriteLine($"faliure to convert to double\n input string:{str}");
                }
            }

            this.x = vals[0];
            this.y = vals[1];
            this.z = vals[2];
        }

        public Vector3(double a, double b, double c)
        {
            x = a;
            y = b;
            z = c;
        }

        public Vector3(Vector4 vect)
        {
            x = vect.x / vect.w;
            y = vect.y / vect.w;
            z = vect.z / vect.w;
        }

        public Vector3(Matrix<double> mat)
        {
            x = mat.els[0];
            y = mat.els[1];
            z = mat.els[2];
        }

        public override string ToString()
        {
            return $"\n[{x},\n {y},\n {z}]\n";
        }


        public double getMag()
        {
            return Math.Sqrt((x * x) + (y * y) + (z * z));
        }

        public Vector3 Normalize()
        {
            double mag = this.getMag();
            return new Vector3(x / mag, y / mag, z / mag);
        }

        public static Vector3 operator +(Vector3 uno, Vector3 dos)
        {
            return new Vector3(uno.x + dos.x, uno.y + dos.y, uno.z + dos.z);
        }

        public static Vector3 operator -(Vector3 uno, Vector3 dos)
        {
            return new Vector3(uno.x - dos.x, uno.y - dos.y, uno.z - dos.z);
        }


        //scalar ops
        public static Vector3 operator *(double un, Vector3 dos)
        {
            return new Vector3(un * dos.x, un * dos.y, un * dos.z);
        }

        public static Vector3 operator /(Vector3 dos, double un)
        {
            return new Vector3(dos.x / un, dos.y / un, dos.z / un);
        }

        public double dot(Vector3 other)
        {
            //should i include w here?
            return x * other.x + y * other.y + z * other.z;
        }

        public Vector3 CrossProduct(Vector3 other)
        {
            return
                new Vector3(
                    y * other.z - z * other.y,
                    z * other.x - x * other.z,
                    x * other.y - y * other.x
                );
        }

        //todo: fix this
        public Vector3 ApplyMatrix(Matrix<double> M)
        {
            return new Vector3(new Matrix<double>(this) * M);
        }


        public Vector3 AppyMatrix(Matrix<double> M, ref double W)
        {
            if (M.cols != 4) throw new Exception("ApplyMatrixHomog called with bad context");

            if (M.cols == 4)
            {
                Vector3 V = new Vector3(); //w=1
                V.x = x * M.getItem(0, 0) + y * M.getItem(1, 0) + z * M.getItem(2, 0) + M.getItem(3, 0);
                V.y = x * M.getItem(0, 1) + y * M.getItem(1, 1) + z * M.getItem(2, 1) + M.getItem(3, 1);
                V.z = x * M.getItem(0, 2) + y * M.getItem(1, 2) + z * M.getItem(2, 2) + M.getItem(3, 2);
                double w = x * M.getItem(0, 3) + y * M.getItem(1, 3) + z * M.getItem(2, 3) + M.getItem(3, 3);

                if (w != 1)
                {
                    V.x /= w;
                    V.y /= w;
                    V.z /= w;
                }

                W = w;
                return V;
            }

            return new Vector3();
        }
    }


    // for representing verticies in homegenous coords
    // all functions are baked
    public struct Vector4
    {
        public double x, y, z, w;

        public Vector4(double[] vals)
        {
            x = vals[0];
            y = vals[1];
            z = vals[2];
            w = 0;
            if (vals.Length < 3) w = vals[3];
        }

        //assumes mat with 1 column, but doesn't matter.
        public Vector4(Matrix<double> mat)
        {
            x = mat.els[0];
            y = mat.els[1];
            z = mat.els[2];
            w = mat.els[3];
        }

        public Vector4(double a, double b, double c)
        {
            x = a;
            y = b;
            z = c;
            w = 1;
        }

        public Vector4(double a, double b, double c, double d)
        {
            x = a;
            y = b;
            z = c;
            w = d;
        }

        public Vector4(Vector3 vect, double vv)
        {
            x = vect.x;
            y = vect.y;
            z = vect.z;
            w = vv;
        }

        public Vector4(Vector3 vect)
        {
            x = vect.x;
            y = vect.y;
            z = vect.z;
            w = 1;
        }


        public double getMag()
        {
            return Math.Sqrt((x * x) + (y * y) + (z * z) + (w * w));
        }

        public Vector4 getCongj()
        {
            return new Vector4(-x, -y, -z, -w);
        }

        public Vector4 Normalize()
        {
            var mag = getMag();
            return new Vector4(x / mag, y / mag, z / mag, w / mag);
        }

        public static Vector4 operator +(Vector4 uno, Vector4 dos)
        {
            return new Vector4(uno.x + dos.x, uno.y + dos.y, uno.z + dos.z, uno.w + dos.w);
        }

        public static Vector4 operator -(Vector4 uno, Vector4 dos)
        {
            return new Vector4(uno.x - dos.x, uno.y - dos.y, uno.z - dos.z, uno.w + dos.w);
        }

        //scalar ops
        public static Vector4 operator *(double un, Vector4 dos)
        {
            return new Vector4(un * dos.x, un * dos.y, un * dos.z, un * dos.w);
        }

        public static Vector4 operator /(Vector4 dos, double un)
        {
            return new Vector4(dos.x / un, dos.y / un, dos.z / un, dos.w / un);
        }


        public double dot(Vector4 other)
        {
            //should i include w here?
            return x * other.x + y * other.y + z * other.z + w * other.w;
        }

        // the cross product does not exsist in spaces of dimention other than 3 or 7,
        // so i just ignore the w
        public Vector4 CrossProduct(Vector4 other)
        {
            return
                new Vector4(
                    y * other.z - z * other.y,
                    z * other.x - x * other.z,
                    x * other.y - y * other.x
                );
        }

        //row-major order
        public Vector4 ApplyMatrix(Matrix<double> mat, bool homog = true)
        {
            return new Vector4(new Matrix<double>(this) * mat);
            /*if (mat.cols != 4) return new Vector4(new Matrix<double>(this) * mat);
                else
                {
                    Vector4 V = new Vector4();
                    V.x = 
                    //todo: bake 44 matrix mult
                }
                */
        }

        public void PerpDiv()
        {
            x = x / w;
            y = y / w;
            z = z / w;
            w = 1;
        }
    }
}