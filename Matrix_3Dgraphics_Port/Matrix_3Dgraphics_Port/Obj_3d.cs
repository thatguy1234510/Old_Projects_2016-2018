using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace Matrix_3Dgraphics_Port
{
    //face is for processing of obj and other file formats
    internal struct Face
    {
        // this will be a 4 * n matrix for homogenous coordinates in 
        public Vector3[] verticies;
        public Color[] vertColors;
        public Vector3 normal;

        public override string ToString()
        {
            return "Verticies: "+new Matrix<double>(verticies).ToString()+"Normal:"+normal.ToString();
        }
    }

    
    internal struct PolyLine
    {
        public Vector4[] verticies;
    }

    public struct Material
    {
        public Color color;
        public double specular_reflection;
        public double diffuse_reflection;
        public double ambient_reflection;
        public int shininess;

        public Material(Color col, double spec, double diff, double amb, int shine)
        {
            color = col;
            specular_reflection = spec;
            diffuse_reflection = diff;
            ambient_reflection = amb;
            shininess = shine;
        }

    }

    class Obj_3d
    {

        
        public Face[] faces;
        public Material mat;

        public string name;

        //TODO: do somehting with these
        public Vector4 eulerAngles;
        public Vector3 location;

        public Obj_3d(Face[] f, Material m, string n)
        {
            faces = f;
            mat = m;
            name = n;
            
        }

        public Obj_3d(Obj_3d other)
        {
            faces = other.faces;
            mat = other.mat;
            name = other.name;
        }

    }
}

