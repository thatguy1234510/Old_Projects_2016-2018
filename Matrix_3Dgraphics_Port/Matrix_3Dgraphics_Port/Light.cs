using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Matrix_3Dgraphics_Port
{
    class Light
    {
        public Vector3 _Loc;
        public Vector3 _ViewTarget;
        public Vector3 _ViewDir;
        public Color _Color;
        public double diffuse_intensity;
        public double specular_intensity;
        public double ambient_intensity;

        public Matrix<double> LightCoordsMatrix;

        


        public Light(Vector3 l, Vector3 t, Color c,double d, double s, double a)
        {
            _Loc = l;
            _ViewTarget = t;
            _ViewDir = (_ViewTarget - _Loc).Normalize();
            _Color = c;
            diffuse_intensity = d;
            specular_intensity = s;
            ambient_intensity = a;
        }

        //public Bitmap ShadowMap(Sene sen);

    }
}
