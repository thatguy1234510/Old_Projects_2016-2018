using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Matrix_3Dgraphics_Port.File_handlers_and_or_helpers
{
    public static class Transform
    {
        /*
        //TODO: write these as 4x4 matricies
        public Matrix<double> MakeRotation(Vector3 xyzRot);

        // 3 for axis, 1 for rotation angle, 
        // in radians
        public Matrix<double> MakeRotation(Vector4 euler);

        public Matrix<double> MakeRotation(Vector3 axis, double rotation);
        */

        //returns 4x4
        public static Matrix<double> MakeTranslation(double x, double y, double z)
        {
            return new Matrix<double>(
                new double[]{
                    1,0,0,0,
                    0,1,0,0,
                    0,0,1,0,
                    x,y,z,1
                }, 4, 4);
        }

        //returns 4x4
        public static Matrix<double> MakeScale(double Xs, double Ys, double Zs)
        {
            return new Matrix<double>(
                new double[]
                             {Xs,0,0,0,
                              0,Ys,0,0,
                              0,0,Zs,0,
                              0,0,0,01}
                                      ,3,3);
        }


    }
}
