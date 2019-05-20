using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Matrix_3Dgraphics_Port
{
    struct Sene
    {
        public Camera _Camera;
        public Bitmap ShadowMap;
        public Light[] lights;
        public Color _AmbientColor;
        
        public List<Obj_3d> objects;
        public Matrix<double> WorldMatrix;

        public void add(Obj_3d n)
        {
            objects.Add(n);
        }

        public void makeShadowMap()
        {

        }


    }
    
}
