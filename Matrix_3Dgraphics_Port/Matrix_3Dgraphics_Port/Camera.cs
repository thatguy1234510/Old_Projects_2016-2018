using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Matrix_3Dgraphics_Port
{
    //TODO: debug projection and normalization 
    struct Camera
    {
        public Vector3 loc;
        public Vector3 ViewTarget;
        public Vector3 viewDir;
        public Vector3 up;
        public Matrix<double> ToCameraCoordsMatrix;

        public Matrix<double> ProjectionMatrix;
        public Frustrum ProjectionFrustrum;

        public Camera(Vector3 loca, Vector3 vt, Vector3 u)
        {      
            loc = loca;
            ViewTarget = vt;
            viewDir = (ViewTarget-loc).Normalize();
            up = u;

            //here we make a matrix that will center the _Camera at zero looking down the z axis, while preserving object location
            Vector3 Zaxis;
            Vector3 Yaxis;
            Vector3 Xaxis;

            Zaxis = viewDir;
            Xaxis = u.CrossProduct(Zaxis).Normalize();
            Yaxis = u.Normalize();

            ToCameraCoordsMatrix = new Matrix<double>(
                new double[16]
                {
                    Xaxis.x, Yaxis.x, Zaxis.x, 0,
                    Xaxis.y, Yaxis.y, Zaxis.y, 0,
                    Xaxis.z, Yaxis.z, Zaxis.z, 0,
                    loca.x, loca.y, loca.z, 1
                }
                , 4, 4);

            ProjectionMatrix = Matrix<double>.identity(4);
            ProjectionFrustrum=new Frustrum();
        }

        public Camera(Vector3 loca, Vector3 vt)
        {
            
            loc = loca;
            ViewTarget = vt;
            viewDir = (ViewTarget-loc).Normalize();

            Vector3 rightVect = viewDir.CrossProduct(new Vector3(0, 1, 0));

            up = rightVect.CrossProduct(viewDir);

            //here we make a matrix that will center the _Camera at zero looking down the z axis, while preserving object location
            Vector3 Zaxis;
            Vector3 Yaxis;
            Vector3 Xaxis;

            Zaxis = viewDir;
            Xaxis = rightVect;
            Yaxis = Zaxis.CrossProduct(Xaxis);

            ToCameraCoordsMatrix = new Matrix<double>(
                new double[16]
                {
                    Xaxis.x, Yaxis.x, Zaxis.x, 0,
                    Xaxis.y, Yaxis.y, Zaxis.y, 0,
                    Xaxis.z, Yaxis.z, Zaxis.z, 0,
                    loca.x, loca.y, loca.z, 1
                }
                , 4, 4);

            ProjectionMatrix = Matrix<double>.identity(4);
            ProjectionFrustrum = new Frustrum();
        }


        public void MakeProjection(double n, double f, double fovy, double aspectRatio)
        {
            var top = Math.Tan(fovy / 2) * n;
            var bot = -top;
             
            var right = top * aspectRatio;
            var left = -top * aspectRatio;
            
            MakeProjection(new Frustrum(n,f,left,right,top,bot));
        }

        public void MakeProjection(double n, double f, double l, double r, double t, double b)
        {
            //this is the opengl projection matrix from a defined frustrum
            this.ProjectionMatrix = new Matrix<double>(
                new double[16]
                {
                    (2 * n) / (r - l), 0, (r + l) / (r - l), 0,
                    0, (2 * n) / (t - b), (t + b) / (t - b), 0,
                    0, 0, -((f + n) / (f - n)), -((2 * f * n) / (f - n)),
                    0, 0, -1, 0
                }, 4, 4).Transform();
        }

        public void MakeProjection(Frustrum F)
        {
            this.ProjectionFrustrum = F;
            this.ProjectionMatrix = new Matrix<double>(
                new double[16]
                {
                    (2 * F.n) / (F.r - F.l), 0, (F.r + F.l) / (F.r - F.l), 0,
                    0, (2 * F.n) / (F.t - F.b), (F.t + F.b) / (F.t - F.b), 0,
                    0, 0, -((F.f + F.n) / (F.f - F.n)), -((2 * F.f * F.n) / (F.f - F.n)),
                    0, 0, -1, 0
                }, 4, 4).Transform();
        }

        public void MakeProjection()
        {
            MakeProjection(this.ProjectionFrustrum);
        }
    }

    struct Frustrum
    {
        public double r,l,t,b,n,f;

        public Frustrum(double n, double f, double l, double r, double t, double b)
        {
            this.n = n;
            this.f = f;
            this.l = l;
            this.r = r;
            this.t = t;
            this.b = b;
        }

        
    }

}
